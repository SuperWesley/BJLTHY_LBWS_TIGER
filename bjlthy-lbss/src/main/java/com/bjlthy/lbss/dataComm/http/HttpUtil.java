package com.bjlthy.lbss.dataComm.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.mapper.LbssARMConfigMapper;
import com.bjlthy.lbss.tool.*;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class HttpUtil {

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("HttpUtil","HttpUtil");
	public static String status="0";
	public static LbssARMConfigMapper armConfigMapper = SpringUtils.getBean(LbssARMConfigMapper.class);
	
	public static void getBeltSpeed(Map<String,Object> map) throws Exception {
		String url = "";
		if("01".equals(ConfigBean.belt_name)){
			//主运
			url = "http://05250d9f5e874a308021093b8a94ef46.apigateway.res.cloud.caojiatan.com/RealTimeData_DHPD?appCode=ae26246c33714b5baefdd7d6d803f37c&pageNum=1&pageSize=10&sntype=&pointname=%E5%A4%A7%E5%B7%B7%E7%9A%AE%E5%B8%A6";
		}else if("07".equals(ConfigBean.belt_name)){
			//07皮带
			url = "http://05250d9f5e874a308021093b8a94ef46.apigateway.res.cloud.caojiatan.com/RealTimeData_07SC?appCode=ae26246c33714b5baefdd7d6d803f37c&pageNum=1&pageSize=10&sntype=&pointname=07%E9%A1%BA%E6%A7%BD%E7%9A%AE%E5%B8%A6";
		}else if("10".equals(ConfigBean.belt_name)){
			//10皮带
			url = "http://05250d9f5e874a308021093b8a94ef46.apigateway.res.cloud.caojiatan.com/RealTimeData_10SC?appCode=ae26246c33714b5baefdd7d6d803f37c&pageNum=1&pageSize=10&sntype=&pointname=10%E9%A1%BA%E6%A7%BD%E7%9A%AE%E5%B8%A6";
		}
		String msg = doGet(url);
		if(StringUtils.isNotEmpty(msg)){
			Double speed = dataProcessing(msg);
			map.put("speed", String.valueOf(speed));
			//保存皮带数据
			String day = DateUtil.getDate();
			FileUtil.writeFileAndTime(PathUtil.KuangSpeed + day + "_speed.txt", String.valueOf(speed));
			if(!"2".equals(status)){
				//查询速度模拟开关控制并更新-->开启读取OPC速度
				LbssARMConfig LbssARMConfig = armConfigMapper.selectLbssARMConfigById(14);
				LbssARMConfig.setValue("2");
				armConfigMapper.updateLbssARMConfig(LbssARMConfig);
				status ="2";
				//更新至下位机
//				NanoConfigProcessor.openARMConfigClient();
			}
		}else{
			if(!"3".equals(status) && !"1".equals(status)){
        		//查询速度模拟开关控制并更新-->开启使用模拟速度
        		LbssARMConfig LbssARMConfig = armConfigMapper.selectLbssARMConfigById(14);
        		LbssARMConfig.setValue("3");
        		armConfigMapper.updateLbssARMConfig(LbssARMConfig);
        		status ="3";
        		//更新至下位机
//        		NanoConfigProcessor.openARMConfigClient();
        	}
		}
		
	}
    /**
     * http get请求
     * @param httpUrl 链接
     * @return 响应数据
     */
    public static String doGet(String httpUrl){
        //链接
        HttpURLConnection connection=null;
        InputStream is=null;
        BufferedReader br = null;
        StringBuffer result=new StringBuffer();
        try {
            //创建连接
            URL url=new URL(httpUrl);
            connection= (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setConnectTimeout(15000);
            //设置读取超时时间
            connection.setReadTimeout(15000);
            //开始连接
            connection.connect();
            //获取响应数据
            if(connection.getResponseCode()==200){
                //获取返回的数据
                is=connection.getInputStream();
                if(is!=null){
                    br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String temp = null;
                    while ((temp=br.readLine())!=null){
                        result.append(temp);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
            	connection.disconnect();// 关闭远程连接
            }
        }
        return result.toString();
    }

    
    /**
     * 
     * @author 张宁
     * @Description: 数据处理
     * @return void    返回类型
     * @date 2022年6月18日 上午10:08:03
     * @throws
     */
    public static Double dataProcessing(String data) {
    	//{"data":{"totalNum":1,"pageSize":10,"rows":[{"sensorcategory":1,"sensorcategoryname":"模拟量","systemcode":88,"datatime":"2022-06-27 15:10:52","sensortypename":"模拟量","datastatedescription":"正常","datastate":21,"pointname":"10顺槽皮带","originalpointcode":"SC10.HD.SC10_PD_SD","alarm":false,"realtimevalue":"3.81","state":21,"areaname":"10顺槽皮带","sensortype":"模拟量","systemname":"","sbcode":"","highrange":100.0000000000,"statedescription":"正常","unit":"","pointcode":"SC10_PD_SD","realtimevaluedescription":"3.81 ","by1":"","v1":"","v2":"","v3":"","lowrange":0E-10}],"pageNum":1},"errCode":0,"requestId":"0a28031e16563138666062729e0409","errMsg":"success","apiLog":null}
    	Double speed = 0.00;
    	try {
    		JSONObject obj1 = JSONObject.parseObject(data);
    		if(obj1 != null){
    			if(obj1.get("data") != null){
    				
    				String msg1 = obj1.get("data").toString();
    				JSONObject json1 = JSONObject.parseObject(msg1);
    				if(json1 != null){
    					String val1 = json1.get("rows").toString();
    					//获取皮带速度
    					if(StringUtils.isNotEmpty(val1)){
    						JSONArray createArray=JSONArray.parseArray(val1);
    						for(int i=0;i<createArray.size();i++){
    	    					String originalpointcode = JSONObject.parseObject(JSONObject.toJSONString(createArray.get(i))).getString("originalpointcode");
    	    					if("DHZX.DZ.SPEED_DH_BELT".equals(originalpointcode)) {
    	    						
    	    						String realtimevalue = JSONObject.parseObject(JSONObject.toJSONString(createArray.get(i))).getString("realtimevalue");
    	    						speed = ParseUtils.formart(realtimevalue);
    	    						break;
    	    					}
    	    					
    	    				}
        					
    					}
    				}
    			}
    			
    		}
			
		} catch (Exception e) {
			speed = 0.0;
			e.printStackTrace();
		}
       
    	return speed;
    }
    
}
