package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssSysconfig;
import com.bjlthy.lbss.config.config.domain.LbssWorking;
import com.bjlthy.lbss.config.config.service.ILbssSysconfigService;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开关控制Controller
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Controller
@RequestMapping("/lbss/config/sysconfig")
public class LbssSysconfigController extends BaseController
{
    private String prefix = "mapper/config/sysconfig";

    @Autowired
    private ILbssSysconfigService lbssSysconfigService;

    @RequiresPermissions("config:sysconfig:view")
    @GetMapping()
    public String sysconfig()
    {
        return prefix + "/sysconfig";
    }
    /**
     * 查询开关控制列表
     */
    @RequiresPermissions("config:sysconfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssSysconfig lbssSysconfig)
    {
        startPage();
        List<LbssSysconfig> list = lbssSysconfigService.selectLbssSysconfigList(lbssSysconfig);
        return getDataTable(list);
    }

    /**
     * 导出开关控制列表
     */
    @RequiresPermissions("config:sysconfig:export")
    @Log(title = "开关控制", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssSysconfig lbssSysconfig)
    {
        List<LbssSysconfig> list = lbssSysconfigService.selectLbssSysconfigList(lbssSysconfig);
        ExcelUtil<LbssSysconfig> util = new ExcelUtil<LbssSysconfig>(LbssSysconfig.class);
        return util.exportExcel(list, "开关控制数据");
    }

    /**
     * 新增开关控制
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存开关控制
     */
    @RequiresPermissions("config:sysconfig:add")
    @Log(title = "开关控制", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssSysconfig lbssSysconfig)
    {
        return toAjax(lbssSysconfigService.insertLbssSysconfig(lbssSysconfig));
    }

    /**
     * 修改开关控制
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssSysconfig lbssSysconfig = lbssSysconfigService.selectLbssSysconfigById(id);
        mmap.put("lbssSysconfig", lbssSysconfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存开关控制
     */
    @RequiresPermissions("config:sysconfig:edit")
    @Log(title = "开关控制", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssSysconfig lbssSysconfig)
    {
        return toAjax(lbssSysconfigService.updateLbssSysconfig(lbssSysconfig));
    }

    /**
     * 删除开关控制
     */
    @RequiresPermissions("config:sysconfig:remove")
    @Log(title = "开关控制", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssSysconfigService.deleteLbssSysconfigByIds(ids));
    }
    
    /**
     * 
     * @author 张宁
     * @Description: 获取公司名称和系统名称
     * @return Map<String,String>    返回类型
     * @date 2022年9月7日 上午10:04:00
     * @throws
     */
    @PostMapping("/getCompanyName")
    @ResponseBody
    public Map<String, Object> getCompanyName()
    {
        String system_name = DictionariesHelper.getDicStringValueByCode("system_name");
        String company_name = DictionariesHelper.getDicStringValueByCode("company_name");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("system_name", system_name);
        map.put("company_name", company_name);

        List<LbssWorking> workingList = ConfigBean.workingList;
        List<String> beltNames = new ArrayList<>();
        for (LbssWorking working : workingList) {
        	String IP = ConfigBean.beltsMap.get(working.getBelt_name()).getBelt_ip();
        	map.put(IP, IP);
            beltNames.add(working.getBelt_name());
        }
        map.put("workingNames", beltNames);
//        map.put("workingList", workingList.toArray());
        return map;
    }

}
