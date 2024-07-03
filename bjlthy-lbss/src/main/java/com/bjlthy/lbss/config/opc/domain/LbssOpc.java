package com.bjlthy.lbss.config.opc.domain;


import com.bjlthy.common.annotation.Excel;
import com.bjlthy.common.core.domain.BaseEntity;

/**
 * opcua 配置对象 lbss_opc_modbus
 * 
 * @author zhangning
 * @date 2022-05-10
 */
public class LbssOpc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 通道 */
    @Excel(name = "通道")
    private String passage;

    /** 设备 */
    @Excel(name = "设备")
    private String equipment;

    /** 节点 */
    @Excel(name = "节点")
    private String nodeId;

    /** 节点说明 */
    @Excel(name = "节点说明")
    private String nodeName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LbssOpc{" +
                "id=" + id +
                ", passage='" + passage + '\'' +
                ", equipment='" + equipment + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public LbssOpc(Integer id, String passage, String equipment, String nodeId, String nodeName, String status) {
        this.id = id;
        this.passage = passage;
        this.equipment = equipment;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.status = status;
    }
}
