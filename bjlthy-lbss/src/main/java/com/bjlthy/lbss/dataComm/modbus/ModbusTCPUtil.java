package com.bjlthy.lbss.dataComm.modbus;

import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;

/***
 * 
 * @version V1.0
 * @author 张宁
 * @description 电液控控制工具类
 * @date switch_bit1年12月3日 上午10:40:48 @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ModbusTCPUtil {

	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("Modbus", "EHCModbusUtil");

	/**
	 * 
	 * @author 张宁 @Description: 往Modbus点表更新信息 @return void 返回类型 @date 2022年9月8日
	 * 下午8:17:21 @throws
	 */
//	public static void writeNodesToModbus(Map<String, Object> map) {
//		Map<NodeId, String> nodeMap = new HashMap<>();
//		List<LbssOpc> list = ConfigBean.opcMap.get(ConfigBean.belt_name);
//		// 添加节点信息
//		try {
//			Integer area = Integer.valueOf(ConfigBean.belt_name);
//			ModbusTCPMaster.writeHoldingRegister(1, 1, area, DataType.FOUR_BYTE_FLOAT);
//			for (int i = 0; i < list.size(); i++) {
//				LbssOpc modbus = list.get(i);
//				String addressBit = modbus.getAddressBit();
//				if (!addressBit.contains("List")) {
//					String str = (String) map.get(addressBit);
//					Double value = Double.valueOf(str);
//					ModbusTCPMaster.writeHoldingRegister(1, i + 1, value, DataType.FOUR_BYTE_FLOAT);
//
//				}
//			}
//
//		} catch (ErrorResponseException | ModbusTransportException | ModbusInitException e) {
//			log.warn("ModbusTCPUtil::writeNodesToModbus 数据写入失败");
//			e.printStackTrace();
//		}
//
//	}

}
