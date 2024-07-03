package com.bjlthy.lbss.tool;

import com.bjlthy.common.utils.StringUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 数据类型转换工具类
 * @date 2021年6月15日 下午4:58:34 @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ParseUtils {

	/**
	 * 截取byte数组
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return 小端，有符号
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		return bs;
	}

	/**
	 * 字节数组转int（小端排序）
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArrayToInt(byte[] b) {
		int intValue = 0;
		for (int i = 0; i < b.length; i++) {
			intValue += (b[i] & 0xFF) << (8 * i);
		}
		return intValue;
	}

	/**
	 * 字节数组转不同进制字符串
	 * 
	 * @param bytes
	 * @param radix
	 * @return
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	public static double bytes2Double(byte[] arr) {
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value |= ((long) (arr[i] & 0xff)) << (8 * i);
		}
		return Double.longBitsToDouble(value);
	}

	/**
	 * 双精度浮点型转byte[]
	 * 
	 * @param d
	 *            double值
	 * @return byteRet byte[]
	 */
	public static byte[] double2Bytes(double d) {
		long value = Double.doubleToRawLongBits(d);
		byte[] byteRet = new byte[8];
		for (int i = 0; i < 8; i++) {
			byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
		}
		return byteRet;
	}

	/**
	 * 整型数值转为 byte[]
	 * 
	 * @param number
	 * @return byte[]
	 */
	public byte[] int2Bytes(int number) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) number;
		bytes[2] = (byte) ((number >> 8) & 0xFF);
		bytes[1] = (byte) ((number >> 16) & 0xFF);
		bytes[0] = (byte) ((number >> 24) & 0xFF);
		return bytes;
	}

	/**
	 * 将byte 数值转为 HEX字符串
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String bytesToHex(byte[] bytes) {
		String hex = new BigInteger(1, bytes).toString(16);
		return hex;
	}

	/**
	 * 计算校验和
	 * 
	 * @param msg
	 */
	public static String checkData(String msg) {
		char[] result = msg.toCharArray();
		String he = sumCheck(result, result.length);
		if (he.length() < 2) {
			he = "0" + he;
		}
		return he;
	}

	public static String sumCheck(char[] b, int len) {
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum = sum + b[i];
		}
		// System.out.println(sum);
		/*
		 * if(sum > 0xff){ //超过了255，使用补码（补码 = 原码取反 + 1） sum = ~sum; sum = sum +
		 * 1; }
		 */
		return Integer.toHexString((sum & 0x0ff)).toUpperCase();
	}

	/**
	 * ping IP加端口
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean pingIpAndPort(String ip, int port) {
		if (null == ip || 0 == ip.length() || port > 65535) {
			return false;
		}
		if (!pingIp(ip)) {
			return false;
		}
		Socket s = new Socket();
		try {
			SocketAddress add = new InetSocketAddress(ip, port);
			s.connect(add, 3000);// 超时3秒
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ping ip
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean pingIp(String ip) {
		if (null == ip || 0 == ip.length()) {
			return false;
		}
		try {
			InetAddress.getByName(ip);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 
	 * @author 张宁
	 * @Title: converToCode 
	 * @Description: java 将字符串转成可执行代码  工具类
	 * @param  jexlExp  
	 * @param map  
	 * @return 设定文件 Object 返回类型 
	 * @date 2021年8月20日 下午3:10:06 
	 */
	public static Object converToCode(String jexlExp, Map<String, Object> map) {

		JexlEngine jexl = new JexlEngine();
		Expression expression = jexl.createExpression(jexlExp);

		JexlContext jc = new MapContext();

		for (String key : map.keySet()) {
			jc.set(key, map.get(key));
		}
		if (null == expression.evaluate(jc)) {
			return "";
		}

		return expression.evaluate(jc);

	}
	/**
     * 保留小数,四舍五入
     *
     * @param data
     * @param amount
     * @return
     */
    public static Double formart(String data) {
        if (StringUtils.isEmpty(data)){
            return 0.00;
        }    
        //利用BigDecimal来实现四舍五入.保留一位小数
        double result = new BigDecimal(data).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //1代表保留1位小数,保留两位小数就是2,依此累推
        //BigDecimal.ROUND_HALF_UP 代表使用四舍五入的方式
        return result;
    }
    
    /**
     * 保留小数,四舍五入
     *
     * @param data
     * @param amount
     * @return
     */
    public static Double formart(String data, Integer amount) {
        if (StringUtils.isEmpty(data)){
            return 0.00;
        }    
        //利用BigDecimal来实现四舍五入.保留一位小数
        double result = new BigDecimal(data).setScale(amount, BigDecimal.ROUND_HALF_UP).doubleValue();
        //1代表保留1位小数,保留两位小数就是2,依此累推
        //BigDecimal.ROUND_HALF_UP 代表使用四舍五入的方式
        return result;
    }
    /**
     * 保留小数,四舍五入
     *
     * @param data
     * @param amount
     * @return
     */
    public static String formart2(String data) {
        if (StringUtils.isEmpty(data)){
            return "";
        }    
        //利用BigDecimal来实现四舍五入.保留一位小数
        double result = new BigDecimal(data).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //1代表保留1位小数,保留两位小数就是2,依此累推
        //BigDecimal.ROUND_HALF_UP 代表使用四舍五入的方式
        return String.valueOf(result);
    }
}