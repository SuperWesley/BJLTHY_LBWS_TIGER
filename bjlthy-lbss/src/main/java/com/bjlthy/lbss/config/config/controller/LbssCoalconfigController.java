package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssCoalconfig;
import com.bjlthy.lbss.config.config.service.ILbssCoalconfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 煤量数据对比配置Controller
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Controller
@RequestMapping("/lbss/config/coalConfig")
public class LbssCoalconfigController extends BaseController
{
    private String prefix = "mapper/config/coalConfig";

    @Autowired
    private ILbssCoalconfigService lbssCoalconfigService;

    @RequiresPermissions("config:coalConfig:view")
    @GetMapping()
    public String coalConfig()
    {
        return prefix + "/coalConfig";
    }

    /**
     * 查询煤量数据对比配置列表
     */
    @RequiresPermissions("config:coalConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssCoalconfig lbssCoalconfig)
    {
        startPage();
        List<LbssCoalconfig> list = lbssCoalconfigService.selectLbssCoalconfigList(lbssCoalconfig);
        return getDataTable(list);
    }


    /**
     * 修改煤量数据对比配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssCoalconfig lbssCoalconfig = lbssCoalconfigService.selectLbssCoalconfigById(id);
        mmap.put("lbssCoalconfig", lbssCoalconfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存煤量数据对比配置
     */
    @RequiresPermissions("config:coalConfig:edit")
    @Log(title = "煤量数据对比配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssCoalconfig lbssCoalconfig)
    {
        return toAjax(lbssCoalconfigService.updateLbssCoalconfig(lbssCoalconfig));
    }

}
