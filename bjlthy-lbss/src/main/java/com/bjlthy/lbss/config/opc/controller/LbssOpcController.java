package com.bjlthy.lbss.config.opc.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;
import com.bjlthy.lbss.config.opc.service.ILbssOpcService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * opcua 配置Controller
 * 
 * @author zhangning
 * @date 2022-05-10
 */
@Controller
@RequestMapping("/lbss/config/opc")
public class LbssOpcController extends BaseController
{
    private String prefix = "mapper/config/opc";

    @Autowired
    private ILbssOpcService lbssOpcService;

    @RequiresPermissions("config:opc:view")
    @GetMapping()
    public String opc()
    {
        return prefix + "/opc";
    }

    /**
     * 查询opcua 配置列表
     */
    @RequiresPermissions("config:opc:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssOpc lbssOpc)
    {
        startPage();
        List<LbssOpc> list = lbssOpcService.selectLbssOpcList(lbssOpc);
        return getDataTable(list);
    }

    /**
     * 导出opcua 配置列表
     */
    @RequiresPermissions("config:opc:export")
    @Log(title = "opcua 配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssOpc lbssOpc)
    {
        List<LbssOpc> list = lbssOpcService.selectLbssOpcList(lbssOpc);
        ExcelUtil<LbssOpc> util = new ExcelUtil<LbssOpc>(LbssOpc.class);
        return util.exportExcel(list, "opcua 配置数据");
    }

    /**
     * 新增opcua 配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存opcua 配置
     */
    @RequiresPermissions("config:opc:add")
    @Log(title = "opcua 配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssOpc lbssOpc)
    {
        return toAjax(lbssOpcService.insertLbssOpc(lbssOpc));
    }

    /**
     * 修改opcua 配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssOpc lbssOpc = lbssOpcService.selectLbssOpcById(id);
        mmap.put("lbssOpc", lbssOpc);
        return prefix + "/edit";
    }

    /**
     * 修改保存opcua 配置
     */
    @RequiresPermissions("config:opc:edit")
    @Log(title = "opcua 配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssOpc lbssOpc)
    {
        return toAjax(lbssOpcService.updateLbssOpc(lbssOpc));
    }

    /**
     * 删除opcua 配置
     */
    @RequiresPermissions("config:opc:remove")
    @Log(title = "opcua 配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssOpcService.deleteLbssOpcByIds(ids));
    }
}
