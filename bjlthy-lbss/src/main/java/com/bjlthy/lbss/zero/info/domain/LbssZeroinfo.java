package com.bjlthy.lbss.zero.info.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 调零信息对象 lbss_zeroinfo
 * 
 * @author zhangning
 * @date 2022-05-03
 */
public class LbssZeroinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 皮带秤名称 */
    @Excel(name = "皮带秤名称")
    private String belt_name;

    /** 记录时间 */
    @Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String time;

    /** 调零次数 */
    @Excel(name = "调零次数")
    private Integer zeronum;

    /** 调零结果 */
    @Excel(name = "调零结果")
    private String status;

    /** 调零结果 */
    @Excel(name = "结果说明")
    private String remark = "";

    /** 零点值 */
    @Excel(name = "零点值")
    private String zeroValue;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setBelt_name(String belt_name)
    {
        this.belt_name = belt_name;
    }

    public String getBelt_name()
    {
        return belt_name;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return time;
    }
    public void setZeronum(Integer zeronum)
    {
        this.zeronum = zeronum;
    }

    public Integer getZeronum()
    {
        return zeronum;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getZeroValue() {
        return zeroValue;
    }

    public void setZeroValue(String zeroValue) {
        this.zeroValue = zeroValue;
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
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("belt_name", getBelt_name())
            .append("time", getTime())
            .append("zeronum", getZeronum())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }

    public LbssZeroinfo() {
    }

    public LbssZeroinfo(Integer id, String belt_name, String time, Integer zeronum, String status) {
        this.id = id;
        this.belt_name = belt_name;
        this.time = time;
        this.zeronum = zeronum;
        this.status = status;
    }
}
