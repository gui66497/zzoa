package zzjz.util;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Office2PDF.
 *
 * @author fgt
 * @version 2017/7/12 10:02
 */
public class Office2PDF {
    private final static Logger LOGGER = LoggerFactory.getLogger(Office2PDF.class);

    /**
     * 根据操作系统的名称，获取OpenOffice.org 3的安装目录.
     * 如我的OpenOffice.org 3安装在：C:/Program Files (x86)/OpenOffice.org 3<br>
     *
     * @return OpenOffice.org 3的安装目录
     */
    private static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        LOGGER.info("操作系统名称:" + osName);
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice.org3";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "C:\\Program Files (x86)\\OpenOffice 4";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Application/OpenOffice.org.app/Contents";
        }
        return "";
    }

    /**
     * office转pdf.
     *
     * @param sourceFile 源文件
     * @param destFile   目标文件
     * @return 结果
     * @throws IOException IOException
     */
    public static boolean office2PDF(String sourceFile, String destFile) throws IOException {
        File inputFile = new File(sourceFile);
        if (!inputFile.exists()) {
            return false; // 找不到源文件, 则返回
        }

        // 如果目标文件已存在, 则直接返回
        File outputFile = new File(destFile);
        if (outputFile.exists()) {
            return true;
        }
        if (!outputFile.getParentFile().exists()) {
            boolean rs = outputFile.getParentFile().mkdirs();
            LOGGER.info("创建文件夹:" + outputFile.getParentFile() + (rs == true ? "成功" : "失败"));
        }

        OfficeManager officeManager = new DefaultOfficeManagerConfiguration()
                .setOfficeHome(getOfficeHome())
                .buildOfficeManager();
        officeManager.start();

        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(inputFile, outputFile);

        officeManager.stop();

        /*String OpenOffice_HOME = getOfficeHome();
        // 启动OpenOffice的服务
        String command = OpenOffice_HOME
                + "\\program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
        Process pro = Runtime.getRuntime().exec(command);

        // connect to an OpenOffice.org instance running on port 8100
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        connection.connect();

        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(inputFile, outputFile);

        // close the connection
        connection.disconnect();
        // 关闭OpenOffice服务的进程
        pro.destroy();*/
        return true;
    }

    /*public static void main(String[] args) {
        String sourceFile = "E:\\zzjz_work\\zzoa\\src\\main\\resources\\resume\\test.docx";
        String destFile = "E:\\zzjz_work\\zzoa\\src\\main\\resources\\resume\\test.pdf";
        try {
            boolean res = office2PDF(sourceFile, destFile);
            System.out.println("结果为：" + res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
