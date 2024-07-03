package com.bjlthy.lbss.data.weight.service;

/**
 * 定时任务Service接口
 * 
 * @author bjlthy
 * @date 2020-12-04
 */
public interface ILbssJobService 
{
  
	/**
	 * 
	 * @author 张宁
	 * @description 查询小时皮带机信息
	 * @date 2021年3月1日 下午9:28:37
	 */
	public void sumHourBeltInfo() throws Exception;

	/**
	 *
	 * @author 张宁
	 * @description 查询小时皮带机信息
	 * @date 2021年3月1日 下午9:28:37
	 */
	public void sumDayBeltInfo() throws Exception;
	
	/**
	 * 
	 * @author 张宁
	 * @Description: 连接FTP下载数据
	 * @return void    返回类型
	 * @date 2022年9月8日 上午11:14:01
	 * @throws
	 */
	public void downloadFileToFTP() throws Exception;

	/**
	 * 读取文件更新数据库
	 * @param filePath
	 */
	public void readFileUpdateDB(String filePath,String beltName);

	


}
