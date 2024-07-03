package com.bjlthy.lbss.data.compare.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.bean.BeanUtils;
import com.bjlthy.lbss.config.config.domain.LbssCoalconfig;
import com.bjlthy.lbss.config.config.mapper.LbssCoalconfigMapper;
import com.bjlthy.lbss.data.compare.domain.LbssDatacompare;
import com.bjlthy.lbss.data.compare.mapper.LbssDatacompareMapper;
import com.bjlthy.lbss.data.compare.service.ILbssDatacompareService;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据对比Service业务层处理
 * 
 * @author zhangning
 * @date 2022-05-03
 */
@Service
public class LbssDatacompareServiceImpl implements ILbssDatacompareService
{
    @Autowired
    private LbssDatacompareMapper lbssDatacompareMapper;
    @Autowired
    private LbssSumWeightHourMapper lbssSumWeightMapper;
    @Autowired
    private LbssCoalconfigMapper lbssCoalconfigMapper;
    
    /**
     * 查询数据对比
     * 
     * @param id 数据对比ID
     * @return 数据对比
     */
    @Override
    public LbssDatacompare selectLbssDatacompareById(Integer id)
    {
        return lbssDatacompareMapper.selectLbssDatacompareById(id);
    }

    /**
     * 查询数据对比列表
     * 
     * @param lbssDatacompare 数据对比
     * @return 数据对比
     */
    @Override
    public List<LbssDatacompare> selectLbssDatacompareList(LbssDatacompare lbssDatacompare)
    {
        return lbssDatacompareMapper.selectLbssDatacompareList(lbssDatacompare);
    }

    /**
     * 新增数据对比
     * 
     * @param lbssDatacompare 数据对比
     * @return 结果
     */
    @Override
    public int insertLbssDatacompare(LbssDatacompare lbssDatacompare)
    {
        return lbssDatacompareMapper.insertLbssDatacompare(lbssDatacompare);
    }

    /**
     * 修改数据对比
     * 
     * @param lbssDatacompare 数据对比
     * @return 结果
     */
    @Override
    public int updateLbssDatacompare(LbssDatacompare lbssDatacompare)
    {
    	//查询煤量参数配置信息
    	LbssCoalconfig coalConfig = lbssCoalconfigMapper.selectLbssCoalconfigById(1);
    	//矿出煤量
    	Double kuangWeight = DataHelper.getKuangWeigh(lbssDatacompare);
    	//偏帮和后溜的煤量
    	Double houWeight = DataHelper.getAfterWeigh(lbssDatacompare);
    	lbssDatacompare.setAfterCoalWeight(houWeight);
    	//水分量 = 矿出煤量*水分比例
    	Double moisture = kuangWeight * coalConfig.getMoisture();
    	lbssDatacompare.setMoisture(moisture);
    	//灰分量 = 矿出煤量*灰分比例
    	Double ashContent = kuangWeight * coalConfig.getAshContent();
    	lbssDatacompare.setAshContent(ashContent);
    	//总产量 = 矿出煤量 + 偏帮和后溜的煤量 + 水分量 + 灰分量
    	Double sumWeight = kuangWeight + houWeight + moisture + ashContent;
    	lbssDatacompare.setKuangSumweight(sumWeight);
    	//相差值
    	Double difference = lbssDatacompare.getBeltSumweight() - sumWeight;
    	lbssDatacompare.setDifference(difference);
    	//相差比例
    	Double diffRatio = DataHelper.getDiffRatio(lbssDatacompare.getBeltSumweight(), sumWeight);
    	lbssDatacompare.setDifferenceRatio(diffRatio);
    	//数据分析
    	String msg = dataCompare(lbssDatacompare);
    	lbssDatacompare.setRemark(msg);
        return lbssDatacompareMapper.updateLbssDatacompare(lbssDatacompare);
    }
    
    /**
     * 
     * @author 张宁
     * @Description: 数据分析
     * @return String    返回类型
     * @date 2022年6月2日 上午11:28:37
     * @throws
     */
    private String dataCompare(LbssDatacompare data) {
		// TODO Auto-generated method stub
    	//相差比例
    	Double diffRatio = data.getDifferenceRatio();
    	Double beltDensity = data.getBeltDensity();
    	//计算雷达重量相差比例
    	Double lidar_ratio = DataHelper.getDiffRatio(data.getLidarSumweight(), data.getKuangSumweight());
    	String msg = "正常";
    	Double density_min = DictionariesHelper.getDicDoubleValueByCode("density_min");
		Double density_max = DictionariesHelper.getDicDoubleValueByCode("density_max");
    	//比较皮带秤产量和矿产量相差比例在10%以内，且密度超出边界值
    	if(Math.abs(diffRatio) > ConfigBean.percentage && (beltDensity < density_min||beltDensity > density_max)){
    		msg = "需要重新进行体积模型训练，更换新模型";
    	}else if(Math.abs(lidar_ratio) < ConfigBean.percentage && Math.abs(diffRatio) > ConfigBean.percentage){//如果体积*恒定密度和矿产量相差比例在10%以内，皮带秤产量和矿产量相差比例超过10%
    		msg = "皮带秤需要进行零点校准";
    	}
		return msg;
	}

	/**
     * 删除数据对比对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssDatacompareByIds(String ids)
    {
        return lbssDatacompareMapper.deleteLbssDatacompareByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据对比信息
     * 
     * @param id 数据对比ID
     * @return 结果
     */
    @Override
    public int deleteLbssDatacompareById(Integer id)
    {
        return lbssDatacompareMapper.deleteLbssDatacompareById(id);
    }
    
    /**
     * 
     * @author 张宁
     * @Description: 和矿方产量数据对比并比较
     * @return void    返回类型
     * @date 2022年6月1日 下午3:26:29
     * @throws
     */
    public void dataCompareAndSave(){
    	//查询煤量参数配置信息
    	LbssCoalconfig  coalConfig = lbssCoalconfigMapper.selectLbssCoalconfigById(1);
    	LbssDatacompare dataCompare = new LbssDatacompare();
    	BeanUtils.copyBeanProp(dataCompare, coalConfig);
    	//切割刀数默认为0
    	dataCompare.setCutNum(0.0);
    	dataCompare.setAfterCoalWeight(0.0);//偏帮和后溜煤量默认为0
    	dataCompare.setKuangSumweight(0.0);//矿总重量默认为0
    	//查询皮带秤日产量信息
    	String endTime = DateUtil.getDate();
    	String startTime = DateHelper.getNextDay(endTime, -1);
    	List<LbssSumWeightHour> list = lbssSumWeightMapper.queryDayWeightInfo(startTime, endTime);
    	LbssSumWeightHour sumWeight = new LbssSumWeightHour();
    	if(StringUtils.isNotEmpty(list)){
    		sumWeight = list.get(0);
    		Double beltDensity = DataHelper.div(sumWeight.getTotalWeight().doubleValue(), sumWeight.getTotalVolume().doubleValue(),2);
    		//数据存入数据对比表
    		dataCompare.setBeltSumweight(sumWeight.getTotalWeight().doubleValue());
    		dataCompare.setBeltSumvolume(sumWeight.getTotalVolume().doubleValue());
    		dataCompare.setBeltDensity(beltDensity*ConfigBean.songsan);
    		Double lidar_sumweight = DataHelper.mul(sumWeight.getTotalVolume().doubleValue(), coalConfig.getDensity());
    		dataCompare.setLidarSumweight(lidar_sumweight);
    	}
    	dataCompare.setDay(startTime);
    	dataCompare.setRemark("正常");
    	lbssDatacompareMapper.insertLbssDatacompare(dataCompare);
    }
}
