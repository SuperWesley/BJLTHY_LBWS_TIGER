package com.bjlthy.lbss.dataComm.modbus;

import com.bjlthy.lbss.tool.DictionariesHelper;
import com.bjlthy.lbss.tool.LogbackUtil;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;
import org.slf4j.Logger;
import org.springframework.web.ErrorResponseException;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description Modbus通信接口
 * @date 2021年6月15日 下午5:00:14
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ModbusTCPMaster {

	/**
	 * 创建modbus工厂
	 */
	static ModbusFactory modbusFactory;
	static {
		if (modbusFactory == null) {
			modbusFactory = new ModbusFactory();
		}
	}

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("Modbus","ModbusTCPMaster");
	/**
	 * 获取master
	 * 
	 * @return
	 * @throws ModbusInitException
	 */
	public static ModbusMaster getMaster() throws ModbusInitException {
		
		String ip = DictionariesHelper.getDicStringValueByCode("Modbus_IP");
		int port = DictionariesHelper.getDicIntegerValueByCode("Modbus_PORT");
		//设置主机TCP参数
		IpParameters params = new IpParameters();
		//设置TCP的IP地址
		params.setHost(ip);
		//设置端口号
		params.setPort(port);
		// 创建一个主机
		ModbusMaster tcpMaster = modbusFactory.createTcpMaster(params, false);
		tcpMaster.init();

		return tcpMaster;
	}

	
	/***
	 * 写[03 Holding Register(4x)] 写一个 function ID = 6
	 * 
	 * @param slaveId 从机地址
	 * @param writeOffset 起始位置偏移量值
	 * @param writeValue 写入的数据
	 * @return 返回是否写入成功
	 * @throws ModbusTransportException
	 * 
	 * @throws ModbusInitException
	 */
	public static boolean writeRegister(int slaveId, int writeOffset, short writeValue)
			throws ModbusTransportException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		// 创建请求对象
		WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
		WriteRegisterResponse response = (WriteRegisterResponse) tcpMaster.send(request);
		if (response.isException()) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 
	 * 写入[03 Holding Register(4x)]写多个 function ID=16
	 * 
	 * @param slaveId
	 *            modbus的slaveID
	 * @param startOffset
	 *            起始位置偏移量值
	 * @param sdata
	 *            写入的数据
	 * @return 返回是否写入成功
	 * @throws ModbusTransportException
	 * @throws ModbusInitException
	 */
	public static boolean writeRegisters(int slaveId, int startOffset, short[] sdata)
			throws ModbusTransportException, ModbusInitException {
		// 获取master
		if(sdata.length > 100){
			short[] sdata1 = new short[100];
			short[] sdata2 = new short[sdata.length-100];
			System.arraycopy(sdata, 0, sdata1, 0, 100);
			System.arraycopy(sdata, 100, sdata2, 0, sdata.length-100);
			ModbusMaster tcpMaster = getMaster();
			// 创建请求对象
			WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata1);
			// 发送请求并获取响应对象
			ModbusResponse response = tcpMaster.send(request);
			
			WriteRegistersRequest request2 = new WriteRegistersRequest(slaveId, startOffset+100, sdata2);
			// 发送请求并获取响应对象
			ModbusResponse response2 = tcpMaster.send(request2);
			
			if (response.isException() || response2.isException()) {
				log.error("ModbusTCPMaster::writeRegisters - Modbus Server端连接错误"+"(0x000006)");
				return false;
			} else {
				return true;
			}
		}else{
			ModbusMaster tcpMaster = getMaster();
			// 创建请求对象
			WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
			// 发送请求并获取响应对象
			ModbusResponse response = tcpMaster.send(request);
			
			if (response.isException()) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 
	 * @author 张宁
	 * @Description: 读取[03 Holding Register类型 2x]模拟量数据
	 * @return Number    返回类型
	 * @date 2022年9月9日 上午10:50:06
	 * @throws
	 */
	public static short[] readHoldingRegister(int slaveId, int startOffset, int len) 
			throws ModbusTransportException, ModbusInitException {
		if(len <= 100){
			ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, startOffset, len);
			ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) getMaster().send(request);
			return response.getShortData();
		}else{
			ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, startOffset, 100);
			ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) getMaster().send(request);
			short[] ret1 = response.getShortData();
			
			ReadHoldingRegistersRequest request1 = new ReadHoldingRegistersRequest(slaveId, startOffset+99, len - 100);
			ReadHoldingRegistersResponse response1 = (ReadHoldingRegistersResponse) getMaster().send(request1);
			short[] ret2 = response1.getShortData();
			
			short[] ret= new short[ret1.length + ret2.length];
			System.arraycopy(ret1, 0, ret, 0, ret1.length);
			System.arraycopy(ret2, 0, ret, ret1.length, ret2.length);
			return ret;
		}
	}
	
	/**
	 * 读取[04 Input Registers 3x]类型 模拟量数据
	 * 
	 * @param slaveId
	 *            slaveId
	 * @param offset
	 *            位置
	 * @param dataType
	 *            数据类型,来自com.serotonin.modbus4j.code.DataType
	 * @return 返回结果
	 * @throws ModbusTransportException
	 *             异常
	 * @throws ErrorResponseException
	 *             异常
	 * @throws ModbusInitException
	 *             异常
	 */
	public static Number readInputRegisters(int slaveId, int offset, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 04 Input Registers类型数据读取
		BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, offset, dataType);
		Number value = null;
		try {
			value = getMaster().getValue(loc);
		} catch (com.serotonin.modbus4j.exception.ErrorResponseException e) {
			throw new RuntimeException(e);
		}
		return value;
	}

	
	/**
	 * 写入数字类型的模拟量（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
	 * 
	 * @param slaveId
	 * @param offset
	 * @param value
	 *            写入值,Number的子类,例如写入Float浮点类型,Double双精度类型,以及整型short,int,long
	 *            ,com.serotonin.modbus4j.code.DataType
	 * @throws ModbusTransportException
	 * @throws ErrorResponseException
	 * @throws ModbusInitException
	 */
	public static void writeHoldingRegister(int slaveId, int offset, Number value, int dataType)
			throws ModbusTransportException, ErrorResponseException, ModbusInitException {
		// 获取master
		ModbusMaster tcpMaster = getMaster();
		
		// 类型
		BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
		try {
			tcpMaster.setValue(locator, value);
		} catch (com.serotonin.modbus4j.exception.ErrorResponseException e) {
			throw new RuntimeException(e);
		}
	}
	

}
