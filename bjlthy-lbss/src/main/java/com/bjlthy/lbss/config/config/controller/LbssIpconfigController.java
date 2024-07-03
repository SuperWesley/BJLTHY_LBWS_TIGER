package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.IpUtils;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssIpconfig;
import com.bjlthy.lbss.config.config.service.ILbssIpconfigService;
import com.bjlthy.lbss.dataComm.socket.processor.relay.LidarRelayProcessor;
import com.bjlthy.lbss.dataComm.socket.processor.relay.RelayProcessor;
import com.bjlthy.lbss.tool.ConfigBean;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IP配置Controller
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Controller
@RequestMapping("/lbss/config/ipconfig")
public class LbssIpconfigController extends BaseController
{
    private String prefix = "mapper/config/ipconfig";
    public static Integer nano=2;
    public static Integer port=2;
    public static Integer lidar=2;

    @Autowired
    private ILbssIpconfigService lbssIpconfigService;

    @RequiresPermissions("config:ipconfig:view")
    @GetMapping()
    public String ipconfig()
    {
        return prefix + "/ipconfig";
    }

    /**
     * 查询IP配置列表
     */
    @RequiresPermissions("config:ipconfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssIpconfig lbssIpconfig)
    {
        startPage();
        List<LbssIpconfig> list = lbssIpconfigService.selectLbssIpconfigList(lbssIpconfig);
        return getDataTable(list);
    }

    /**
     * 导出IP配置列表
     */
    @RequiresPermissions("config:ipconfig:export")
    @Log(title = "IP配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssIpconfig lbssIpconfig)
    {
        List<LbssIpconfig> list = lbssIpconfigService.selectLbssIpconfigList(lbssIpconfig);
        ExcelUtil<LbssIpconfig> util = new ExcelUtil<LbssIpconfig>(LbssIpconfig.class);
        return util.exportExcel(list, "IP配置数据");
    }

    /**
     * 新增IP配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存IP配置
     */
    @RequiresPermissions("config:ipconfig:add")
    @Log(title = "IP配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssIpconfig lbssIpconfig)
    {
        return toAjax(lbssIpconfigService.insertLbssIpconfig(lbssIpconfig));
    }

    /**
     * 修改IP配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssIpconfig lbssIpconfig = lbssIpconfigService.selectLbssIpconfigById(id);
        mmap.put("lbssIpconfig", lbssIpconfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存IP配置
     */
    @RequiresPermissions("config:ipconfig:edit")
    @Log(title = "IP配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssIpconfig lbssIpconfig)
    {
        return toAjax(lbssIpconfigService.updateLbssIpconfig(lbssIpconfig));
    }

    /**
     * 删除IP配置
     */
    @RequiresPermissions("config:ipconfig:remove")
    @Log(title = "IP配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssIpconfigService.deleteLbssIpconfigByIds(ids));
    }
    
    /**
     * 获取电脑IP地址
     */
    @PostMapping("/getIP")
    @ResponseBody
    public Map<String,String> getIP(HttpServletRequest request)
    {
    	Map<String,String> map = new HashMap<String,String>();
    	List<LbssIpconfig> configList = lbssIpconfigService.selectLbssIpconfigList(null);
    	for (LbssIpconfig lbssConfig : configList) {
			if("IPv4".equals(lbssConfig.getCode())){
				map.put("IPv4", lbssConfig.getValue());
			}else if("MapperIP".equals(lbssConfig.getCode())){
				map.put("MapperIP", lbssConfig.getValue());
			}else if ("endpointUrl".equals(lbssConfig.getCode())) {
				String value = lbssConfig.getValue().split(":")[1];
				map.put("endpointUrl", value);
			}
		}
    	map.put("working_area", ConfigBean.belt_name);
        return map;
    }
    /***
     *
     * @author 张宁
     * @Description: 控制Nano 重启
     * @return Integer    返回类型
     * @date 2022年12月9日 下午4:22:24
     * @throws
     */
    @RequestMapping(value = "/kongzhiNano")
    @ResponseBody
    public Integer kongzhiNano(HttpServletRequest request) {
        String ip = request.getParameter("ip");
        if(IpUtils.ipDetection(ip,2000)){
            RelayProcessor.openRelayClient(ip);
            try {
                if (RelayProcessor.getSession() != null){
                    if (nano == 0){
                        //开启板子
                        byte[] buff = hexStringToByteArray("05");
                        //功能码
                        //buff[0] =  0x06;
                        RelayProcessor.sendData(buff);
                        nano = 1;
                    }else {
                        //关闭板子
                        byte[] buff = hexStringToByteArray("06");
                        //功能码
                        //buff[0] =  0x05;
                        RelayProcessor.sendData(buff);
                        nano = 0;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            nano = 2;
        }
        return nano;
    }

    /**
     *
     * @author 张宁
     * @Description: 控制串口屏重启
     * @return Integer    返回类型
     * @date 2022年12月9日 下午4:21:57
     * @throws
     */
    @RequestMapping(value = "/kongzhiport")
    @ResponseBody
    public Integer kongzhiport(HttpServletRequest request) {
        String ip = request.getParameter("ip");
        if(IpUtils.ipDetection(ip,2000)){
            RelayProcessor.openRelayClient(ip);
            try {
                if(RelayProcessor.getSession() != null){
                    if (port == 0){
                        //开启
                        byte[] buff = hexStringToByteArray("03");
                        //功能码
                        //buff[0] =  0x01;
                        RelayProcessor.sendData(buff);

                        port = 1;
                    }else {
                        //关闭
                        byte[] buff = hexStringToByteArray("04");
                        //功能码
                        //buff[0] = 0x02;
                        RelayProcessor.sendData(buff);

                        port = 0;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            port = 2;
        }

        return port;
    }

    /**
     *
     * @author 张宁
     * @Description: 控制激光雷达重启
     * @return Integer    返回类型
     * @date 2022年12月9日 下午4:21:38
     * @throws
     */
    @RequestMapping(value = "/kongzhiLidar")
    @ResponseBody
    public Integer kongzhiLidar(HttpServletRequest request) {
        String ip = request.getParameter("ip");
        if(IpUtils.ipDetection(ip,2000)){
            LidarRelayProcessor.openRelayClient(ip,"Lidar");
            lidar = 1;
        }else {
            lidar = 2;
        }

        return lidar;
    }
    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}
