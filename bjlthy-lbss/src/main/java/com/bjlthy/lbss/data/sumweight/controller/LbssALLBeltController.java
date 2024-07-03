package com.bjlthy.lbss.data.sumweight.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.config.RuoYiConfig;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.service.ILbssALLBeltService;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightDayService;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightHourService;
import com.bjlthy.lbss.tool.DateHelper;
import com.bjlthy.lbss.tool.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 数据统计查询Controller
 * @date 2020年11月28日 下午9:23:32 
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
@Controller
@RequestMapping("/lbss/data/allBelt")
public class LbssALLBeltController extends BaseController {

	@Autowired
	private ILbssALLBeltService lbssALLBeltService;

	@Autowired
	private ILbssSumWeightHourService lbssSumWeightHourService;
	@Autowired
	private ILbssSumWeightDayService lbssSumWeightDayService;
	/**
	 * 详细统计管理界面
	 */
	@GetMapping("/showAreaDetail")
	public String showAreaDetail() {
		return "mapper/data/sumweight/showAreaDetail";
	}
	

	/**
	 * @author: zhangning
	 * @description 查询皮带日统计信息
	 * @throws Exception
	 */
	@PostMapping("/getDetail")
	@ResponseBody
	public Map<String, Object> getDetail(HttpServletRequest request) throws Exception {
		Map<String, Object> map = lbssALLBeltService.queryALLBeltWeightDetail();
		//查询各皮带的统计信息
		lbssALLBeltService.queryALLBeltWeight(map);

		return map;
		
	}


	/**
	 * @author: zhangning
	 * @description 详细统计查询
	 * @throws Exception
	 */
	@PostMapping("/getDetailData")
	@ResponseBody
	public TableDataInfo getDetailData(LbssSumWeightDay lbssSumWeight) throws Exception {
		
		startPage();
		List<LbssSumWeightDay> detailDataList = new ArrayList<LbssSumWeightDay>();
		String startTime = (String) lbssSumWeight.getParams().get("startTime");
		String endTime = (String) lbssSumWeight.getParams().get("endTime");
		//查询时间为空，不进行查询
		if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
			lbssSumWeight.setStartTime(startTime);
			lbssSumWeight.setEndTime(endTime);
			//获取查询状态
			detailDataList = lbssALLBeltService.queryDetailData(lbssSumWeight);
		}
		return getDataTable(detailDataList);
	}
	
	/**
	 * @author: zhangning
	 * @description 查询皮带秤汇总信息
	 * @throws Exception
	 */
	@PostMapping("/getTotalData")
	@ResponseBody
	public Map<String, String> getTotalData(HttpServletRequest request) throws Exception {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String status = request.getParameter("status");
		String belt_name = request.getParameter("belt_name");
		LbssSumWeightDay lbssSumWeightDay = new LbssSumWeightDay();
		lbssSumWeightDay.setStartTime(startTime);
		lbssSumWeightDay.setEndTime(endTime);
		lbssSumWeightDay.setStatus(status);
		lbssSumWeightDay.setBelt_name(belt_name);
		List<LbssSumWeightDay> list = lbssALLBeltService.queryDetailData(lbssSumWeightDay);
		//数据统计
		Double sumWeight = 0.00;
		Double sumVolume = 0.00;
		for(int i =0; i<list.size();i++){
			LbssSumWeightDay lbssSumWeight = list.get(i);
			sumWeight += lbssSumWeight.getTotalWeight();
			sumVolume += lbssSumWeight.getTotalVolume();
		}
		Map<String, String> beltMap = new HashMap<String, String>();
		beltMap.put("sumWeight", String.format("%.2f", sumWeight));
		beltMap.put("sumVolume", String.format("%.2f", sumVolume));
		return beltMap;
	}
	
	
	/**
     * 导出详细统计列表信息
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HttpServletRequest request)
    {
    	String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String status = request.getParameter("status");
		String belt_name = request.getParameter("belt_name");
		LbssSumWeightDay lbssSumWeightDay = new LbssSumWeightDay();
		lbssSumWeightDay.setStartTime(startTime);
		lbssSumWeightDay.setEndTime(endTime);
		lbssSumWeightDay.setStatus(status);
		lbssSumWeightDay.setBelt_name(belt_name);
		// 获取皮带信息
		List<LbssSumWeightDay> list = lbssALLBeltService.queryDetailData(lbssSumWeightDay);
		//添加合计信息
		List<LbssSumWeightDay> detailList = lbssALLBeltService.totalListInfo(list);
        ExcelUtil<LbssSumWeightDay> util = new ExcelUtil<LbssSumWeightDay>(LbssSumWeightDay.class);
        return util.exportCoalExcel(detailList, "统计信息","详细");
    }
   

	/**
	 * 
	 * @author 张宁
	 * @description 下载Excel文件
	 * @param 参数
	 * @date 2021年5月24日 下午9:59:29
	 */
	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		try {
			String fileName = request.getParameter("fileName");
			String savePath = RuoYiConfig.getDownloadPath() + fileName ;
			File file = new File(savePath);
			FileInputStream fis = new FileInputStream(file);
			response.setContentType("application/force-download");
			response.addHeader("Content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
			OutputStream os = response.getOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while((len = fis.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author: zhangning
	 * @description 查询皮带日统计信息
	 * @throws Exception
	 */
	@PostMapping("/getDayData")
	@ResponseBody
	public Map<String, Object> getDayData(HttpServletRequest request) throws Exception {
		//获取时间
		String startTime = DateUtil.getDate();
		//获取下一天日期
		String endTime = DateHelper.getNextDay(startTime, 1);
		// 查询皮带信息
		Map<String, Object> beltMap = lbssALLBeltService.queryShiftWeightInfo(startTime, endTime);

		//返回结果
		return beltMap;

	}

	/**
	 * @author: zhangning
	 * @description 查询皮带周统计
	 * @throws Exception
	 */
	@RequestMapping("/getWeekData")
	@ResponseBody
	public Map<String, Object> getWeekData(HttpServletRequest request) throws Exception {

		List<String> weeks = DateHelper.getWeekDays(DateUtil.getDate());
		String startTime = weeks.get(0);
		String endTime = weeks.get(weeks.size()-1);
		// 获取皮带机信息
		Map<String, Object> beltMap = lbssALLBeltService.queryDayWeightInfo(startTime, endTime);

		return beltMap;
	}

	/**
	 * @author: zhangning
	 * @description 查询皮带月统计
	 * @throws Exception
	 */
	@RequestMapping("/getMonthData")
	@ResponseBody
	public Map<String, Object> getMonthData(HttpServletRequest request) throws Exception {
		//获取开始时间
		String startTime = DateHelper.getFirstDayOfMonth(new Date());
		//获取结束时间
		String endTime = DateHelper.getNextDayOfMonth(new Date());
		// 获取每天的煤量信息
		Map<String, Object> beltMap = lbssALLBeltService.queryMonthWeightInfo(startTime, endTime);

		return beltMap;
	}

	/**
	 * @author: zhangning
	 * @description 查询皮带年统计
	 * @throws Exception
	 */
	@PostMapping("/getYearData")
	@ResponseBody
	public Map<String, Object> getYearData(HttpServletRequest request) throws Exception {
		//获取开始时间
		String startTime = DateUtil.getYear()+"-01-01 00:00:00";
		//获取结束时间
		String endTime = DateUtil.getYear()+"-12-31 23:59:59";

		// 获取每天的煤量信息
		Map<String, Object> beltMap = lbssALLBeltService.queryYearWeightInfo(startTime, endTime);

		return beltMap;
	}
}
