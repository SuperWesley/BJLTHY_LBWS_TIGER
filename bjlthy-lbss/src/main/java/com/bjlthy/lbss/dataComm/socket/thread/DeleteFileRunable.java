package com.bjlthy.lbss.dataComm.socket.thread;


import com.bjlthy.lbss.tool.FileUtil;

import java.io.File;
/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 删除文件
 * @date 2020年11月16日 下午4:56:16
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class DeleteFileRunable implements Runnable{
	
	/**
	 * 文件路径
	 */
	private String path;
	
	public DeleteFileRunable(String path){
		this.path = path;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			File file = new File(path);
			FileUtil.deleteDir(file.getPath());
			System.out.println("删除文件夹成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author 张宁
	 * @description 迭代删除文件夹
	 * @param dirPath
	 * @date 2020年11月16日 下午4:56:47
	 */
	public static void deleteDir(String dirPath)
	{
		File file = new File(dirPath);
		if(file.isFile())
		{
			file.delete();
		}else
		{
			File[] files = file.listFiles();
			if(files == null)
			{
				file.delete();
			}else
			{
				for (int i = 0; i < files.length; i++) 
				{
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
	}
}
