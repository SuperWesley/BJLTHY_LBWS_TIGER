package com.bjlthy.lbss.data.weight.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 实时重量对象 coal_weight
 * 
 * @author zn
 * @date 2020-11-10
 */
public class LbssWeight extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 时间 */
    @Excel(name = "时间")
    private Date time;
	@Excel(name = "皮带秤")
	private String belt_name;

    /** 瞬时体积 */
    @Excel(name = "瞬时体积")
    private Double volume;

    /** 累计体积 */
    @Excel(name = "累计体积")
    private Double totalVolume;

    /** 瞬时重量 */
    @Excel(name = "瞬时重量")
    private Double weight;

    /** 累计重量 */
    @Excel(name = "累计重量")
    private Double totalWeight;

    /**  密度*/
    @Excel(name = "密度")
    private Double density;

    /** 含矸率 */
    @Excel(name = "含矸率")
    private Double gangueRatio;

    /** 速度 */
    @Excel(name = "速度")
    private Double speed;

    /** 早班产量 */
    @Excel(name = "早班产量")
    private Double mor_weight;

    /** 中班产量 */
    @Excel(name = "中班产量")
    private Double aft_weight;

    /** 晚班产量 */
    @Excel(name = "晚班产量")
    private Double nig_weight;
	/** 备注 */
	@Excel(name = "备注")
	private String remark;
	/** 最大时间*/
	private Date minTime;
	/** 最小时间*/
	private Date maxTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getBelt_name() {
		return belt_name;
	}

	public void setBelt_name(String belt_name) {
		this.belt_name = belt_name;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
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

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getMor_weight() {
		return mor_weight;
	}

	public void setMor_weight(Double mor_weight) {
		this.mor_weight = mor_weight;
	}

	public Double getAft_weight() {
		return aft_weight;
	}

	public void setAft_weight(Double aft_weight) {
		this.aft_weight = aft_weight;
	}

	public Double getNig_weight() {
		return nig_weight;
	}

	public void setNig_weight(Double nig_weight) {
		this.nig_weight = nig_weight;
	}

	public Date getMinTime() {
		return minTime;
	}

	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "CoalWeight [id=" + id + ", time=" + time + ", volume=" + volume + ", totalVolume=" + totalVolume
				+ ", weight=" + weight + ", totalWeight=" + totalWeight + ", density=" + density + ", gangueRatio="
				+ gangueRatio + ", speed=" + speed + ", mor_weight=" + mor_weight + ", aft_weight=" + aft_weight
				+ ", nig_weight=" + nig_weight + "]";
	}
	
	public LbssWeight() {
		super();
	}

	public LbssWeight(Integer id, Date time, String belt_name, Double volume, Double totalVolume, Double weight, Double totalWeight, Double density, Double gangueRatio, Double speed, Double mor_weight, Double aft_weight, Double nig_weight, String remark) {
		this.id = id;
		this.time = time;
		this.belt_name = belt_name;
		this.volume = volume;
		this.totalVolume = totalVolume;
		this.weight = weight;
		this.totalWeight = totalWeight;
		this.density = density;
		this.gangueRatio = gangueRatio;
		this.speed = speed;
		this.mor_weight = mor_weight;
		this.aft_weight = aft_weight;
		this.nig_weight = nig_weight;
		this.remark = remark;
	}
}
