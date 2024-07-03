package com.bjlthy.lbss.dataComm.modbus;

import com.serotonin.modbus4j.BasicProcessImage;


/**
 * 寄存器及线圈
 */
public class Register {

    public static BasicProcessImage getModscanProcessImage(int slaveId) {

        /* 初始化过程影像区 */
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        /* 初始化寄存器数据范围 */
        processImage.setInvalidAddressValue(Short.MIN_VALUE);
        /* 初始化过程影像区各个数值为0 */
        for(int i = 0; i < 608; i++){
            processImage.setHoldingRegister(i, (short) 0);
        }
        /* 为影像区添加监听 */
//        processImage.addListener(new BasicProcessImageListener());
        return processImage;
    }
}