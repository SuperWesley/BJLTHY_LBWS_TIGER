package com.bjlthy.lbss.data.weight.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;

/**
 * 实时重量对象 coal_weight
 * 
 * @author zn
 * @date 2020-11-10
 */
public class LbssWeightDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 时间 */
    @Excel(name = "日期")
    private String day;

    /** 瞬时体积 */
    private Double volume = 0.0;
    
    /** 零点班重量 */
    @Excel(name = "零点班重量(t)")
    private Double nig_weight  = 0.0;
    
    /** 八点班重量 */
    @Excel(name = "八点班重量(t)")
    private Double mor_weight = 0.0;

    /** 四点班重量 */
    @Excel(name = "四点班重量(t)")
    private Double aft_weight = 0.0;
    
	/** 累计重量 */
	@Excel(name = "总过煤重量(t)")
	private Double totalWeight = 0.0;

    /** 累计体积 */
	@Excel(name = "总过煤体积(m3)")
    private Double totalVolume = 0.0;

    /** 瞬时重量 */
    private Double weight = 0.0;

    /**  密度*/
    @Excel(name = "松散密度(t/m3)")
    private Double density = 0.0;

    /** 含矸率 */
    @Excel(name = "含矸率(%)")
    private Double gangueRatio = 0.0;

    /** 速度 */
    private Double speed = 0.0;
    
    /** 小时重量 */
    private Double hourWeight = 0.0;
    
    /** 小时体积 */
    private Double hourVolume = 0.0;
    
    /** 工作面*/
    private String working_area;

	/** ip */
	private String ip;

	/** 皮带秤*/
	private String belt_name;
    /** 日产量*/
    private Double day_weight = 0.0;
    /** 月产量*/
    private Double month_weight = 0.0;
	/** 年产量*/
	private Double year_weight = 0.0;
	/** 煤仓1煤量*/
	private Double mc1_weight = 0.0;
	/** 煤仓2煤量*/
	private Double mc2_weight = 0.0;

    /** 瞬时产量集合*/
    private String weightList;
    /** 时间集合*/
    private String timeList;
	/** 设备状态*/
	private String status;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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


	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
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

	public Double getHourWeight() {
		return hourWeight;
	}

	public void setHourWeight(Double hourWeight) {
		this.hourWeight = hourWeight;
	}

	public Double getHourVolume() {
		return hourVolume;
	}

	public void setHourVolume(Double hourVolume) {
		this.hourVolume = hourVolume;
	}

	public String getWorking_area() {
		return working_area;
	}

	public void setWorking_area(String working_area) {
		this.working_area = working_area;
	}

	public String getBelt_name() {
		return belt_name;
	}

	public void setBelt_name(String belt_name) {
		this.belt_name = belt_name;
	}


	public Double getDay_weight() {
		return day_weight;
	}

	public void setDay_weight(Double day_weight) {
		this.day_weight = day_weight;
	}

	public Double getMonth_weight() {
		return month_weight;
	}

	public void setMonth_weight(Double month_weight) {
		this.month_weight = month_weight;
	}

	public Double getYear_weight() {
		return year_weight;
	}

	public void setYear_weight(Double year_weight) {
		this.year_weight = year_weight;
	}

	
	public String getWeightList() {
		return weightList;
	}

	public void setWeightList(String weightList) {
		this.weightList = weightList;
	}

	public String getTimeList() {
		return timeList;
	}

	public void setTimeList(String timeList) {
		this.timeList = timeList;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Double getMc1_weight() {
		return mc1_weight;
	}

	public void setMc1_weight(Double mc1_weight) {
		this.mc1_weight = mc1_weight;
	}

	public Double getMc2_weight() {
		return mc2_weight;
	}

	public void setMc2_weight(Double mc2_weight) {
		this.mc2_weight = mc2_weight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LbssWeightDTO [id=" + id + ", day=" + day + ", volume=" + volume + ", nig_weight=" + nig_weight
				+ ", mor_weight=" + mor_weight + ", aft_weight=" + aft_weight + ", totalWeight=" + totalWeight
				+ ", totalVolume=" + totalVolume + ", weight=" + weight + ", density=" + density + ", gangueRatio="
				+ gangueRatio + ", speed=" + speed + ", hourWeight=" + hourWeight + ", hourVolume=" + hourVolume
				+ ", belt_name=" + belt_name + ", day_weight=" + day_weight + ", month_weight=" + month_weight
				+ ", year_weight=" + year_weight + ", weightList=" + weightList + ", timeList=" + timeList + "]";
	}

	public LbssWeightDTO() {
		super();
	}

	public LbssWeightDTO(Integer id, String day, Double volume, Double nig_weight, Double mor_weight, Double aft_weight,
			Double totalWeight, Double totalVolume, Double weight, Double density, Double gangueRatio, Double speed,
			Double hourWeight, Double hourVolume, String belt_name, Double day_weight, Double month_weight,
			Double year_weight, String weightList, String timeList) {
		super();
		this.id = id;
		this.day = day;
		this.volume = volume;
		this.nig_weight = nig_weight;
		this.mor_weight = mor_weight;
		this.aft_weight = aft_weight;
		this.totalWeight = totalWeight;
		this.totalVolume = totalVolume;
		this.weight = weight;
		this.density = density;
		this.gangueRatio = gangueRatio;
		this.speed = speed;
		this.hourWeight = hourWeight;
		this.hourVolume = hourVolume;
		this.belt_name = belt_name;
		this.day_weight = day_weight;
		this.month_weight = month_weight;
		this.year_weight = year_weight;
		this.weightList = weightList;
		this.timeList = timeList;
	}


	public LbssWeightDTO(String ip,String working_area,String belt_name,Double nig_weight, Double mor_weight, Double aft_weight,Double weight, Double hourWeight, Double speed,Double density, Double gangueRatio,
						   Double day_weight, Double month_weight,Double year_weight,String status) {
		super();
		this.ip = ip;
		this.working_area = working_area;
		this.belt_name = belt_name;
		this.nig_weight = nig_weight;
		this.mor_weight = mor_weight;
		this.aft_weight = aft_weight;
		this.weight = weight;
		this.hourWeight = hourWeight;
		this.speed = speed;
		this.density = density;
		this.gangueRatio = gangueRatio;
		this.day_weight = day_weight;
		this.month_weight = month_weight;
		this.year_weight = year_weight;
		this.status = status;
	}

}
