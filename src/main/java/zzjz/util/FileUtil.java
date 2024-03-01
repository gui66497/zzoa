package zzjz.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类.
 *
 * @author 梅宏振
 * @version 2017-10-19
 */
public class FileUtil {

    /**
     * 以行的方式读取文件.
     *
     * @param path path
     * @return String
     */
    public String readLineFromFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return "";
        }
        StringBuffer value = new StringBuffer("");
        String tempString = "";
        BufferedReader reader;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            while ((tempString = reader.readLine()) != null) {
                value.append(tempString);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value.toString();
    }

    /**
     * @param path path
     * @return List
     * @Title: getBackupfileNames
     * @Description: 获取备份文件名称
     */
    public static List<String> getFileNames(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File sourceFile = new File(path);
        if (!sourceFile.exists()) {
            return null;
        }
        List<String> backupFileNameList = new ArrayList<String>();
        File[] files = sourceFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName != null && fileName.toLowerCase().endsWith(".sql")) {
                    backupFileNameList.add(fileName);
                }
            }
        }
        return backupFileNameList;
    }

    /*public static void main(String args[]) {
        String path = "D:\\sql文件";
        List<String> fileNames = FileUtil.getFileNames(path);
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }*/

}
