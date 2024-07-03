package com.bjlthy.lbss.dataComm.socket.protocol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version: V1.0
 * @author: 张宁
 * @description: 线程池
 * @date: 2020-11-05
 * @copyright: 北京龙田华远科技有限公司
 */

public class ThreadPoolHelper {
    // ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
    
    /**
     * executorService
     */
    private static ExecutorService executorService;

    /**
     * @author: 张宁
     * @description: 描述
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    static {
        executorService = Executors.newFixedThreadPool(100);
    }

    /**
     * @author: 张宁
     * @description: 添加线程
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
