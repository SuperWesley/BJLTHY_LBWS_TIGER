package com.bjlthy.lbss.data.compare.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据对比对象 lbss_datacompare
 * 
 * @author zhangning
 * @date 2022-05-03
 */
public class LbssDatacompare extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;


    /** 时间 */
    @Excel(name = "时间")
    private String day;
    
    
    /** 工作面长度 */
    @Excel(name = "工作面长度")
    private Double length;
    
    /** 总采高 */
    @Excel(name = "总采高")
    private Double sumHeight;
    
    
    /** 采高 */
    @Excel(name = "采高")
    private Double height;
    
    /** 每刀进尺 */
    @Excel(name = "每刀进尺")
    private Double footage;

    /** 切割刀数 */
    @Excel(name = "切割刀数")
    private Double cutNum;
    
    /** 采出率 */
    @Excel(name = "采出率")
    private Double recovery_ratio;
    
    /** 放煤率*/
    @Excel(name = "放煤率")
    private Double coal_ratio;
    
    /** 偏帮和后溜的煤量 */
    @Excel(name = "偏帮和后溜的煤量")
    private Double afterCoalWeight;

    /** 平均密度 */
    @Excel(name = "平均密度")
    private Double density;

    /** 水分 */
    @Excel(name = "水分")
    private Double moisture;

    /** 灰分量 */
    @Excel(name = "灰分量")
    private Double ashContent;

    /** 矿总重量 */
    @Excel(name = "矿总重量")
    private Double kuangSumweight;

    /** 皮带秤总重量 */
    @Excel(name = "皮带秤总重量")
    private Double beltSumweight;

    /** 皮带秤总体积 */
    @Excel(name = "皮带秤总体积")
    private Double beltSumvolume;

    /** 皮带秤密度 */
    @Excel(name = "皮带秤密度")
    private Double beltDensity;

    /** 雷达计算总重量 */
    @Excel(name = "雷达计算总重量")
    private Double lidarSumweight;
    
    /** 相差值 */
    @Excel(name = "相差值")
    private Double difference;

    /** 相差比例 */
    @Excel(name = "相差比例")
    private Double differenceRatio;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    
    public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public void setFootage(Double footage)
    {
        this.footage = footage;
    }

    public Double getFootage()
    {
        return footage;
    }
    public void setLength(Double length)
    {
        this.length = length;
    }

    public Double getLength()
    {
        return length;
    }
    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getHeight()
    {
        return height;
    }
    public void setCutNum(Double cutNum)
    {
        this.cutNum = cutNum;
    }

    public Double getCutNum()
    {
        return cutNum;
    }
    public void setAfterCoalWeight(Double afterCoalWeight)
    {
        this.afterCoalWeight = afterCoalWeight;
    }

    public Double getAfterCoalWeight()
    {
        return afterCoalWeight;
    }
    public void setDensity(Double density)
    {
        this.density = density;
    }

    public Double getDensity()
    {
        return density;
    }
    public void setMoisture(Double moisture)
    {
        this.moisture = moisture;
    }

    public Double getMoisture()
    {
        return moisture;
    }
    public void setAshContent(Double ashContent)
    {
        this.ashContent = ashContent;
    }

    public Double getAshContent()
    {
        return ashContent;
    }
    public void setKuangSumweight(Double kuangSumweight)
    {
        this.kuangSumweight = kuangSumweight;
    }

    public Double getKuangSumweight()
    {
        return kuangSumweight;
    }
    public void setBeltSumweight(Double beltSumweight)
    {
        this.beltSumweight = beltSumweight;
    }

    public Double getBeltSumweight()
    {
        return beltSumweight;
    }
    public void setBeltSumvolume(Double beltSumvolume)
    {
        this.beltSumvolume = beltSumvolume;
    }

    public Double getBeltSumvolume()
    {
        return beltSumvolume;
    }
    public void setBeltDensity(Double beltDensity)
    {
        this.beltDensity = beltDensity;
    }

    public Double getBeltDensity()
    {
        return beltDensity;
    }
    public void setDifference(Double difference)
    {
        this.difference = difference;
    }

    public Double getDifference()
    {
        return difference;
    }
    public void setDifferenceRatio(Double differenceRatio)
    {
        this.differenceRatio = differenceRatio;
    }

    public Double getDifferenceRatio()
    {
        return differenceRatio;
    }
    
    
    public Double getLidarSumweight() {
		return lidarSumweight;
	}

	public void setLidarSumweight(Double lidarSumweight) {
		this.lidarSumweight = lidarSumweight;
	}

	public Double getRecovery_ratio() {
		return recovery_ratio;
	}

	public void setRecovery_ratio(Double recovery_ratio) {
		this.recovery_ratio = recovery_ratio;
	}

	public Double getCoal_ratio() {
		return coal_ratio;
	}

	public void setCoal_ratio(Double coal_ratio) {
		this.coal_ratio = coal_ratio;
	}

	public Double getSumHeight() {
		return sumHeight;
	}

	public void setSumHeight(Double sumHeight) {
		this.sumHeight = sumHeight;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("day", getDay())
            .append("footage", getFootage())
            .append("length", getLength())
            .append("sumHeight", getSumHeight())
            .append("height", getHeight())
            .append("cutNum", getCutNum())
            .append("recovery_ratio", getRecovery_ratio())
            .append("coal_ratio", getCoal_ratio())
            .append("afterCoalWeight", getAfterCoalWeight())
            .append("density", getDensity())
            .append("moisture", getMoisture())
            .append("ashContent", getAshContent())
            .append("kuangSumweight", getKuangSumweight())
            .append("beltSumweight", getBeltSumweight())
            .append("beltSumvolume", getBeltSumvolume())
            .append("beltDensity", getBeltDensity())
            .append("lidarSumweight", getLidarSumweight())
            .append("difference", getDifference())
            .append("differenceRatio", getDifferenceRatio())
            .append("remark", getRemark())
            .toString();
    }
}
