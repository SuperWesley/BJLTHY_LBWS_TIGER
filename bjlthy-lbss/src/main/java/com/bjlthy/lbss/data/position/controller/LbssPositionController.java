package com.bjlthy.lbss.data.position.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.data.position.domain.LbssPosition;
import com.bjlthy.lbss.data.position.service.ILbssPositionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 综采编码器数据Controller
 * 
 * @author zhangning
 * @date 2023-12-15
 */
@Controller
@RequestMapping("/data/position")
public class LbssPositionController extends BaseController
{
    private String prefix = "data/position";

    @Autowired
    private ILbssPositionService lbssPositionService;

    @RequiresPermissions("data:position:view")
    @GetMapping()
    public String position()
    {
        return prefix + "/position";
    }

    /**
     * 查询综采编码器数据列表
     */
    @RequiresPermissions("data:position:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssPosition lbssPosition)
    {
        startPage();
        List<LbssPosition> list = lbssPositionService.selectLbssPositionList(lbssPosition);
        return getDataTable(list);
    }

    /**
     * 导出综采编码器数据列表
     */
    @RequiresPermissions("data:position:export")
    @Log(title = "综采编码器数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssPosition lbssPosition)
    {
        List<LbssPosition> list = lbssPositionService.selectLbssPositionList(lbssPosition);
        ExcelUtil<LbssPosition> util = new ExcelUtil<LbssPosition>(LbssPosition.class);
        return util.exportExcel(list, "综采编码器数据数据");
    }

    /**
     * 新增综采编码器数据
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存综采编码器数据
     */
    @RequiresPermissions("data:position:add")
    @Log(title = "综采编码器数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssPosition lbssPosition)
    {
        return toAjax(lbssPositionService.insertLbssPosition(lbssPosition));
    }

    /**
     * 修改综采编码器数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssPosition lbssPosition = lbssPositionService.selectLbssPositionById(id);
        mmap.put("lbssPosition", lbssPosition);
        return prefix + "/edit";
    }

    /**
     * 修改保存综采编码器数据
     */
    @RequiresPermissions("data:position:edit")
    @Log(title = "综采编码器数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssPosition lbssPosition)
    {
        return toAjax(lbssPositionService.updateLbssPosition(lbssPosition));
    }

    /**
     * 删除综采编码器数据
     */
    @RequiresPermissions("data:position:remove")
    @Log(title = "综采编码器数据", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssPositionService.deleteLbssPositionByIds(ids));
    }
}
