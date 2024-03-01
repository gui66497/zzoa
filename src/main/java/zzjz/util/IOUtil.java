package zzjz.util;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;


/**
 * @author 梅宏振
 * @version 2015年3月12日 上午8:27:35
 * @ClassName: FileUploadUtil
 * @Description: 文件上传相关操作工具类
 */
public class IOUtil {

    /**
     * 文件访问路径.
     */
    public static final String FILE_UPLOAD_PATH = "http://120.3.28.117:8081/examples/uploadfile/";

    /**
     * 文件存储路径.
     */
    public static final String FILE_UPLOAD_LOCAL_PATH = "D:/apache-tomcat-file-server/webapps/examples/uploadfile/";

    private static final Logger LOGGER = Logger.getLogger(IOUtil.class);

    /**
     * @param uploadedInputStream uploadedInputStream
     * @param path                path
     * @throws IOException IOException
     * @Title: saveFile
     * @Description: 将输入流存储保存到指定服务器上的文件
     */
    public static void saveFile(InputStream uploadedInputStream, String path)
            throws IOException {
        LOGGER.debug("文件上传路径:" + path);
        BufferedInputStream bis = new BufferedInputStream(uploadedInputStream);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(path)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /*public static void main(String[] args) throws IOException {
        //write("fsdafas", "C:\\Users\\GX\\Desktop\\新建文件夹 (2)\\新建文本文档 (4).txt", true);
        delete("C:\\Users\\GX\\Desktop\\新建文件夹 (2)");
    }*/

    /**
     * 根据文件路径删除文件或者文件夹以及文件夹下的子文件.
     *
     * @param path path
     */
    public static void delete(String path) {
        File file = new File(path);
        delete(file);
    }

    /**
     * @param file file
     */
    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File tempFile : files) {
                    delete(tempFile);
                }
            } else {
                boolean rs = file.delete();
                LOGGER.info("删除文件夹:" + file + (rs == true ? "成功" : "失败"));
            }
        } else {
            boolean rs = file.delete();
            LOGGER.info("删除文件夹:" + file + (rs == true ? "成功" : "失败"));
        }
    }

    /**
     * 将字符串写入到为文件中.
     *
     * @param value  value
     * @param path   path
     * @param append append
     * @throws IOException IOException
     */
    public static void write(String value, String path, boolean append) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            boolean rs = file.getParentFile().mkdirs();
            LOGGER.info("创建文件夹:" + file.getParentFile() + (rs == true ? "成功" : "失败"));
            rs = file.createNewFile();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, append), "utf-8"));
        out.println(value);
        out.flush();
        out.close();
    }

}
