package com.bjlthy.lbss.tool;


import com.bjlthy.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能描述
 *
 * @author: zhn
 * @date: 2024年01月25日 22:30:01
 */
public class LidarWaveformProcessUtil {
    public static List<double[]> list = new ArrayList<>();
    public static void readFile(){
        File file = new File("D:\\HuaweiMoveData\\Users\\宁\\Desktop\\龙田\\皮带秤\\红柳林\\data\\2024-01-01_15_laser.txt");// Text文件
        String s = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            int index = 1;
            while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
                index++;
                sendToWebBrowser(s,index);
                if (index > 50){
                    br.close();
                }

            }
            br.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param message
     * @author 张宁
     * @description 接收雷达数据并在页面展示
     * @date 2020年11月16日 下午4:12:17
     */
    public static void sendToWebBrowser(String message,int index)  {

        try {
            String json ="";
            if (message.indexOf("1388 12D") > -1) {
                String saveValueM = message.substring(message.indexOf("1388 12D"));
                String saveValueN = saveValueM.substring(9, saveValueM.length() - 13);
                json = saveValueN;
            }
            String[] jsonArray = json.split(" ");

            double[] y = {};
            //数据转16进制进行组合处理
            int length = jsonArray.length;
            for (int i = 0; i < length; i++) {
                if (StringUtils.isNotEmpty(jsonArray[i])) {
                    y = insertElement(y, Integer.parseInt(jsonArray[i], 16), i);
                }
            }
            //消息发送至界面
            int begin = 90;
            int end = 200;
            //雷达数据0值过滤
            if(y.length>137) {
                //截取数据范围
                y = Arrays.copyOfRange(y, begin+3,end-3);
                int y_length = y.length;
//                double[][] arrays= new double[y_length][2];//汇总数组
                double[] arrays= new double[y_length];//汇总数组
                //统计0值个数
                int num=0;
                for(int i=0;i<y_length;i++){
                    if( i>0 && Math.abs(y[i] - y[i-1]) > 140){
                        double data = new BigDecimal(y[i-1]/Math.cos(0.5*Math.PI/180)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        y[i]=data;
                        num++;
                    }else if(y[0]==0){
                        double data = new BigDecimal(1400.0/Math.cos(0.5*Math.PI/180)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        y[i]=data;
                    }
                    arrays[i] = y[i];
                }
                list.add(arrays);
//                    System.out.println(JSON.toJSONString(arrays));
                //消息发送至界面
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void dataProcessing(List<double[]> list){
        double[] avg = null;
        for (double[] arr:list) {
            avg = new double[arr.length];
            for (int i = 0; i < arr.length; i++) {

            }
        }
    }


    /**
     * @param dims
     * @author 张宁
     * @description 组织雷达数据
     * @date 2020年11月16日 下午4:43:40
     */
    private static double[] insertElement(double[] dims, double element, int index) {
        int length = dims.length;
        double destination[] = new double[length + 1];
        System.arraycopy(dims, 0, destination, 0, index);
        destination[index] = element;
        System.arraycopy(dims, index, destination, index + 1, length - index);
        return destination;
    }
}
