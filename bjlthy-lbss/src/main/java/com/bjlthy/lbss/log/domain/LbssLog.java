package com.bjlthy.lbss.log.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 日志信息对象 lbss_log
 * 
 * @author 张宁
 * @date 2021-08-09
 */
public class LbssLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 序号 */
    private Integer id;

    /** 时间 */
    @Excel(name = "时间")
    private String time;
    
    /** 模块 */
    @Excel(name = "模块")
    private String module;

    /** 错误码 */
    @Excel(name = "错误码")
    private String code;

    /** 等级 */
    @Excel(name = "等级")
    private String level;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return time;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getLevel()
    {
        return level;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    
    public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("module", getModule())
            .append("code", getCode())
            .append("level", getLevel())
            .append("content", getContent())
            .toString();
    }
}
