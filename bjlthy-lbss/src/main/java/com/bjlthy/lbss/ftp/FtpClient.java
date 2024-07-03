package com.bjlthy.lbss.ftp;

import ch.qos.logback.classic.Logger;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FtpClient {
	
	//日志工具类
	private static Logger logger = LogbackUtil.getLogger("FtpClient", "FtpClient");
	private static FTPClient ftp;
	
	/**
	 * 获取ftp连接
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static boolean connectFtp(Ftp f) {
		ftp=new FTPClient();
		boolean flag=false;
		int reply;
		try {
			if (f.getPort()==null) {
				ftp.connect(f.getIpAddr(),21);
			}else{
				ftp.connect(f.getIpAddr(),f.getPort());
			}
			ftp.login(f.getUserName(), f.getPwd());
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return flag;
			}
			ftp.changeWorkingDirectory(f.getPath());
			flag = true;
		}catch (Exception e){
			logger.error(f.getIpAddr()+"连接ftp服务失败", e);
		}
	    return flag;
	}
	
	/**
	 *关闭ftp连接
	 */
	public static void closeFtp(){
		if (ftp!=null && ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				logger.error("closeFtp,{}",e);
			}
		}
	}
	
	/**
	 * ftp上传文件
	 * @param f
	 * @throws Exception
	 */
	public static void upload(File f) throws Exception{
		if (f.isDirectory()) {
			ftp.makeDirectory(f.getName());
			ftp.changeWorkingDirectory(f.getName());
			String[] files=f.list();
			for(String fstr : files){
				File file1=new File(f.getPath()+"/"+fstr);
				if (file1.isDirectory()) {
					upload(file1);
					ftp.changeToParentDirectory();
				}else{
					File file2=new File(f.getPath()+"/"+fstr);
					FileInputStream input=new FileInputStream(file2);
					ftp.storeFile(file2.getName(),input);
					input.close();
				}
			}
		}else{
			File file2=new File(f.getPath());
			FileInputStream input=new FileInputStream(file2);
			ftp.storeFile(file2.getName(),input);
			input.close();
		}
	}

	/**
	 * 下载链接配置
	 * @param f
	 * @param localBaseDir 本地目录
	 * @param remoteBaseDir 远程目录
	 * @throws Exception
	 */
	public static void startDown(Ftp f,String localBaseDir,String remoteBaseDir ) throws Exception{
		if (FtpClient.connectFtp(f)) {
	        try {
	            FTPFile[] files = null;
	            boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir); 
	            if (changedir) { 
	                ftp.setControlEncoding("UTF-8");
	                files = ftp.listFiles(); 
	                for (int i = 0; i < files.length; i++) { 
	                    try{ 
	                        downloadFile(files[i], localBaseDir, remoteBaseDir); 
	                        deleteFile(remoteBaseDir,files[i].getName());
	                    }catch(Exception e){ 
	                    	logger.error(f.getIpAddr()+"<"+files[i].getName()+">下载失败,{}",e);
	                    } 
	                }
					closeFtp();
	            } 
	        } catch (Exception e) { 
	        	logger.error(f.getIpAddr()+"下载过程中出现异常,{}",e);
	        } 
		}else{
			logger.error(f.getIpAddr()+"链接失败！");
		}
		
	}

	/**
	 * 下载链接配置
	 * @param f
	 * @param localBaseDir 本地目录
	 * @param remoteBaseDir 远程目录
	 * @throws Exception
	 */
	public static void startDownEWS(Ftp f,String localBaseDir,String remoteBaseDir ) throws Exception{
		if (FtpClient.connectFtp(f)) {
			try {
				FTPFile[] files = null;
				boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir);
				if (changedir) {
					ftp.setControlEncoding("UTF-8");
					files = ftp.listFiles();
					for (int i = 0; i < files.length; i++) {
						try{
							localBaseDir = localBaseDir+files[i].getName().substring(0,10)+"/";
							downloadEWSFile(files[i], localBaseDir, remoteBaseDir);
							deleteFile(remoteBaseDir,files[i].getName());
						}catch(Exception e){
							logger.error(f.getIpAddr()+"<"+files[i].getName()+">下载失败,{}",e);
						}
					}
					closeFtp();
				}
			} catch (Exception e) {
				logger.error(f.getIpAddr()+"下载过程中出现异常,{}",e);
			}
		}else{
			logger.error(f.getIpAddr()+"链接失败！");
		}

	}

	/**
	 *
	 * 下载FTP文件
	 * 当你需要下载FTP文件的时候，调用此方法
	 * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
	 *
	 * @param ftpFile
	 * @param relativeLocalPath
	 * @param relativeRemotePath
	 */
	private  static void downloadFile(FTPFile ftpFile, String relativeLocalPath,String relativeRemotePath) {
		OutputStream outputStream = null;
		try {
			File locaFile= new File(relativeLocalPath+ ftpFile.getName());
			//判断文件是否存在，存在则返回
			if(locaFile.exists()){
				return;
			}else{
				File f=new File(relativeLocalPath+ ftpFile.getName());
				//不存在自动创建文件夹
				File fileParent = f.getParentFile();
				if(!fileParent.exists()){
					fileParent.mkdirs();
				}
				outputStream = new FileOutputStream(relativeLocalPath+ ftpFile.getName());
				ftp.retrieveFile(ftpFile.getName(), outputStream);
                outputStream.flush();
				outputStream.close();
			}
		} catch (Exception e) {
			logger.error("downloadFile,{}",e);
		}
	}


	/**
	 *
	 * 下载FTP文件
	 * 当你需要下载FTP文件的时候，调用此方法
	 * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
	 *
	 * @param ftpFile
	 * @param relativeLocalPath
	 * @param relativeRemotePath
	 */
	private  static void downloadEWSFile(FTPFile ftpFile, String relativeLocalPath,String relativeRemotePath) {
		OutputStream outputStream = null;
		try {
			File locaFile= new File(relativeLocalPath+ ftpFile.getName());
			//判断文件是否存在，存在则返回
			if(locaFile.exists()){
				return;
			}else{
				File f=new File(relativeLocalPath+ ftpFile.getName());
				//不存在自动创建文件夹
				File fileParent = f.getParentFile();
				if(!fileParent.exists()){
					fileParent.mkdirs();
				}
				String originalFilename = ftpFile.getName();
				String filename;
				if (originalFilename.endsWith(".txt")) {
					filename = originalFilename.substring(0, originalFilename.length() - 4) + "_belt.txt";
					if (filename.contains("error")){
						return;
					}
				} else {
					// 文件名不是以 .txt 结尾，你可以决定是否要进行其他处理
					filename = originalFilename;
				}
				String ewsPath = relativeLocalPath+ filename;
				if(Files.exists(Paths.get(ewsPath))){
					ewsPath = ewsPath.replaceAll("_belt","_belt2");
				}
				outputStream = new FileOutputStream(ewsPath);
				ftp.retrieveFile(ftpFile.getName(), outputStream);
				outputStream.flush();
				outputStream.close();
			}
		} catch (Exception e) {
			logger.error("downloadFile,{}",e);
		}
	}

	/** * 删除文件 *

	 * @param pathname FTP服务器保存目录 *

	 * @param filename 要删除的文件名称 *

	 * @return */

	public static boolean deleteFile(String pathname, String filename) {
		boolean flag = false;
		try {
			// 切换FTP目录
			ftp.changeWorkingDirectory(pathname);

			ftp.dele(filename);

			flag = true;
		} catch (Exception e) {
			logger.error("deleteFile 删除的文件失败,{}",e);
		}
		return flag;
	}

}
	
