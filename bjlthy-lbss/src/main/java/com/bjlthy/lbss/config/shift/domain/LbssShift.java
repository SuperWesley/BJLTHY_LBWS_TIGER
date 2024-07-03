package com.bjlthy.lbss.config.shift.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 班次对象 coal_shift
 * @date 2021年2月6日 上午10:38:55
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class LbssShift extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 班次 */
    @Excel(name = "班次")
    private String name;

    /** 编码 */
    @Excel(name = "编码")
    private String code;
    
    /** 开始时间 */
    @Excel(name = "开始时间")
    private String begin;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String end;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;
    
    /** 是否显示标识 */
    @Excel(name = "是否显示标识")
    private String status;
    
    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
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
    public void setBegin(String begin)
    {
        this.begin = begin;
    }

    public String getBegin()
    {
        return begin;
    }
    public void setEnd(String end)
    {
        this.end = end;
    }

    public String getEnd()
    {
        return end;
    }

    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("code", getCode())
            .append("begin", getBegin())
            .append("end", getEnd())
            .append("remark", getRemark())
            .append("status", getStatus())
            .toString();
    }
}
