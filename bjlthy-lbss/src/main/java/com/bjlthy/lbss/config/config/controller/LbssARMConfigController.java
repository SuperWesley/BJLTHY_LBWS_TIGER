package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.IpUtils;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.service.ILbssARMConfigService;
import com.bjlthy.lbss.dataComm.socket.processor.config.NanoConfigProcessor;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 其他配置Controller
 * 
 * @author 张宁
 * @date 2021-01-03
 */
@Controller
@RequestMapping("/lbss/config/armconfig")
public class LbssARMConfigController extends BaseController
{
	private String prefix = "mapper/config/armconfig";

    @Autowired
    private ILbssARMConfigService lbssARMConfigService;

    @RequiresPermissions("config:armconfig:view")
    @GetMapping()
    public String armconfig()
    {
        return prefix + "/armconfig";
    }

    /**
     * 查询其他配置列表
     */
    @RequiresPermissions("config:armconfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssARMConfig lbssARMConfig)
    {
        startPage();
        List<LbssARMConfig> list = lbssARMConfigService.selectLbssARMConfigList(lbssARMConfig);
        return getDataTable(list);
    }

    /**
     * 导出其他配置列表
     */
    @RequiresPermissions("config:armconfig:export")
    @Log(title = "其他配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssARMConfig lbssARMConfig)
    {
        List<LbssARMConfig> list = lbssARMConfigService.selectLbssARMConfigList(lbssARMConfig);
        ExcelUtil<LbssARMConfig> util = new ExcelUtil<LbssARMConfig>(LbssARMConfig.class);
        return util.exportExcel(list, "其他配置数据");
    }

    /**
     * 新增其他配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存其他配置
     */
    @RequiresPermissions("config:armconfig:add")
    @Log(title = "其他配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssARMConfig lbssARMConfig)
    {
    	AjaxResult result = new AjaxResult();
        String msg = "";
        //工作面信息
        Set<String> beltNames = ConfigBean.beltsMap.keySet();
        for (String beltName : beltNames) {
            if(lbssARMConfig.getBelt_name().equals(beltName)){
                String ip = DictionariesHelper.getDicStringValueByCode(beltName+"-Nano_IP");
                boolean flag = IpUtils.ipDetection(ip,1000);
                if(flag){
                    NanoConfigProcessor.openARMConfigClient(ip);
                    if(NanoConfigProcessor.session != null){
                        //更新成功
                        result = toAjax(lbssARMConfigService.insertLbssARMConfig(lbssARMConfig));
                        NanoConfigProcessor.sendConfigInfo(beltName);
                        msg = beltName;
                    }
                }
            }
        }
        if(StringUtils.isNotEmpty(msg)){
            result.put("msg",msg+"更新成功");
            result.put("code",0);
        }else{
            result.put("msg","所有皮带秤更新失败");
            result.put("code",1);
        }

        return result;
    }

    /**
     * 修改其他配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssARMConfig lbssARMConfig = lbssARMConfigService.selectLbssARMConfigById(id);
        mmap.put("lbssarmconfig", lbssARMConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存其他配置
     */
    @RequiresPermissions("config:armconfig:edit")
    @Log(title = "其他配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssARMConfig lbssARMConfig)
    {
        AjaxResult result = new AjaxResult();
        String msg = "";
        //工作面信息
        Set<String> beltNames = ConfigBean.beltsMap.keySet();
        for (String beltName : beltNames) {
            if(lbssARMConfig.getBelt_name().equals(beltName)){
                String ip = DictionariesHelper.getDicStringValueByCode(beltName+"-Nano_IP");
                boolean flag = IpUtils.ipDetection(ip,1000);
                if(flag){
                    NanoConfigProcessor.openARMConfigClient(ip);
                    if(NanoConfigProcessor.session != null){
                        //更新成功
                        result = toAjax(lbssARMConfigService.updateLbssARMConfig(lbssARMConfig));
                        NanoConfigProcessor.sendConfigInfo(beltName);
                        msg = beltName;
                    }
                }
            }
        }
        if(StringUtils.isNotEmpty(msg)){
            result.put("msg",msg+"更新成功");
            result.put("code",0);
        }else{
            result.put("msg","所有皮带秤更新失败");
            result.put("code",1);
        }
    	return result;
    }

    /**
     * 删除其他配置
     */
    @RequiresPermissions("config:armconfig:remove")
    @Log(title = "其他配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssARMConfigService.deleteLbssARMConfigByIds(ids));
    }

 
    
    /**
     * 校验配置编码
     * @return 
     */
    @PostMapping("/checkConfigCodeUnique")
    @ResponseBody
    public String checkConfigCodeUnique(LbssARMConfig LbssARMConfig){
    	return lbssARMConfigService.checkConfigCodeUnique(LbssARMConfig);
    }
}
