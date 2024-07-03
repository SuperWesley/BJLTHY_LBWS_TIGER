package com.bjlthy.lbss.data.position.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 综采编码器数据对象 lbss_position
 * 
 * @author zhangning
 * @date 2023-12-15
 */
public class LbssPosition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 皮带名称 */
    @Excel(name = "皮带名称")
    private String belt_name;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 架号 */
    @Excel(name = "架号")
    private Integer holder;

    /** x坐标 */
    @Excel(name = "x坐标")
    private Double xAxis;

    /** 切割刀数 */
    @Excel(name = "切割刀数")
    private Integer cutnum;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setTime(Date time)
    {
        this.time = time;
    }

    public Date getTime()
    {
        return time;
    }
    public void setHolder(Integer holder)
    {
        this.holder = holder;
    }

    public Integer getHolder()
    {
        return holder;
    }
    public void setxAxis(Double xAxis)
    {
        this.xAxis = xAxis;
    }

    public Double getxAxis()
    {
        return xAxis;
    }
    public void setCutnum(Integer cutnum)
    {
        this.cutnum = cutnum;
    }

    public Integer getCutnum()
    {
        return cutnum;
    }

    public String getBelt_name() {
        return belt_name;
    }

    public void setBelt_name(String belt_name) {
        this.belt_name = belt_name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("belt_name", getBelt_name())
            .append("time", getTime())
            .append("holder", getHolder())
            .append("xAxis", getxAxis())
            .append("cutnum", getCutnum())
            .toString();
    }
}
