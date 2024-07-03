package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssLabelconfig;
import com.bjlthy.lbss.config.config.service.ILbssLabelconfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签配置Controller
 * 
 * @author zhangning
 * @date 2024-01-22
 */
@Controller
@RequestMapping("/lbss/config/labelconfig")
public class LbssLabelconfigController extends BaseController
{
    private String prefix = "mapper/config/labelconfig";

    @Autowired
    private ILbssLabelconfigService lbssLabelconfigService;

    @RequiresPermissions("config:labelconfig:view")
    @GetMapping()
    public String labelconfig()
    {
        return prefix + "/labelconfig";
    }

    /**
     * 查询标签配置列表
     */
    @RequiresPermissions("config:labelconfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssLabelconfig lbssLabelconfig)
    {
        startPage();
        List<LbssLabelconfig> list = lbssLabelconfigService.selectLbssLabelconfigList(lbssLabelconfig);
        return getDataTable(list);
    }

    /**
     * 导出标签配置列表
     */
    @RequiresPermissions("config:labelconfig:export")
    @Log(title = "标签配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssLabelconfig lbssLabelconfig)
    {
        List<LbssLabelconfig> list = lbssLabelconfigService.selectLbssLabelconfigList(lbssLabelconfig);
        ExcelUtil<LbssLabelconfig> util = new ExcelUtil<LbssLabelconfig>(LbssLabelconfig.class);
        return util.exportExcel(list, "标签配置数据");
    }

    /**
     * 新增标签配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存标签配置
     */
    @RequiresPermissions("config:labelconfig:add")
    @Log(title = "标签配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssLabelconfig lbssLabelconfig)
    {
        return toAjax(lbssLabelconfigService.insertLbssLabelconfig(lbssLabelconfig));
    }

    /**
     * 修改标签配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssLabelconfig lbssLabelconfig = lbssLabelconfigService.selectLbssLabelconfigById(id);
        mmap.put("lbssLabelconfig", lbssLabelconfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存标签配置
     */
    @RequiresPermissions("config:labelconfig:edit")
    @Log(title = "标签配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssLabelconfig lbssLabelconfig)
    {
        return toAjax(lbssLabelconfigService.updateLbssLabelconfig(lbssLabelconfig));
    }

    /**
     * 删除标签配置
     */
    @RequiresPermissions("config:labelconfig:remove")
    @Log(title = "标签配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssLabelconfigService.deleteLbssLabelconfigByIds(ids));
    }
}
