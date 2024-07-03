package com.bjlthy.lbss.config.config.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.aspectj.lang.annotation.Log;
import com.bjlthy.framework.aspectj.lang.enums.BusinessType;
import com.bjlthy.lbss.config.config.domain.LbssVersion;
import com.bjlthy.lbss.config.config.service.ILbssVersionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目版本号Controller
 * 
 * @author zhangning
 * @date 2023-11-27
 */
@Controller
@RequestMapping("/lbss/config/version")
public class LbssVersionController extends BaseController
{
    private String prefix = "mapper/config/version";

    @Autowired
    private ILbssVersionService lbssVersionService;

    @RequiresPermissions("config:version:view")
    @GetMapping()
    public String version()
    {
        return prefix + "/version";
    }

    /**
     * 查询项目版本号列表
     */
    @RequiresPermissions("config:version:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssVersion lbssVersion)
    {
        startPage();
        List<LbssVersion> list = lbssVersionService.selectLbssVersionList(lbssVersion);
        return getDataTable(list);
    }

    /**
     * 导出项目版本号列表
     */
    @RequiresPermissions("config:version:export")
    @Log(title = "项目版本号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssVersion lbssVersion)
    {
        List<LbssVersion> list = lbssVersionService.selectLbssVersionList(lbssVersion);
        ExcelUtil<LbssVersion> util = new ExcelUtil<LbssVersion>(LbssVersion.class);
        return util.exportExcel(list, "项目版本号数据");
    }

    /**
     * 修改项目版本号
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssVersion lbssVersion = lbssVersionService.selectLbssVersionById(id);
        mmap.put("lbssVersion", lbssVersion);
        return prefix + "/edit";
    }

    /**
     * 修改保存项目版本号
     */
    @RequiresPermissions("config:version:edit")
    @Log(title = "项目版本号", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssVersion lbssVersion)
    {
        return toAjax(lbssVersionService.updateLbssVersion(lbssVersion));
    }

    /**
     * 删除项目版本号
     */
    @RequiresPermissions("config.version:remove")
    @Log(title = "项目版本号", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssVersionService.deleteLbssVersionByIds(ids));
    }
}
