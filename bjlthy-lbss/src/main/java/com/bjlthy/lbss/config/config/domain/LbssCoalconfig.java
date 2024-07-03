package com.bjlthy.lbss.config.config.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 煤量数据对比配置对象 lbss_coalconfig
 * 
 * @author zhangning
 * @date 2022-08-10
 */
public class LbssCoalconfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 工作面长度 */
    @Excel(name = "工作面长度")
    private Double length;

    /** 总采高 */
    @Excel(name = "总采高")
    private Double sumHeight;

    /** 采高 */
    @Excel(name = "采高")
    private Double height;

    /** 一刀进尺 */
    @Excel(name = "一刀进尺")
    private Double footage;
    
    /** 密度 */
    @Excel(name = "密度")
    private Double density;
    
    /** 采出率 */
    @Excel(name = "采出率")
    private Double recoveryRatio;

    /** 放煤率 */
    @Excel(name = "放煤率")
    private Double coalRatio;

    /** 水分比例 */
    @Excel(name = "水分比例")
    private Double moisture;

    /** 灰分比例 */
    @Excel(name = "灰分比例")
    private Double ashContent;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setLength(Double length)
    {
        this.length = length;
    }

    public Double getLength()
    {
        return length;
    }
    public void setSumHeight(Double sumHeight)
    {
        this.sumHeight = sumHeight;
    }

    public Double getSumHeight()
    {
        return sumHeight;
    }
    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getHeight()
    {
        return height;
    }
    public void setFootage(Double footage)
    {
        this.footage = footage;
    }

    public Double getFootage()
    {
        return footage;
    }
    public void setRecoveryRatio(Double recoveryRatio)
    {
        this.recoveryRatio = recoveryRatio;
    }

    public Double getRecoveryRatio()
    {
        return recoveryRatio;
    }
    public void setCoalRatio(Double coalRatio)
    {
        this.coalRatio = coalRatio;
    }

    public Double getCoalRatio()
    {
        return coalRatio;
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

    
    public Double getDensity() {
		return density;
	}

	public void setDensity(Double density) {
		this.density = density;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("length", getLength())
            .append("sumHeight", getSumHeight())
            .append("height", getHeight())
            .append("footage", getFootage())
            .append("recoveryRatio", getRecoveryRatio())
            .append("coalRatio", getCoalRatio())
            .append("moisture", getMoisture())
            .append("ashContent", getAshContent())
            .append("density", getDensity())
            .toString();
    }
}
