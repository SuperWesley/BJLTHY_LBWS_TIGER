package com.bjlthy.lbss.dataComm.socket.thread;


import com.bjlthy.common.utils.DateUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.tool.*;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.Set;

/**
 *
 * @version V1.0
 * @author 张宁
 * @description 压缩文件
 * @date 2020年11月16日 下午4:56:16
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */

public class BackupRunable implements Runnable{

	public String username = "root";
	public String password = "bjlthy";
	public String databasename;
	public Integer day;

	public JdbcTemplate jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);
	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("BackupRunable", "BackupRunable");


	public BackupRunable(String databasename,Integer day){
		this.databasename = databasename;
		this.day = day;
	}

	@Override
	public void run() {

		Set<String> beltNames = ConfigBean.beltsMap.keySet();
		for (String beltName : beltNames) {
			if ("ALL".equals(beltName)) {
				continue;
			}
			String tablename = "lbss_weight";
			String dir = PathUtil.backupPath+beltName+"/";
			String sqlName = DateUtils.dateTimeNow()+"_"+tablename +"_";
			String sqlPathName = dir + sqlName;

			File backUpFile = new File(dir);
			if (!backUpFile.exists()) {
				backUpFile.mkdirs();
			}
			File dataFile = new File(sqlPathName+".sql");
			//拼接cmd命令
			StringBuffer sb = new StringBuffer();
			sb.append("mysqldump");
			sb.append(" -u "+username);
			sb.append(" -p"+password);
			sb.append(" "+ databasename+" " +tablename+" > ");
			sb.append(dataFile);
			log.info("======数据库"+tablename+"备份cmd命令为："+sb.toString()+"=======");
			try {
				Process exec = Runtime.getRuntime().exec("cmd /c"+sb.toString());
				if (exec.waitFor() == 0){
					deleteDBOldData(tablename);
					log.info("======数据库"+tablename+"备份成功");
				}
			} catch (Exception e) {
				log.error("======数据库备份失败，异常为："+e.getMessage()+"=======");
			}

		}
	}

	/**
	 * 删除数据库旧数据
	 * @param tablename
	 */
	public void deleteDBOldData(String tablename) throws Exception{
		String lastDay = DateHelper.getLastDay(DateUtil.getDate(),day);
		String querySQL = "DELETE FROM "+tablename+" WHERE time<?";
		jdbcTemplate.update(querySQL,lastDay);

	}
}
