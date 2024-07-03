package com.bjlthy.lbss.config.config.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 其他配置对象 coal_otherconfig
 * 
 * @author 张宁
 * @date 2021-01-03
 */
public class LbssARMConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Integer id;

    /** 皮带秤名称 */
    @Excel(name = "皮带秤名称")
    private String belt_name;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String name;

    /** 参数值 */
    @Excel(name = "参数值")
    private String value;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

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

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("belt_name", getBelt_name())
            .append("code", getCode())
            .append("name", getName())
            .append("value", getValue())
            .append("unit", getUnit())
            .append("type", getType())
            .append("status",getStatus())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
