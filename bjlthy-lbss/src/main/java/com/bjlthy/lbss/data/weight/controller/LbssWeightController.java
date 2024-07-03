package com.bjlthy.lbss.data.weight.controller;

import com.bjlthy.common.annotation.Log;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.enums.BusinessType;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.lbss.data.weight.domain.LbssWeight;
import com.bjlthy.lbss.data.weight.service.ILbssWeightService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 实时重量Controller
 * @date 2020年12月1日 下午7:20:24
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
@Controller
@RequestMapping("/lbss/weight")
public class LbssWeightController extends BaseController
{
    private String prefix = "mapper/weight";

    @Autowired
    private ILbssWeightService lbssWeightService;
    
    @RequiresPermissions("bjlthy:weight:view")
    @GetMapping()
    public String weight()
    {
        return prefix + "/weight";
    }
    
    /**
     * 
     * @author 张宁
     * @description 跳转到主界面
     * @param 参数
     * @date 2020年11月30日 下午3:49:52
     */
    @RequestMapping("/conveyor/index")
    public String main(Model model){
    	model.addAttribute("msg", null);
    	
    	return "conveyor/index"; 
    }
    /**
     * 查询实时重量列表
     */
    @RequiresPermissions("bjlthy:weight:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LbssWeight coalWeight)
    {
        startPage();
        List<LbssWeight> list = lbssWeightService.selectCoalWeightList(coalWeight);
        return getDataTable(list);
    }

    /**
     * 导出实时重量列表
     */
    @RequiresPermissions("bjlthy:weight:export")
    @Log(title = "实时重量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LbssWeight coalWeight)
    {
        List<LbssWeight> list = lbssWeightService.selectCoalWeightList(coalWeight);
        ExcelUtil<LbssWeight> util = new ExcelUtil<LbssWeight>(LbssWeight.class);
        return util.exportExcel(list, "weight");
    }

	/**
	 * 
	 * @author 张宁
	 * @description 实时查询过煤量数据
	 * @param 参数
	 * @date 2020年11月28日 下午6:11:32
	 */
    @RequestMapping("/getDetail")
    @ResponseBody
	public Map<String,Object> getDetail(HttpServletRequest request){
        Map<String,Object> beltMap = new HashMap<>();
        try {
            String belt_name = request.getParameter("belt_name");
            //实时查询皮带信息
            beltMap = lbssWeightService.webFindRealWeightInfo(belt_name);
        }catch (Exception e){
            logger.error("getDetail,{}",e);
        }

		
		return beltMap;
	}
    
}
