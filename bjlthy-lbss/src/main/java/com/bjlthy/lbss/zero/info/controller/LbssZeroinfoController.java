package com.bjlthy.lbss.zero.info.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.zero.info.domain.LbssZeroinfo;
import com.bjlthy.lbss.zero.info.service.ILbssZeroinfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 调零信息Controller
 * 
 * @author zhangning
 * @date 2022-05-03
 */
@Controller
@RequestMapping("/zero/info")
public class LbssZeroinfoController extends BaseController
{
    private String prefix = "mapper/zero/info";

    @Autowired
    private ILbssZeroinfoService lbssZeroinfoService;

    @GetMapping()
    public String info()
    {
        return prefix + "/info";
    }

    /**
     * 查询调零信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssZeroinfo lbssZeroinfo)
    {
        startPage();
        List<LbssZeroinfo> list = lbssZeroinfoService.selectLbssZeroinfoList(lbssZeroinfo);
        return getDataTable(list);
    }

    /**
     * 导出调零信息列表
     */
    @RequiresPermissions("zero:info:export")
    @Log(title = "调零信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssZeroinfo lbssZeroinfo)
    {
        List<LbssZeroinfo> list = lbssZeroinfoService.selectLbssZeroinfoList(lbssZeroinfo);
        ExcelUtil<LbssZeroinfo> util = new ExcelUtil<LbssZeroinfo>(LbssZeroinfo.class);
        return util.exportExcel(list, "调零信息数据");
    }


}
