package com.bjlthy.lbss.data.sumweight.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;

/**
 * 日累计重量对象 coal_sumweight
 * 
 * @author zn
 * @date 2020-11-10
 */
public class LbssDetailData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 时间 */
    @Excel(name = "日期")
    private String day;

	/** 总过煤重量 */
	@Excel(name = "总过煤重量(t)")
	private Double totalWeight;

    /** 总过煤体积 */
    @Excel(name = "总过煤体积(m3)")
    private Double totalVolume;

    /** 密度 */
    @Excel(name = "密度(t/m3)")
    private Double density;

    /** 含矸率 */
    @Excel(name = "含矸率(%)")
    private Double gangueRatio;
    
    /** 星期 */
    @Excel(name = "星期")
    private String week;

    /** 标志*/
    private String flag;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public LbssDetailData() {
		super();
	}

	
	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}



	@Override
	public String toString() {
		return "LbssDetailData [id=" + id + ", day=" + day + ", totalWeight=" + totalWeight + ", totalVolume="
				+ totalVolume + ", density=" + density + ", gangueRatio=" + gangueRatio + ", week=" + week + ", flag="
				+ flag + "]";
	}


	public LbssDetailData(Integer id, String day, Double totalWeight, Double totalVolume, Double density,
			Double gangueRatio, String week, String flag) {
		super();
		this.id = id;
		this.day = day;
		this.totalWeight = totalWeight;
		this.totalVolume = totalVolume;
		this.density = density;
		this.gangueRatio = gangueRatio;
		this.week = week;
		this.flag = flag;
	}

	
}
