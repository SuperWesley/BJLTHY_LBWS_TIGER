package com.bjlthy.lbss.config.config.domain;

import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 服务器ip配置对象 lbss_server_ipconfig
 * 
 * @author zhangning
 * @date 2023-11-05
 */
public class LbssServerIpconfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 服务器ip */
    @Excel(name = "服务器ip")
    private String serverIp;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("serverIp", getServerIp())
            .toString();
    }
}
