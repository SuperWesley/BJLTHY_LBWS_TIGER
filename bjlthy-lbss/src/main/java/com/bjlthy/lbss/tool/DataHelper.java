package com.bjlthy.lbss.tool;


import com.bjlthy.lbss.data.compare.domain.LbssDatacompare;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DataHelper {

	/**
	 * 
	 * @author 张宁
	 * @Description: 校验松散密度和含矸率是否超出范围
	 * @param @param weight
	 * @param @param volume
	 * @return Map<String,Double>    返回类型
	 * @date 2021年9月11日 下午8:32:37
	 * @throws
	 */
	public static Map<String,Double> checkDensityAndGangueRatio(double weight,double volume){
		
		//松散密度
    	Double density = 0.00;
    	//含矸率
    	Double gangueRatio = 0.00;
    	
		if(weight>0 && volume>0){
    		//保留2位小数
    		density = Double.valueOf(String.format("%.2f", weight/volume));
    		Double density_min = DictionariesHelper.getDicDoubleValueByCode("density_min");
    		Double density_max = DictionariesHelper.getDicDoubleValueByCode("density_max");
    		//判断松散密度是否超范围
    		if(density < density_min){
				density = density_min;
			}else if(density > density_max){
				density = density_max;
			}
    		//计算含矸率
			gangueRatio = getGangueRatio(weight , volume/ConfigBean.songsan) * 100;
			Double gangue_min = DictionariesHelper.getDicDoubleValueByCode("gangue_min");
    		Double gangue_max = DictionariesHelper.getDicDoubleValueByCode("gangue_max");
			//判断含矸率是否超范围
			if(gangueRatio <0){
				gangueRatio = gangue_min;
			}else if(gangueRatio > gangue_max){
				gangueRatio = gangue_max;
			}
			
			gangueRatio = ParseUtils.formart(gangueRatio+"");
    	}
		
		Map<String,Double> resultMap = new HashMap<String, Double>();
		//存入map中
		resultMap.put("density", density);		
		resultMap.put("gangueRatio", gangueRatio);		
		//返回结果
		return resultMap;
	}
	
	/**
     * 
     * @author 张宁
     * @Title: getGangueRatio
     * @Description: 计算含矸率 = ρ矸*（w - ρ煤*v）/w*(ρ矸-ρ煤)
     * @param @param weight
     * @param @param volume
     * @param @return    设定文件
     * @return Double    返回类型
     * @date 2021年8月19日 下午10:45:46
     * @throws
     */
    public static Double getGangueRatio(Double weight,Double volume){
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	Double coal = DictionariesHelper.getDicDoubleValueByCode("coal");
    	Double gangue = DictionariesHelper.getDicDoubleValueByCode("gangue");
    	//煤石密度
    	map.put("x", coal);
    	//矸石密度
    	map.put("y", gangue);
    	//重量
    	map.put("w", weight);
    	//体积
    	map.put("v", volume);
    	//含矸率计算公式
    	String formula = "(y*(w - x*v))/(w*(y-x))";
    	//带入公式进行计算得出结果
    	Object result = ParseUtils.converToCode(formula, map);
    	//保留4位小数
    	String data = String.format("%.4f", result);
    	Double gangueRatio = Double.valueOf(data);
    	if(gangueRatio<0){
    		gangueRatio = 0.0006;
    	}
    	if(gangueRatio>0.1706){
    		gangueRatio = 0.1706;
    	}
		return gangueRatio;
    }
    
    /**
     * 
     * @author 张宁
     * @Title: getKuangWeigh
     * @Description: 计算矿出煤量数据 = 工作面长度*采高*每刀进尺*切割刀数*平均密度*矿采出率
     * 				  偏帮和后溜的煤量 = 工作面长度*(总采高-采高)*每刀进尺*切割刀数*平均密度
     * @param @param weight
     * @param @param volume
     * @param @return    设定文件
     * @return Double    返回类型
     * @date 2021年8月19日 下午10:45:46
     * @throws
     */
    public static Double getKuangWeigh(LbssDatacompare data){
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	//工作面长度
    	map.put("a", data.getLength());
    	//采高
    	map.put("h", data.getHeight());
    	//每刀进尺
    	map.put("c", data.getFootage());
    	//切割刀数
    	map.put("n", data.getCutNum());
    	//平均密度
    	map.put("k", data.getDensity());
    	//矿采出率
    	map.put("e", data.getRecovery_ratio());
    	//煤量计算公式
    	String formula = "a*h*c*n*k*e";
    	//带入公式进行计算得出结果
    	Object result = ParseUtils.converToCode(formula, map);
    	//保留4位小数
    	String value = String.format("%.2f", result);
    	Double weight = Double.valueOf(value);
		return weight;
    }
    
    /**
     * 
     * @author 张宁
     * @Title: getAfterWeigh
     * 		    偏帮和后溜的煤量 = 工作面长度*(总采高-采高)*每刀进尺*切割刀数*平均密度
     * @param @param weight
     * @param @param volume
     * @param @return    设定文件
     * @return Double    返回类型
     * @date 2021年8月19日 下午10:45:46
     * @throws
     */
    public static Double getAfterWeigh(LbssDatacompare data){
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	//工作面长度
    	map.put("a", data.getLength());
    	//总采高
    	map.put("H", data.getSumHeight());
    	//采高
    	map.put("h", data.getHeight());
    	//每刀进尺
    	map.put("c", data.getFootage());
    	//切割刀数
    	map.put("n", data.getCutNum());
    	//平均密度
    	map.put("k", data.getDensity());
    	//放煤率
    	map.put("e", data.getCoal_ratio());
    	//煤量计算公式
    	String formula = "a*(H-h)*c*n*k*e";
    	//带入公式进行计算得出结果
    	Object result = ParseUtils.converToCode(formula, map);
    	//保留4位小数
    	String value = String.format("%.2f", result);
    	Double weight = Double.valueOf(value);
		return weight;
    }
    
    /**
     * 
     * @author 张宁
     * @Description: 计算两个重量的相差比例
     * @return Double    返回类型
     * @date 2022年6月2日 上午10:59:27
     * @throws
     */
    public static Double getDiffRatio(Double weight1 , Double weight2){
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	//重量1
    	map.put("a", weight1);
    	//重量2
    	map.put("b", weight2);
    	
    	//相差比例计算公式
    	String formula = "(a-b)/a";
    	
    	//带入公式进行计算得出结果
    	Object result = ParseUtils.converToCode(formula, map);
    	//保留4位小数
    	String value = String.format("%.4f", result);
    	Double weight = Double.valueOf(value);
    	
		return weight;
    	
    }

	/**
	 *
	 * @author 张宁
	 * @Description: 计算平均采高
	 * @return Double    返回类型
	 * @throws
	 */
	public static Double getAvgHeight(Double volume , Integer num,Double length){

		Map<String, Object> map = new HashMap<String, Object>();
		//体积
		map.put("v", volume);
		//刀数
		map.put("n", num);
		//长度
		map.put("l", length);

		//计算公式
		String formula = "v/(n*l)";

		//带入公式进行计算得出结果
		Object result = ParseUtils.converToCode(formula, map);
		//保留4位小数
		String value = String.format("%.2f", result);
		Double height = Double.valueOf(value);

		return height;

	}

	//默认除法运算精度
  	private static final int DEF_DIV_SCALE = 10; 
  	
  	/** * 提供精确的加法运算。 * @param v1 被加数 * @param v2 加数 * @return 两个参数的和 */  
  	public static double add(double v1,double v2){ 
        
  		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
  		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
  		return b1.add(b2).doubleValue();   
  	}   
  	
  	/** * 提供精确的减法运算。 * @param v1 被减数 * @param v2 减数 * @return 两个参数的差 */  
  	public static double sub(double v1,double v2){ 
        
  		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
  		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
  		return b1.subtract(b2).doubleValue();   
  	}   
  	
  	/** * 提供精确的乘法运算。 * @param v1 被乘数 * @param v2 乘数 * @return 两个参数的积 */  
  	public static double mul(double v1,double v2){ 
        
  		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
  		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
  		return b1.multiply(b2).doubleValue();   
  	}   
  	
  	/** * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 * 小数点以后10位，以后的数字四舍五入。 * @param v1 被除数 * @param v2 除数 * @return 两个参数的商 */  
  	public static double div(double v1,double v2){ 
        
  		return div(v1,v2,DEF_DIV_SCALE);   
  	}   
  	
  	/** * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 * 定精度，以后的数字四舍五入。 * @param v1 被除数 * @param v2 除数 * @param scale 表示表示需要精确到小数点以后几位。 * @return 两个参数的商 */  
  	public static double div(double v1,double v2,int scale){ 
        
  		if(scale<0){ 
        
  			throw new IllegalArgumentException("The scale must be a positive integer or zero");   
  		}   
  		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
  		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
  		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();   
  	}   
  	
  	/** * 提供精确的小数位四舍五入处理。 * @param v 需要四舍五入的数字 * @param scale 小数点后保留几位 * @return 四舍五入后的结果 */  
  	public static double round(double v,int scale){ 
        
  		if(scale<0){ 
        
  			throw new IllegalArgumentException("The scale must be a positive integer or zero");   
  		}   
  		BigDecimal b = new BigDecimal(Double.toString(v));   
  		BigDecimal one = new BigDecimal("1");   
  		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();   
  	}  
}
