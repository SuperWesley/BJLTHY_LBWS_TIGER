package com.bjlthy.lbss.dataComm.socket.processor.zero;

import com.alibaba.fastjson.JSONObject;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.errorcode.domain.LbssError;
import com.bjlthy.lbss.config.errorcode.service.ILbssErrorService;
import com.bjlthy.lbss.dataComm.redis.RedisTopic;
import com.bjlthy.lbss.tool.*;
import com.bjlthy.lbss.zero.info.domain.LbssZeroinfo;
import com.bjlthy.lbss.zero.info.mapper.LbssZeroinfoMapper;
import com.mysql.cj.protocol.MessageListener;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZeroDataSave {

    // 日志工具类
    private static Logger log = LogbackUtil.getLogger("ZeroDataSave", "ZeroDataSave");

    public static LbssZeroinfoMapper zeroInfoMapper = SpringUtils.getBean(LbssZeroinfoMapper.class);


    /**
     *
     * @author 张宁
     * @Description: 保存调零信息到数据库
     * @return void    返回类型
     * @date 2022年9月7日 下午7:47:09
     * @throws
     */
    public static void saveZeroInfo(String data,String ip){
        String msgs = data.replaceAll("\\$ZERO ","").replaceAll(" END\\$","");
        log.info(msgs);
        String zeros = "{"+msgs+"}";
        //转成JSONObject
        JSONObject object = JSONObject.parseObject(zeros);
        String time = object.get("time").toString();
        String zeronum = object.get("zeronum").toString();
        String status = object.get("status").toString();
        String code = object.get("remark").toString();
        String zeroValue = object.get("zeroValue").toString();
        //根据IP地址获取工作面信息
        String belt_name = ConfigBean.beltsIPMap.get(ip);
        //存入对象中
        LbssZeroinfo zeroInfo = new LbssZeroinfo();
        zeroInfo.setBelt_name(belt_name);
        zeroInfo.setTime(time);
        zeroInfo.setZeronum(Integer.valueOf(zeronum));
        zeroInfo.setStatus(status);
        zeroInfo.setZeroValue(zeroValue);
        LbssError error = new LbssError();
        error.setType("下位机设备");
        error.setSolution("zero");
        error.setCode(code);
        // 查询异常消息
        ILbssErrorService bean = SpringUtils.getBean(ILbssErrorService.class);
        List<LbssError> errors = bean.selectCoalErrorList(error);
        if (StringUtils.isNotEmpty(errors)){
            LbssError er = errors.get(0);
            zeroInfo.setRemark(er.getDescription());
        }

        //存入数据库中
        zeroInfoMapper.insertLbssZeroinfo(zeroInfo);

    }

    /**
     *
     * @author 张宁
     * @Description: 保存调零信息到文件
     * @return void    返回类型
     * @date 2022年9月7日 下午7:47:09
     * @throws
     */
    public static void saveZeroFile(String data,String ip){
        //根据IP地址获取工作面信息
        String belt_name = ConfigBean.beltsIPMap.get(ip);
        if (belt_name == null) {
            return;
        }
        //根据皮带秤名称获取工作面信息
        String working_area = ConfigBean.beltsMap.get(belt_name).getWorking_area();
        //根据工作面信息判断是否在服务器配置
        String server_name = ConfigBean.serverIp_Map.get(working_area);
        if (StringUtils.isNotEmpty(server_name)) {
            // 保存数据
            String month = DateUtil.getYear()+"-"+DateUtil.getMonth();
            //按照工作面信息存储到对应的实时数据表中
            String path = PathUtil.zeroPath+belt_name+"/ZeroValue/";
            FileUtil.writeFile(path + month + "_zero.txt", data);
        }
    }

}
