package com.bjlthy.lbss.dataComm.socket.processor.label;

import com.bjlthy.lbss.dataComm.redis.RedisTopic;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.FileUtil;
import com.bjlthy.lbss.tool.LogbackUtil;
import com.bjlthy.lbss.tool.PathUtil;
import com.mysql.cj.protocol.MessageListener;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LabelDataSave   {

    // 日志工具类
    private static Logger log = LogbackUtil.getLogger("LabelDataSave", "LabelDataSave");

    /**
     * 保存电子秤原始数据
     * @param msg 接收数据
     * @param ip 接收IP
     */
    public static void saveLabelData(String msg, String ip){

        try {
            //根据IP地址获取工作面信息
            String belt_name = ConfigBean.beltsIPMap.get(ip);
            if (belt_name == null) {
                return;
            }
            // 保存数据
            String timeStrH = msg.substring(0, 13).replace(" ", "_");
            String day = msg.substring(0, 10);
            String[] datas = msg.split(",");
            String data = datas[0].split(" ")[3]+","+datas[1]+","+datas[2]+","+datas[3]+","+datas[4]+","+datas[5]+","+datas[6];

            //按照工作面信息存储到对应的实时数据表中
            String path = PathUtil.path+"/Belt/"+belt_name+PathUtil.elecsPath;
            if (!data.contains("-")) {
                FileUtil.writeFile(path + day + "/" + timeStrH + "_belt.txt", msg.trim());
            }
        } catch (Exception e) {
            log.error("LabelDataSave:saveElecsData - 数据保存失败	" + e.getMessage());
        }
    }
}
