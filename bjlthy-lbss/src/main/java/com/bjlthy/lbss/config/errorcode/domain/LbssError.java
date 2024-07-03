package com.bjlthy.lbss.config.errorcode.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 上位机界面异常表对象 coal_error
 * 
 * @author 张宁
 * @date 2021-03-01
 */
public class LbssError extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /**  */
    @Excel(name = "")
    private String name;

    /**  */
    @Excel(name = "")
    private String code;

    /**  */
    @Excel(name = "")
    private String type;

    /**  */
    @Excel(name = "")
    private String tips;

    /**  */
    @Excel(name = "")
    private String description;

    /**  */
    @Excel(name = "")
    private String solution;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setTips(String tips)
    {
        this.tips = tips;
    }

    public String getTips()
    {
        return tips;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setSolution(String solution)
    {
        this.solution = solution;
    }

    public String getSolution()
    {
        return solution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("code", getCode())
            .append("type", getType())
            .append("tips", getTips())
            .append("description", getDescription())
            .append("solution", getSolution())
            .toString();
    }
}
