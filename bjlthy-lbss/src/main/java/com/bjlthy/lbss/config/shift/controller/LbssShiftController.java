package com.bjlthy.lbss.config.shift.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.IpUtils;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.service.ILbssShiftService;
import com.bjlthy.lbss.dataComm.socket.processor.config.NanoConfigProcessor;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DateUtil;
import com.bjlthy.lbss.tool.DictionariesHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 班次Controller
 * 
 * @author zn
 * @date 2020-11-10
 */
@Controller
@RequestMapping("/lbss/config/shift")
public class LbssShiftController extends BaseController
{
	private String prefix = "mapper/config/shift";

    @Autowired
    private ILbssShiftService LbssShiftService;

    @RequiresPermissions("config:shift:view")
    @GetMapping()
    public String shift()
    {
        return prefix + "/shift";
    }

    /**
     * 查询班次列表
     */
    @RequiresPermissions("config:shift:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssShift lbssShift)
    {
        startPage();
        List<LbssShift> list = LbssShiftService.selectLbssShiftList(lbssShift);
        return getDataTable(list);
    }

    /**
     * 导出班次列表
     */
    @RequiresPermissions("config:shift:export")
    @Log(title = "班次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssShift lbssShift)
    {
        List<LbssShift> list = LbssShiftService.selectLbssShiftList(lbssShift);
        ExcelUtil<LbssShift> util = new ExcelUtil<LbssShift>(LbssShift.class);
        return util.exportExcel(list, "班次数据");
    }

    /**
     * 新增班次
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存班次
     */
    @RequiresPermissions("config:shift:add")
    @Log(title = "班次", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LbssShift lbssShift)
    {
        return toAjax(LbssShiftService.insertLbssShift(lbssShift));
    }

    /**
     * 修改班次
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        LbssShift lbssShift = LbssShiftService.selectLbssShiftById(id);
        mmap.put("lbssShift", lbssShift);
        return prefix + "/edit";
    }

    /**
     * 修改保存班次
     */
    @RequiresPermissions("config:shift:edit")
    @Log(title = "班次", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LbssShift lbssShift) throws IOException {
        
        AjaxResult result = new AjaxResult();
        String msg = "";
        //工作面信息
        Set<String> beltNames = ConfigBean.beltsMap.keySet();
        for (String beltName : beltNames) {

            String ip = DictionariesHelper.getDicStringValueByCode(beltName+"-Nano_IP");
            boolean flag = IpUtils.ipDetection(ip,3000);
            if(flag){
                NanoConfigProcessor.openARMConfigClient(ip);
                if(NanoConfigProcessor.session != null){
                    //更新成功
                    result = toAjax(LbssShiftService.updateLbssShift(lbssShift));
                    long sec = DateUtil.getSec(lbssShift.getBegin());
                    String data = "$CONFIG "+lbssShift.getCode()+":"+sec+" END$";
                    NanoConfigProcessor.session.writeBuffer().write(data.getBytes());
                    NanoConfigProcessor.session.writeBuffer().flush();
                    NanoConfigProcessor.log.info(beltName+"更新班次时间-->"+data);
                    msg += beltName+",";
                }
            }
        }
        if(StringUtils.isNotEmpty(msg)){
            result.put("msg",msg+"更新成功");
            result.put("code",0);
        }else{
            result.put("msg","所有皮带秤更新失败");
            result.put("code",1);
        }
        return result;
    }

    /**
     * 删除班次
     */
    @RequiresPermissions("config:shift:remove")
    @Log(title = "班次", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(LbssShiftService.deleteLbssShiftByIds(ids));
    }
    
    /**
     * 校验班次名称
     */
    @PostMapping("/checkShiftNameUnique")
    @ResponseBody
    public String checkShiftNameUnique(LbssShift coalShift)
    {
        return LbssShiftService.checkShiftNameUnique(coalShift);
    }
}
