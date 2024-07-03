package com.bjlthy.lbss.dataComm.modbus;

import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;
import com.bjlthy.lbss.tool.LogbackUtil;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.exception.ModbusInitException;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 张宁
 * @date 2021-06-10
 * @lastupdate 2021-06-14
 */
@Component
@Order(3)
public class ModbusTCPSlave implements CommandLineRunner {

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("Modbus","ModbusTCPSlave");
    @Override
    public void run(String ...args) throws Exception{
        /* createSlave方法执行完成后，会进行监听，影响之后的业务运行，需要以线程的方式进行启动 */
//    	Integer status = DictionariesHelper.getDicIntegerValueByCode("server_or_client");
//    	if(status == 2){
//            System.out.println("启动Modbus 服务");
//            new Thread(() -> {
//    			createSalve();
//    		}).start();
//    	}
    }

    /**
     * @description 创建ModbusTCP服务器
     * @author 张宁
     * @date 2021-06-14
     * @update 2021-06-15
     */
    public void createSalve() {
        //创建modbus工厂
        ModbusFactory modbusFactory = new ModbusFactory();
        //创建TCP服务端
        final ModbusSlaveSet salve = modbusFactory.createTcpSlave(false);
        //向过程影像区添加数据
        salve.addProcessImage(Register.getModscanProcessImage(1));
        //获取寄存器
        salve.getProcessImage(1);
        try {
        	ConfigBean.opc_state = true;
            salve.start();
        } catch (ModbusInitException e) {
        	log.info("ModbusTCPSlave::createSalve - ModbusTCPSalve start fail, Error code(0x000010).");
        }
    }
}