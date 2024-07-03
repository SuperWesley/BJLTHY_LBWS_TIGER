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
public class LbssWorking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Integer id;

    /** 工作面编码 */
    @Excel(name = "工作面编码")
    private String working_area;

    /** 皮带秤名称 */
    @Excel(name = "皮带秤名称")
    private String belt_name;

    /** 皮带秤名称简写 */
    @Excel(name = "皮带秤名称简写")
    private String belt_name_en;

    /** 工作面地址 */
    @Excel(name = "工作面地址")
    private String belt_ip;
    
    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
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

	public void setBelt_name_en(String belt_name_en) {
		this.belt_name_en = belt_name_en;
	}

    public String getBelt_name_en() {
        return belt_name_en;
    }

    public void setBelt_name(String belt_name) {
        this.belt_name = belt_name;
    }

	public String getBelt_ip() {
		return belt_ip;
	}

	public void setBelt_ip(String belt_ip) {
		this.belt_ip = belt_ip;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("working_area", getWorking_area())
            .append("belt_name", getBelt_name())
            .append("belt_name_en", getBelt_name_en())
            .append("belt_ip", getBelt_ip())
            .toString();
    }
}
