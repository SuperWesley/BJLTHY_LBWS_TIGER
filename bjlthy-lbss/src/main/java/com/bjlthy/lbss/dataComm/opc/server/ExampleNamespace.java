/*
 * Copyright (c) 2021 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package com.bjlthy.lbss.dataComm.opc.server;

import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.LbssWorking;
import com.bjlthy.lbss.config.config.mapper.LbssWorkingMapper;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;
import com.bjlthy.lbss.tool.ConfigBean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.dtd.DataTypeDictionaryManager;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

public class ExampleNamespace extends ManagedNamespaceWithLifecycle {

    public static final String NAMESPACE_URI = "urn:eclipse:milo:lthy";

    private static final String[] DATA_NODES = {"nig_weight","mor_weight","aft_weight","weight","hour_weight","speed","density","gangueRatio","day_weight","month_weight","year_weight"};


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile Thread eventThread;
    private volatile boolean keepPostingEvents = true;

    private final DataTypeDictionaryManager dictionaryManager;

    private  OpcUaServer server;

    private final SubscriptionModel subscriptionModel;

    ExampleNamespace(OpcUaServer server) {
        super(server, NAMESPACE_URI);

        subscriptionModel = new SubscriptionModel(server, this);
        dictionaryManager = new DataTypeDictionaryManager(getNodeContext(), NAMESPACE_URI);

        getLifecycleManager().addLifecycle(dictionaryManager);
        getLifecycleManager().addLifecycle(subscriptionModel);

        getLifecycleManager().addStartupTask(this::createAndAddNodes);

        getLifecycleManager().addLifecycle(new Lifecycle() {
            @Override
            public void startup() {
                startBogusEventNotifier();
            }

            @Override
            public void shutdown() {
                try {
                    keepPostingEvents = false;
                    eventThread.interrupt();
                    eventThread.join();
                } catch (InterruptedException ignored) {
                    // ignored
                }
            }
        });
    }

    //创建节点并添加
    private void createAndAddNodes() {
        // Create a "lthy" folder and add it to the node manager
        //创建一个文件夹并将其添加到节点管理器
        NodeId folderNodeId = newNodeId("lthy");

        UaFolderNode folderNode = new UaFolderNode(
            getNodeContext(),
            folderNodeId,
            newQualifiedName("lthy"),
            LocalizedText.english("lthy")
        );

        getNodeManager().addNode(folderNode);

        // 确保我们的新文件夹显示在服务器的Objects文件夹下 .
        folderNode.addReference(new Reference(
            folderNode.getNodeId(),
            Identifiers.Organizes,
            Identifiers.ObjectsFolder.expanded(),
            false
        ));

        //自定义数据类型
        addCustomObjectTypeAndInstance(folderNode);
    }

    /**
     * 开始事件通知
     */
    private void startBogusEventNotifier() {
        // Set the EventNotifier bit on Server Node for Events.
        UaNode serverNode = getServer()
            .getAddressSpaceManager()
            .getManagedNode(Identifiers.Server)
            .orElse(null);

        if (serverNode instanceof ServerTypeNode) {
            ((ServerTypeNode) serverNode).setEventNotifier(ubyte(1));

            // Post a bogus Event every couple seconds
            //每隔几秒发布一个事件
            eventThread = new Thread(() -> {
                while (keepPostingEvents) {
                    try {
                        BaseEventTypeNode eventNode = getServer().getEventFactory().createEvent(
                            newNodeId(UUID.randomUUID()),
                            Identifiers.BaseEventType
                        );

                        eventNode.setBrowseName(new QualifiedName(2, "lthy.opc"));
                        eventNode.setDisplayName(LocalizedText.english("lthy.opc"));
                        eventNode.setEventId(ByteString.of(new byte[]{0, 1, 2, 3}));
                        eventNode.setEventType(Identifiers.BaseEventType);
                        eventNode.setSourceNode(serverNode.getNodeId());
                        eventNode.setSourceName(serverNode.getDisplayName().getText());
                        eventNode.setTime(DateTime.now());
                        eventNode.setReceiveTime(DateTime.NULL_VALUE);
                        eventNode.setMessage(LocalizedText.english("event message!"));
                        eventNode.setSeverity(ushort(2));

                        //noinspection UnstableApiUsage
                        getServer().getEventBus().post(eventNode);

                        eventNode.delete();
                    } catch (Throwable e) {
                        logger.error("Error creating EventNode: {}", e);
                    }

                    try {
                        //noinspection BusyWait
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("Error creating EventNode: {}",  e);
                    }
                }
            }, "bogus-event-poster");

            eventThread.start();
        }
    }


    /**
     * 自定义对象类型和实例
     * @param rootFolder
     */
    private void addCustomObjectTypeAndInstance(UaFolderNode rootFolder) {
        // Define a new ObjectType called "MyObjectType".
    	//添加父节点
        UaFolderNode scalarTypesFolder = new UaFolderNode(
                getNodeContext(),
                newNodeId("lthy.opc"),
                newQualifiedName("opc"),
                LocalizedText.english("opc")
        );

        getNodeManager().addNode(scalarTypesFolder);
        rootFolder.addOrganizes(scalarTypesFolder);
        LbssWorkingMapper workingMapper = SpringUtils.getBean(LbssWorkingMapper.class);
        //查询工作面信息
        List<LbssWorking> workingList = workingMapper.selectLbssWorkingNotALLList(null);
        //获取节点信息
        for (LbssWorking working : workingList) {
            String working_area = working.getWorking_area();
            String belt_name = working.getBelt_name();
        	//添加子节点
            UaFolderNode folderNode = new UaFolderNode(
                    getNodeContext(),
                    newNodeId("lthy.opc."+working_area+""),
                    newQualifiedName(working_area+""),
                    LocalizedText.english(working_area+"")
            );

            getNodeManager().addNode(folderNode);
            scalarTypesFolder.addOrganizes(folderNode);

            //添加子节点
            UaFolderNode folde2Node = new UaFolderNode(
                    getNodeContext(),
                    newNodeId("lthy.opc."+working_area+"."+belt_name+""),
                    newQualifiedName(belt_name+""),
                    LocalizedText.english(belt_name+"")
            );
            getNodeManager().addNode(folde2Node);
            folderNode.addOrganizes(folde2Node);
            
            List<LbssOpc> opcList = ConfigBean.opcList;

            //遍历子节点
            for (int i = 0; i < opcList.size(); i++) {
                LbssOpc opc = opcList.get(i);
                String nodeId = opc.getNodeId();
                //添加结点的性质
                UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
                        //NodeId需要加在folder结点下面，folder结点又是HelloWorld的子节点
                        .setNodeId(newNodeId("lthy.opc." +working_area+"." +belt_name+"."+nodeId))
                        .setAccessLevel(AccessLevel.READ_WRITE)//访问级别
                        .setUserAccessLevel(AccessLevel.READ_WRITE)//用户访问级别
                        .setBrowseName(newQualifiedName(nodeId))//内容名称
                        .setDisplayName(LocalizedText.english(nodeId))//显示名称
                        .setDataType(Identifiers.String)//数据类型
                        //添加结点的类型定义
                        .setTypeDefinition(Identifiers.BaseDataVariableType)
                        .build();

                node.setValue(new DataValue(new Variant("0.0")));
                node.getFilterChain().addLast(new AttributeLoggingFilter(AttributeId.Value::equals));
                //调用NodeManager添加结点
                getNodeManager().addNode(node);
                //由folder结点添加Orgnize引用
                folde2Node.addOrganizes(node);
            }

		}
        


    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
        subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }

}
