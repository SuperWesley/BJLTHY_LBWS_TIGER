package com.bjlthy.lbss.dataComm.opc.server;

import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 类解释说明
 * @date 2022年9月13日 上午9:34:35
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
@Component
@Order(2)
public class OPCUAServerRunner implements CommandLineRunner {

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("OPCUA","OPCUAServerRunner");
		
	@Override
	public void run(String... args) throws Exception {
		 /* createSlave方法执行完成后，会进行监听，影响之后的业务运行，需要以线程的方式进行启动 */
//		Integer status = DictionariesHelper.getDicIntegerValueByCode("server_or_client");
//		if(status == 1){
//			System.out.println("启动opcua 服务");
//			new Thread(() -> {
//				createOPCServer();
//			}).start();
//		}

	}
	
	/**
     * @description 创建OPCUA服务器
     * @author 张宁
     * @date 2021-06-14
     * @update 2021-06-15
     */
    public void createOPCServer() {
    	
		try {
			ExampleServer server = new ExampleServer();
			server.startup().get();
			ConfigBean.opc_state = true;
			
			final CompletableFuture<Void> future = new CompletableFuture<>();
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));
			
			future.get();
			System.out.println("opcua状态：启动成功");
			
		} catch (Exception e) {
			log.error("OPCUAServerRunner::createServer - OPCUAServerRunner start fail, Error code(0x00000A).",e);
		}

    }

}
