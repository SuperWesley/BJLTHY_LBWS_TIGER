package com.bjlthy.lbss.dataComm.http;

import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.domain.LbssSysconfig;
import com.bjlthy.lbss.config.config.mapper.LbssARMConfigMapper;
import com.bjlthy.lbss.config.config.mapper.LbssSysconfigMapper;
import com.bjlthy.lbss.dataComm.socket.processor.config.NanoConfigProcessor;
import com.bjlthy.lbss.tool.*;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DWSUtil {

    public static JdbcTemplate jdbcTemplate = null;

    @Resource
    private static LbssARMConfigMapper lbssARMConfigMapper = SpringUtils.getBean(LbssARMConfigMapper.class);

    @Resource
    private static LbssSysconfigMapper lbssSysconfigMapper = SpringUtils.getBean(LbssSysconfigMapper.class);

    //日志工具类
    public static Logger log = LogbackUtil.getLogger("DWSUtil", "DWSUtil");

    private static Map<String,Integer> map = new HashMap<>();

    //记录与数据湖连接异常次数
    public static  int err = 0;
    /**
     * 根据工作面获取服务器数据库
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(){

        try {
            //获取服务地址
            String driver = "org.postgresql.Driver";
            String url ="jdbc:postgresql://10.3.10.85:8000/zhgkptdb";//连接地址
            String user ="hll_lthy_03";//用户
            String password ="lthy003.";//密码

            DriverManagerDataSource dataSource=new DriverManagerDataSource();
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driver);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            jdbcTemplate = new JdbcTemplate(dataSource);
        }catch (Exception e){
            err++;
            log.error("DWSUtil:getJdbcTemplate, 数据湖连接异常, {}",e);
        }


        return jdbcTemplate;
    }

    public static String getKuangBeltSpeed(String beltName){
        String belt_speed = "0.0";
        if(jdbcTemplate == null){
            if (err > 60){
                updateSysConfig(beltName);
            }else {
                jdbcTemplate = getJdbcTemplate();
            }

        }else {
            String belt_name_en = ConfigBean.beltsMap.get(beltName).getBelt_name_en();
            String deviceId = DictionariesHelper.getDicStringValueByCode(belt_name_en);

            String sql = "select belt_speed,event_time from dwi_zys.dwi_zys_belt_conveyor_info where device_id=? ";
            Map<String, Object> weightMap = jdbcTemplate.queryForMap(sql,deviceId);
            belt_speed = (String) weightMap.get("belt_speed");
            String event_time = (String) weightMap.get("event_time");

            String data = "speed:"+belt_speed+",event_time:"+event_time;
            String path = PathUtil.path+"/Belt/"+beltName+"/KuangSpeed/"+ DateUtil.getDate()+"/"+ DateUtil.getDateHour()+"_speed.txt";
            FileUtil.writeFileAndTime(path,data);
            long diffMin = DateUtil.getDiffMin(new Date(), DateUtil.parseDate(event_time));
            //当前时间 - 矿速度 > 5min 需要将速度切换为算法速度
            if (diffMin >= 10){
                Integer flag = map.get(beltName);
                if (flag == null || flag != 0){
                    updateNanoConfig(beltName,"0");
                    updateSysConfig(beltName);
                }
            }else{ //当前时间 - 矿速度 < 5min 需要将速度切换为矿速度
                Integer flag = map.get(beltName);
                if (flag == null || flag != 2){
                    updateNanoConfig(beltName,"2");
                }
            }
        }


        return belt_speed;
    }

    /**
     * 更新皮带基础配置关闭矿速度开关
     * @param beltName
     */
    private static void updateSysConfig(String beltName) {
        LbssSysconfig sysconfig = new LbssSysconfig();
        sysconfig.setBelt_name(beltName);
        sysconfig.setCode("kuang_speed_swtich");
        List<LbssSysconfig> lbssSysconfigList = lbssSysconfigMapper.selectLbssSysconfigList(sysconfig);
        LbssSysconfig config = lbssSysconfigList.get(0);
        config.setValue("3");
        lbssSysconfigMapper.updateLbssSysconfig(config);
    }

    public static void updateNanoConfig(String beltName,String value){
        try {
            LbssARMConfig armConfig = new LbssARMConfig();
            armConfig.setBelt_name(beltName);
            armConfig.setCode("speedSwitch");
            List<LbssARMConfig> lbssARMConfigs = lbssARMConfigMapper.selectLbssARMConfigList(armConfig);
            LbssARMConfig lbssARMConfig = lbssARMConfigs.get(0);
            lbssARMConfig.setValue(value);
            lbssARMConfigMapper.updateLbssARMConfig(lbssARMConfig);
            String ip = ConfigBean.beltsMap.get(beltName).getBelt_ip();
            NanoConfigProcessor.openARMConfigClient(ip);
            if (NanoConfigProcessor.session != null){
                String msg = "$CONFIG SpeedSwitch:"+value+" END$";
                NanoConfigProcessor.session.writeBuffer().write(msg.getBytes());
                NanoConfigProcessor.session.writeBuffer().flush();
                NanoConfigProcessor.log.info("DWSUtil:updateNanoConfig,"+beltName+"速度更新为数据湖速度,{}",msg);
                //记录数据更新信息
                map.put(beltName,Integer.valueOf(value));
            }
        }catch (Exception e){
            log.error("DWSUtil:updateNanoConfig,"+beltName+",{}",e);
        }

    }
}
