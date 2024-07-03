package com.bjlthy.lbss.dataComm.opc.client;


import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.ServiceFaultListener;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * OPCUA客户端工具类
 */
public class OPCUAClient {

    Logger log = LoggerFactory.getLogger(getClass());

    /**
     * OPCUA客户端对象
     */
    private OpcUaClient client;

    /**
     * 服务链接地址
     */
   /* private String endpointUrl;

    *//**
     * 应用链接地址
     *//*
    private String applicationUri;

    *//**
     * 链接账户
     *//*
    private String userName;

    *//**
     * 链接地址
     *//*
    private String passWord;

    *//**
     * 节点容器
     *//*
    private Map<NodeId, String> nodeValues = new HashMap<>();*/

    /**
     * 运行状态
     */
    private boolean runningState;

	/**
     * 工具类构造函数
     * @param endpointUrl 服务链接地址
     * @param applicationUri 应用链接地址
     */
   /* public OPCUAClient(String endpointUrl, String applicationUri){
        this.endpointUrl = endpointUrl;
        this.applicationUri = applicationUri;
        this.userName = null;
        this.passWord = null;
    }

    *//**
     * 工具类构造函数
     * @param endpointUrl 服务链接地址
     * @param applicationUri 应用链接地址
     * @param userName 账户
     * @param passWord 密码
     *//*
    public OPCUAClient(String endpointUrl, String applicationUri, String userName, String passWord){
        this.endpointUrl = endpointUrl;
        this.applicationUri = applicationUri;
        this.userName = userName;
        this.passWord = passWord;
    }*/

    /**
     * 创建OPC链接
     * @return boolean 返回链接状态
     */
    public boolean createConnection() {
        try {
            synchronized (this){
                //已连接，不重新链接
                if (runningState){
                    runningState = false;
                    return runningState;
                }
                Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
                Files.createDirectories(securityTempDir);
                if (!Files.exists(securityTempDir)) {
                    throw new Exception("unable to create security dir: " + securityTempDir);
                }

                //加载秘钥
//                KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);
                //安全策略 None、Basic256、Basic128Rsa15、Basic256Sha256
                List<EndpointDescription> endpoints;

                try {
                    endpoints = DiscoveryClient.getEndpoints(OPCUAUtil.endpointUrl).get();
                } catch (Throwable ex) {
                    // 发现服务
                    String discoveryUrl = OPCUAUtil.endpointUrl;

                    if (!discoveryUrl.endsWith("/")) {
                        discoveryUrl += "/";
                    }
//                discoveryUrl += "discovery";

                    endpoints = DiscoveryClient.getEndpoints(discoveryUrl).get();
                }
                //过滤掉不需要的安全策略，选择一个自己需要的安全策略
                EndpointDescription endpoint = endpoints.stream()
                        .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                        .findFirst()
                        .orElseThrow(() -> new Exception("没有连接上端点"));
                String url = OPCUAUtil.endpointUrl.split(":")[1];
                //修改URL地址
                endpoint = EndpointUtil.updateUrl(endpoint, url.replace("//", ""));
                OpcUaClientConfig clientConfig = OpcUaClientConfig.builder()
                        .setApplicationName(LocalizedText.english(OPCUAUtil.applicationUrl.substring(OPCUAUtil.applicationUrl.lastIndexOf(":") + 1)))
                        .setApplicationUri(OPCUAUtil.applicationUrl)
//                        .setCertificate(loader.getClientCertificate())
//                        .setKeyPair(loader.getClientKeyPair())
                        .setEndpoint(endpoint)
                        .setIdentityProvider(new AnonymousProvider())
                        .setRequestTimeout(UInteger.valueOf(5000))
                        .build();
                client = OpcUaClient.create(clientConfig);
                client.connect().get();
                client.addFaultListener(new UaFaultListener());
                runningState = true;
            }
        } catch (Exception e) {
            log.info("创建ua客户端时发生了错误", e);
            runningState = false;
        }

        return runningState;
    }

    
    /**
     * 断开OPC链接
     */
    public void disconnect() {
        runningState = false;
        try {
            //客户端断开链接
            client.disconnect().get();
            //释放共享资源
            Stack.releaseSharedResources();
        } catch (InterruptedException | ExecutionException e) {
            log.error("设备断开连接的时候发生了错误: {}", e.getMessage(), e);
        }
    }


    /**
     * 批量订阅
     * @param nodeIds 节点集
     * @param sf 订阅周期，单位：毫秒
     */
    /*public void subscription(List<NodeId> nodeIds, double sf) {
        try {
            UaSubscription subscription = client.getSubscriptionManager().createSubscription(sf).get();

            List<MonitoredItemCreateRequest> requests = new ArrayList<>();

            int i = 1;

            for (NodeId nodeId : nodeIds) {
                ReadValueId readValueId = new ReadValueId(
                        nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE
                );

                MonitoringParameters parameters = new MonitoringParameters(
                        UInteger.valueOf(i++), sf, null, UInteger.valueOf(10), true
                );

                MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(
                        readValueId, MonitoringMode.Reporting, parameters
                );

                requests.add(request);
            }

            List<UaMonitoredItem> items = subscription.createMonitoredItems(
                    TimestampsToReturn.Both,
                    requests,
                    (item, id) -> item.setValueConsumer(this::onSubscriptionValue)
            ).get();

        } catch (InterruptedException | ExecutionException e) {
            log.info("订阅点位时发生了错误", e);
            disconnect();
        }
    }

    *//**
     * 更新节点数据
     * @param item 监听项
     * @param value 数据值
     *//*
    private void onSubscriptionValue(UaMonitoredItem item, DataValue value) {
        if (item.getStatusCode().isGood()) {
            nodeValues.put(
                    item.getReadValueId().getNodeId(),
                    value.getValue().getValue().toString()
            );
        } else {
            log.info("点位[{}]读到了脏数据[{}]",
                    item.getReadValueId().getNodeId(),
                    value.getValue().getValue().toString()
            );
        }
    }

    *//**
     * 读取OPC节点数据
     * @return
     * @throws ConnectionInterruptException
     *//*
    public Map<NodeId, String> read() throws ConnectionInterruptException {
        if (!runningState) {
            throw new ConnectionInterruptException("ua客户端未运行或链接中断");
        }
        return this.nodeValues;
    }
    
    *//**
     * 
     * @author 张宁
     * @Description: 读取OPC节点数据:
     * @return DataValue    返回类型
     * @date 2022年7月6日 上午9:38:40
     * @throws
     *//*
    public DataValue getNodeMessage(int namespaceIndex, String identifier){
        NodeId nodeId = new NodeId(namespaceIndex,identifier);
        DataValue dataValue = null;
        try {
        	if(client != null){
        		dataValue = this.client.readValue(0.0, TimestampsToReturn.Both, nodeId).get();
        	}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return dataValue;
    }
    
    *//**
     * 写入节点数据(单个)
     *
     * @param client
     * @throws Exception
     *//*
    public  void writeNodeValue(NodeId nodeId,float value) {
        try {
        	//创建数据对象,此处的数据对象一定要定义类型，不然会出现类型错误，导致无法写入
        	DataValue nowValue = new DataValue(new Variant(value), null, null);
        	//写入节点数据
			client.writeValue(nodeId, nowValue).get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("节点数据写入失败: {}", e.getMessage(), e);
		}
    }*/
    

    /**
     * 写入OPC节点数据(多个)
     * @param writeNodes 更新OPC数据集
     * @return boolean 写入状态
     */
    public boolean writeNodes(Map<NodeId, String> writeNodes) {
        try {

            List<NodeId> nodeIds = new ArrayList<>();
            List<DataValue> dataValues = new ArrayList<>();
            for (NodeId nodeId : writeNodes.keySet()) {
                nodeIds.add(nodeId);
                Variant v = new Variant(writeNodes.get(nodeId));
                dataValues.add(new DataValue(v, null, null));
            }
            // write asynchronously....
            CompletableFuture<List<StatusCode>> f = client.writeValues(nodeIds, dataValues);

            // ...but block for the results so we write in order
            List<StatusCode> statusCodes = f.get();
            for (int i = 0; i < statusCodes.size(); i++) {
                if (!statusCodes.get(i).isGood()) {
                    log.info("向点位（NodeId={}）写值失败", nodeIds.get(i));
                    return false;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            log.info("向点位写值时发生了错误", e);
            disconnect();
            return false;
        }
        return true;
    }



    /**
     * 服务监听
     */
    public class UaFaultListener implements ServiceFaultListener {

        @Override
        public void onServiceFault(ServiceFault serviceFault) {
            log.info("UA的连接发生了错误，即将断开连接");
            disconnect();
        }
    }

    public class ConnectionInterruptException extends Exception{
        public ConnectionInterruptException(String message) {
            super(message);
        }
    }


    public OpcUaClient getClient() {
        return client;
    }

    public void setClient(OpcUaClient client) {
        this.client = client;
    }

    public boolean isRunningState() {
        return runningState;
    }

    public void setRunningState(boolean runningState) {
        this.runningState = runningState;
    }
}
