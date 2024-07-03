package com.bjlthy.lbss.data.sumweight.controller;
import com.bjlthy.common.core.controller.BaseController;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightHourService;
import com.bjlthy.lbss.tool.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 数据统计查询Controller
 * @date 2020年11月28日 下午9:23:32 @copyright(c) 北京龙田华远科技有限公司
 *
 */
@Controller
@RequestMapping("/lbss/data/sumweightHour")
public class LbssSumweightHourController extends BaseController {
	private String prefix = "mapper/data/sumweight";

	@Autowired
	private ILbssSumWeightHourService lbssSumWeightService;

	/**
	 * @author: zhangning
	 * @description 查询皮带秤日统计信息
	 * @throws Exception
	 */
	@PostMapping("/getShiftData")
	@ResponseBody
	public Map<String, Object> getShiftData(HttpServletRequest request) throws Exception {
		//获取时间
        String startTime = request.getParameter("startTime");
        //获取下一天日期
        String endTime = DateHelper.getNextDay(startTime, 1);
		//获取皮带秤名称
		String belt_name = request.getParameter("belt_name");
		// 查询皮带信息
		Map<String, Object> beltMap = lbssSumWeightService.queryShiftWeightInfo(startTime, endTime,belt_name);
		
		beltMap.put("day", startTime);
		//返回结果
		return beltMap;
		
	}
}
