package com.bjlthy.lbss.data.sumweight.controller;

import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.core.page.TableDataInfo;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.poi.ExcelUtil;
import com.bjlthy.framework.config.RuoYiConfig;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightDayService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/lbss/data/sumweightDay")
public class LbssSumWeightDayController extends BaseController {

	private String prefix = "mapper/data/sumweight";

	@Autowired
	private ILbssSumWeightDayService lbssSumWeightDayService;

	@GetMapping()
	public String sumweightDay()
	{
		return prefix + "/sumweightday";
	}

	/**
	 * 查询日累计重量列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LbssSumWeightDay lbssSumweightDay)
	{
		startPage();
		List<LbssSumWeightDay> list = lbssSumWeightDayService.selectLbssSumweightDayList(lbssSumweightDay);
		return getDataTable(list);
	}

	/**
	 * @author: zhangning
	 * @description 查询皮带秤周统计
	 * @throws Exception
	 */
	@RequestMapping("/getWeekData")
	@ResponseBody
	public Map<String, Object> getWeekData(HttpServletRequest request) throws Exception {

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		//获取皮带秤名称
		String belt_name = request.getParameter("belt_name");
		// 获取皮带机信息
		Map<String, Object> beltMap = lbssSumWeightDayService.queryDayWeightInfo(startTime, endTime,belt_name);

		beltMap.put("day", startTime + " \r\n至\r\n "+ endTime);
		return beltMap;
	}

	/**
	 * @author: zhangning
	 * @description 查询皮带秤月统计
	 * @throws Exception
	 */
	@RequestMapping("/getMonthData")
	@ResponseBody
	public Map<String, Object> getMonthData(HttpServletRequest request) throws Exception {
		//获取开始时间
		String startTime = request.getParameter("startTime");
		//获取结束时间
		String endTime = request.getParameter("endTime");
		//获取皮带秤名称
		String belt_name = request.getParameter("belt_name");
		// 获取每天的煤量信息
		Map<String, Object> beltMap = lbssSumWeightDayService.queryMonthWeightInfo(startTime, endTime,belt_name);

		beltMap.put("day", startTime + " \r\n至\r\n " + endTime);
		return beltMap;
	}

	/**
	 * @author: zhangning
	 * @description 查询皮带秤年统计
	 * @throws Exception
	 */
	@PostMapping("/getYearData")
	@ResponseBody
	public Map<String, Object> getYearData(HttpServletRequest request) throws Exception {
		//获取开始时间
		String startTime = request.getParameter("startTime");
		//获取结束时间
		String endTime = request.getParameter("endTime");
		//获取皮带秤名称
		String belt_name = request.getParameter("belt_name");
		// 获取每天的煤量信息
		Map<String, Object> beltMap = lbssSumWeightDayService.queryYearWeightInfo(startTime, endTime,belt_name);

		beltMap.put("day", startTime + " \r\n至\r\n " + endTime);
		return beltMap;
	}

	/**
	 * @author: zhangning
	 * @description 详细统计查询
	 * @throws Exception
	 */
	@PostMapping("/getDetailData")
	@ResponseBody
	public TableDataInfo getDetailData(LbssSumWeightDay lbssSumWeight,Integer pageNum,Integer pageSize) throws Exception {
		
		List<LbssSumWeightDay> detailDataList = new ArrayList<LbssSumWeightDay>();
		String startTime = (String) lbssSumWeight.getParams().get("startTime");
		String endTime = (String) lbssSumWeight.getParams().get("endTime");

		//查询时间为空，不进行查询
		if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
			//获取查询状态
			lbssSumWeight.setStartTime(startTime);
			lbssSumWeight.setEndTime(endTime);
			detailDataList = lbssSumWeightDayService.queryDetailData(lbssSumWeight);
		}

		int num = detailDataList.size();
		detailDataList = detailDataList.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(detailDataList);
		rspData.setTotal(num);
		return rspData;
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
		List<LbssSumWeightDay> list = lbssSumWeightDayService.queryDetailData(lbssSumWeightDay);
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
		List<LbssSumWeightDay> list = lbssSumWeightDayService.queryDetailData(lbssSumWeightDay);
		//添加合计信息
		List<LbssSumWeightDay> detailList = lbssSumWeightDayService.totalListInfo(list);
        ExcelUtil<LbssSumWeightDay> util = new ExcelUtil<LbssSumWeightDay>(LbssSumWeightDay.class);
        return util.exportCoalExcel(detailList, belt_name+"统计信息","详细");
    }
   

	/**
	 * 
	 * @author 张宁
	 * @description 下载Excel文件
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
}
