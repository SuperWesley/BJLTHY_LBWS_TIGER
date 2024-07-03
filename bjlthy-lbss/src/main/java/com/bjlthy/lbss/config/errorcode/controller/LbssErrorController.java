package com.bjlthy.lbss.config.errorcode.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.config.errorcode.domain.LbssError;
import com.bjlthy.lbss.config.errorcode.service.ILbssErrorService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 上位机界面异常表Controller
 * 
 * @date 2021-03-01
 */
@Controller
@RequestMapping("/lbss/config/errorcode")
public class LbssErrorController extends BaseController
{
    private String prefix = "mapper/config/errorcode";

    @Autowired
    private ILbssErrorService lbssErrorService;

    @RequiresPermissions("mapper:errorcode:view")
    @GetMapping()
    public String errorcode()
    {
        return prefix + "/errorcode";
    }

    /**
     * 查询上位机界面异常表列表
     */
    @RequiresPermissions("mapper:errorcode:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssError coalError)
    {
        startPage();
        List<LbssError> list = lbssErrorService.selectCoalErrorList(coalError);
        return getDataTable(list);
    }

    /**
     * 导出上位机界面异常表列表
     */
    @RequiresPermissions("mapper:errorcode:export")
    @Log(title = "上位机界面异常表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssError coalError)
    {
        List<LbssError> list = lbssErrorService.selectCoalErrorList(coalError);
        ExcelUtil<LbssError> util = new ExcelUtil<LbssError>(LbssError.class);
        return util.exportExcel(list, "errorcode");
    }

    /**
     * 新增上位机界面异常表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存上位机界面异常表
     */
    @RequiresPermissions("mapper:errorcode:add")
    @Log(title = "上位机界面异常表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssError coalError)
    {
        return toAjax(lbssErrorService.insertCoalError(coalError));
    }

    /**
     * 修改上位机界面异常表
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LbssError coalError = lbssErrorService.selectCoalErrorById(id);
        mmap.put("coalError", coalError);
        return prefix + "/edit";
    }

    /**
     * 修改保存上位机界面异常表
     */
    @RequiresPermissions("mapper:errorcode:edit")
    @Log(title = "上位机界面异常表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssError coalError)
    {
        return toAjax(lbssErrorService.updateCoalError(coalError));
    }

    /**
     * 删除上位机界面异常表
     */
    @RequiresPermissions("mapper:errorcode:remove")
    @Log(title = "上位机界面异常表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssErrorService.deleteCoalErrorByIds(ids));
    }
}
