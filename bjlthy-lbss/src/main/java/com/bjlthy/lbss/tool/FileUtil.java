package com.bjlthy.lbss.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 文件操作流工具类
 * @date 2021年6月15日 下午4:54:57
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class FileUtil {

	/**
	 * 
	 * @author 张宁
	 * @description 保存数据写入文件
	 * @param path 文件路径
	 * @param value 数据
	 * @date 2020年11月16日 下午4:06:16
	 */
	public static void writeFile(String path,String value) {
		FileWriter fw = null;
		try {
			File f=new File(path);
			//不存在自动创建文件夹
			File fileParent = f.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			f.setWritable(true, false);
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(value);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入文件加时间
	 * @param path
	 * @param value
	 */
	public static void writeFileAndTime(String path,String value) {
		FileWriter fw = null;
		String date = DateUtil.getDateTimeMS();
		try {
		//如果文件存在，则追加内容；如果文件不存在，则创建文件
			//保存文件
			File file=new File(path);
			//不存在自动创建文件夹
			File fileParent = file.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			fw = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(date+" "+value);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author 张宁
	 * @description 迭代删除文件夹
	 * @param 参数
	 * @date 2020年11月16日 下午4:07:17
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
	/**
	 * 
	 * @author 张宁
	 * @description 获取目录下所有文件按时间排序
	 * @param 参数
	 * @date 2020年11月16日 下午4:07:26
	 */
	public static List<File> getFileSort(String path) {

		List<File> list = getFiles(path, new ArrayList<File>());

		if (list != null && list.size() > 0) {

			Collections.sort(list);
		}

		return list;
	}
	/**
	 * 
	 * @author 张宁
	 * @description 获取目录下所有文件
	 * @param 参数
	 * @date 2020年11月16日 下午4:07:35
	 */
	public static List<File> getFiles(String realpath, List<File> files) {

		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				if (file.isDirectory()) {
					//getFiles(file.getAbsolutePath(), files);
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 *
	 * @author 张宁
	 * @description 获取目录下所有文件夹按时间排序
	 * @param 参数
	 * @date 2020年11月16日 下午4:07:26
	 */
	public static List<File> getFilesSort(String path) {

		List<File> list = getDirectory(path, new ArrayList<File>());

		if (list != null && list.size() > 0) {

			Collections.sort(list);
		}

		return list;
	}

	/**
	 *
	 * @author 张宁
	 * @description 获取目录下所有文件夹
	 * @param 参数
	 * @date 2020年11月16日 下午4:07:35
	 */
	public static List<File> getDirectory(String realpath, List<File> files) {

		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				if (file.isDirectory()) {
					files.add(file);
				}
			}
		}
		return files;
	}

}
