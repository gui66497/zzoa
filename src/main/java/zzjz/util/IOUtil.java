package zzjz.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @ClassName: FileUploadUtil
 * @Description: 文件上传相关操作工具类
 * @author 梅宏振
 * @date 2015年3月12日 上午8:27:35
 */
public class IOUtil {

	// 文件访问路径

	public static final String FILE_UPLOAD_PATH = "http://120.3.28.117:8081/examples/uploadfile/";

	// 文件存储路径
	public static final String FILE_UPLOAD_LOCAL_PATH = "D:/apache-tomcat-file-server/webapps/examples/uploadfile/";
	private static Logger LOGGER = Logger.getLogger(IOUtil.class);

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @Title: saveFile
	 * @Description: 将输入流存储保存到指定服务器上的文件
	 * @param uploadedInputStream
	 * @param serverLocation
	 * @return
	 * @throws
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

	public static void main(String[] args) throws IOException {
		//write("fsdafas", "C:\\Users\\GX\\Desktop\\新建文件夹 (2)\\新建文本文档 (4).txt", true);
		delete("C:\\Users\\GX\\Desktop\\新建文件夹 (2)");
	}

	/**根据文件路径删除文件或者文件夹以及文件夹下的子文件
	 * @param path
	 */
	public static void delete(String path) {
		File file = new File(path);
		delete(file);
	}
	
	public static void delete(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files.length > 0){
				for (File tempFile : files) {
					delete(tempFile);
				}
			}else{
				file.delete();
			}
		}else{
			file.delete();
		}
	}

	/**将字符串写入到为文件中
	 * @param value
	 * @param path
	 * @param append
	 * @throws IOException 
	 */
	public static void write(String value, String path, boolean append) throws IOException {
		File file = new File(path);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		PrintWriter out = new PrintWriter(new FileOutputStream(path, append));
		out.println(value);
		out.flush();
		out.close();
	}
	
}
