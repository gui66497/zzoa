package zzjz.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * @author 梅宏振
 *
 */
public class FileUtil {

	/**
	 * 以行的方式读取文件
	 * @return
	 */
	public String readLineFromFile(String path) {
		File file = new File(path);
		if(!file.isFile()) {
			return "";
		}
		String value = "";
		String tempString = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((tempString = reader.readLine()) != null) {
				value = value + tempString;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return value;
	}

	/**
	 * @Title: getBackupfileNames
	 * @Description: 获取备份文件名称
	 */
	public static List<String> getFileNames(String path) {
		if(StringUtils.isBlank(path)) {
			return null;
		}
		File sourceFile = new File(path);
		if(!sourceFile.exists()) {
			return null;
		}
		List<String> backupFileNameList = new ArrayList<String>();
		File[] files = sourceFile.listFiles();
		if(files!=null && files.length > 0) {
			for (File file : files) {
				String fileName = file.getName();
				if (fileName != null && fileName.toLowerCase().endsWith(".sql")) {
					backupFileNameList.add(fileName);
				}
			}
		}
		return backupFileNameList;
	}

	public static void main(String args[]) {
		String path = "D:\\sql文件";
		List<String> fileNames = FileUtil.getFileNames(path);
		for(String fileName : fileNames) {
			System.out.println(fileName);
		}
	}
	
}
