package com.bjlthy.lbss.config.config.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 标签配置对象 lbss_labelconfig
 * 
 * @author zhangning
 * @date 2024-01-22
 */
public class LbssLabelconfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 计算周期，单位秒 */
    @Excel(name = "计算周期，单位秒")
    private Integer timeCycle;

    /** 计算周期最大值 */
    @Excel(name = "计算周期最大值")
    private Integer timeCycleMax;

    /** 周期步长 */
    @Excel(name = "周期步长")
    private Integer timeCycleStep;

    /** AD频率 */
    @Excel(name = "AD频率")
    private Integer adFrequency;

    /** 运动与静止状态数据时间限制，单位分 */
    @Excel(name = "运动与静止状态数据时间限制，单位分")
    private Integer timeCnt;

    /** 系数 */
    @Excel(name = "系数")
    private Double coeff;

    /** AD标准差系数最小值 */
    @Excel(name = "AD标准差系数最小值")
    private Double adCoeffStdMin;

    /** 阈值 */
    @Excel(name = "阈值")
    private Double threshold;

    /** 皮带秤ad路数 */
    @Excel(name = "皮带秤ad路数")
    private String adCount;

    /** 阈值准确度 */
    @Excel(name = "阈值准确度")
    private Double accuracy;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setTimeCycle(Integer timeCycle)
    {
        this.timeCycle = timeCycle;
    }

    public Integer getTimeCycle()
    {
        return timeCycle;
    }
    public void setTimeCycleMax(Integer timeCycleMax)
    {
        this.timeCycleMax = timeCycleMax;
    }

    public Integer getTimeCycleMax()
    {
        return timeCycleMax;
    }
    public void setTimeCycleStep(Integer timeCycleStep)
    {
        this.timeCycleStep = timeCycleStep;
    }

    public Integer getTimeCycleStep()
    {
        return timeCycleStep;
    }
    public void setAdFrequency(Integer adFrequency)
    {
        this.adFrequency = adFrequency;
    }

    public Integer getAdFrequency()
    {
        return adFrequency;
    }
    public void setTimeCnt(Integer timeCnt)
    {
        this.timeCnt = timeCnt;
    }

    public Integer getTimeCnt()
    {
        return timeCnt;
    }
    public void setCoeff(Double coeff)
    {
        this.coeff = coeff;
    }

    public Double getCoeff()
    {
        return coeff;
    }
    public void setAdCoeffStdMin(Double adCoeffStdMin)
    {
        this.adCoeffStdMin = adCoeffStdMin;
    }

    public Double getAdCoeffStdMin()
    {
        return adCoeffStdMin;
    }
    public void setThreshold(Double threshold)
    {
        this.threshold = threshold;
    }

    public Double getThreshold()
    {
        return threshold;
    }
    public void setAdCount(String adCount)
    {
        this.adCount = adCount;
    }

    public String getAdCount()
    {
        return adCount;
    }
    public void setAccuracy(Double accuracy)
    {
        this.accuracy = accuracy;
    }

    public Double getAccuracy()
    {
        return accuracy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("timeCycle", getTimeCycle())
            .append("timeCycleMax", getTimeCycleMax())
            .append("timeCycleStep", getTimeCycleStep())
            .append("adFrequency", getAdFrequency())
            .append("timeCnt", getTimeCnt())
            .append("coeff", getCoeff())
            .append("adCoeffStdMin", getAdCoeffStdMin())
            .append("threshold", getThreshold())
            .append("adCount", getAdCount())
            .append("accuracy", getAccuracy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
