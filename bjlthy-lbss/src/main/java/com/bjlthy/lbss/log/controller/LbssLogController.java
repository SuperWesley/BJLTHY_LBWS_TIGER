package com.bjlthy.lbss.log.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.log.domain.LbssLog;
import com.bjlthy.lbss.log.service.ILbssLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 日志信息Controller
 * 
 * @author 张宁
 * @date 2021-08-09
 */
@Controller
@RequestMapping("/lbss/data/log")
public class LbssLogController extends BaseController
{
    private String prefix = "mapper/data/log";

    @Autowired
    private ILbssLogService lbssLogService;

    @RequiresPermissions("data:log:view")
    @GetMapping()
    public String log()
    {
        return prefix + "/log";
    }

    /**
     * 查询日志信息列表
     */
    @RequiresPermissions("data:log:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssLog lbssLog)
    {
        startPage();
        List<LbssLog> list = lbssLogService.selectLbssLogList(lbssLog);
        return getDataTable(list);
    }

    /**
     * 导出日志信息列表
     */
    @RequiresPermissions("data:log:export")
    @Log(title = "日志信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssLog lbssLog)
    {
        List<LbssLog> list = lbssLogService.selectLbssLogList(lbssLog);
        ExcelUtil<LbssLog> util = new ExcelUtil<LbssLog>(LbssLog.class);
        return util.exportExcel(list, "日志信息数据");
    }


    /**
     * 删除日志信息
     */
    @RequiresPermissions("data:log:remove")
    @Log(title = "日志信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(lbssLogService.deleteLbssLogByIds(ids));
    }
}
