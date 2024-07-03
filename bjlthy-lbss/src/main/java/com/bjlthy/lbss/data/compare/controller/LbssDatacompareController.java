package com.bjlthy.lbss.data.compare.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.data.compare.domain.LbssDatacompare;
import com.bjlthy.lbss.data.compare.service.ILbssDatacompareService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据对比Controller
 * 
 * @author zhangning
 * @date 2022-05-03
 */
@Controller
@RequestMapping("/data/compare")
public class LbssDatacompareController extends BaseController
{
    private String prefix = "mapper/data/compare";

    @Autowired
    private ILbssDatacompareService lbssDatacompareService;

    @RequiresPermissions("data:compare:view")
    @GetMapping()
    public String compare()
    {
        return prefix + "/dataCompare";
    }

    /**
     * 查询数据对比列表
     */
    @RequiresPermissions("data:compare:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssDatacompare lbssDatacompare)
    {
        startPage();
        List<LbssDatacompare> list = lbssDatacompareService.selectLbssDatacompareList(lbssDatacompare);
        return getDataTable(list);
    }

    /**
     * 导出数据对比列表
     */
    @RequiresPermissions("data:compare:export")
    @Log(title = "数据对比", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssDatacompare lbssDatacompare)
    {
        List<LbssDatacompare> list = lbssDatacompareService.selectLbssDatacompareList(lbssDatacompare);
        ExcelUtil<LbssDatacompare> util = new ExcelUtil<LbssDatacompare>(LbssDatacompare.class);
        return util.exportExcel(list, "数据对比数据");
    }

    /**
     * 新增数据对比
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存数据对比
     */
    @RequiresPermissions("data:compare:add")
    @Log(title = "数据对比", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssDatacompare lbssDatacompare)
    {
        return toAjax(lbssDatacompareService.insertLbssDatacompare(lbssDatacompare));
    }

    /**
     * 修改数据对比
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssDatacompare lbssDatacompare = lbssDatacompareService.selectLbssDatacompareById(id);
        mmap.put("lbssDatacompare", lbssDatacompare);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据对比
     */
    @RequiresPermissions("data:compare:edit")
    @Log(title = "数据对比", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssDatacompare lbssDatacompare)
    {
        return toAjax(lbssDatacompareService.updateLbssDatacompare(lbssDatacompare));
    }

    /**
     * 删除数据对比
     */
    @RequiresPermissions("data:compare:remove")
    @Log(title = "数据对比", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssDatacompareService.deleteLbssDatacompareByIds(ids));
    }
}
