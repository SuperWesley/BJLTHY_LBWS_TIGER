package com.bjlthy.lbss.config.config.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 开关控制对象 lbss_switchconfig
 * 
 * @author zhangning
 * @date 2022-08-10
 */
public class LbssSysconfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 皮带秤名称 */
    @Excel(name = "皮带秤名称")
    private String belt_name;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String name;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 参数值 */
    @Excel(name = "参数值")
    private String value;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String status;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("belt_name", getBelt_name())
            .append("name", getName())
            .append("code", getCode())
            .append("value", getValue())
            .append("type", getType())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
