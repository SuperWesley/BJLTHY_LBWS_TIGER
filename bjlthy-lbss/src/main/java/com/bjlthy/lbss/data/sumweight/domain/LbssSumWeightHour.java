package com.bjlthy.lbss.data.sumweight.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;

/**
 * 日累计重量对象 coal_sumweight
 *
 * @author zn
 * @date 2020-11-10
 */
public class LbssSumWeightHour extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;

	/** 皮带秤名称 */
	@Excel(name = "皮带秤名称")
	private String belt_name;

	/** 时间 */
	@Excel(name = "时间")
	private String day;

	/** 总过煤重量 */
	@Excel(name = "总过煤重量(t)")
	private Double totalWeight=0.0;

	/** 总过煤体积 */
	@Excel(name = "总过煤体积(m3)")
	private Double totalVolume=0.0;

	/** 密度 */
	@Excel(name = "密度(t/m3)")
	private Double density;

	/** 含矸率 */
	@Excel(name = "含矸率(%)")
	private Double gangueRatio;

	/** 星期 */
	@Excel(name = "星期")
	private String week;

	/** 备注信息*/
	@Excel(name = "备注信息")
	private String remark;

	private String status;

	private Double sumWeight;

	private Double sumVolume;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBelt_name() {
		return belt_name;
	}

	public void setBelt_name(String belt_name) {
		this.belt_name = belt_name;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getDensity() {
		return density;
	}

	public void setDensity(Double density) {
		this.density = density;
	}

	public Double getGangueRatio() {
		return gangueRatio;
	}

	public void setGangueRatio(Double gangueRatio) {
		this.gangueRatio = gangueRatio;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getSumWeight() {
		return sumWeight;
	}

	public void setSumWeight(Double sumWeight) {
		this.sumWeight = sumWeight;
	}

	public Double getSumVolume() {
		return sumVolume;
	}

	public void setSumVolume(Double sumVolume) {
		this.sumVolume = sumVolume;
	}


	@Override
	public String toString() {
		return "LbssSumWeight [id=" + id + ", belt_name=" + belt_name + ", day=" + day + ", totalWeight="
				+ totalWeight + ", totalVolume=" + totalVolume + ", density=" + density + ", gangueRatio=" + gangueRatio
				+ ", week=" + week + ", remark=" + remark + ", status=" + status + ", sumWeight=" + sumWeight
				+ ", sumVolume=" + sumVolume + "]";
	}

	public LbssSumWeightHour(Integer id, String belt_name, String day, Double totalWeight, Double totalVolume, Double density,
							 Double gangueRatio, String week, String remark, String status, Double sumWeight, Double sumVolume) {
		super();
		this.id = id;
		this.belt_name = belt_name;
		this.day = day;
		this.totalWeight = totalWeight;
		this.totalVolume = totalVolume;
		this.density = density;
		this.gangueRatio = gangueRatio;
		this.week = week;
		this.remark = remark;
		this.status = status;
		this.sumWeight = sumWeight;
		this.sumVolume = sumVolume;
	}


	public LbssSumWeightHour() {
		super();
	}





}
