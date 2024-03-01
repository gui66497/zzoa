package zzjz.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Util.
 *
 * @author Administrator
 * @version 2017/9/7 16:50
 */
public class MD5Util {

    private static final int INT16 = 16;

    private static final int INT32 = 32;

    /**
     * md5.
     * @param plainText plainText
     * @return String
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes("utf8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        } catch (UnsupportedEncodingException ee) {
            throw new RuntimeException("没有md5这个编码！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(INT16); // 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < INT32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 获取文件的md5值.
     * @param path 路径
     * @return md5
     * @throws IOException IOException
     */
    public static String getFileMd5(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("文件" + path + "不存在");
        }
        FileInputStream fis = new FileInputStream(path);
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        IOUtils.closeQuietly(fis);
        return md5;
    }

    /*public static void main(String[] args) {
        //System.out.println(md5("haohao123"));
        String path = "C:\\Users\\Administrator\\Desktop\\房桂堂_外出594_17221426_export.pdf";
        try {
            getFileMd5(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    //lixudong wzsy4102
    //liuying 930606
    //renjingru Rjr19911114
    //wangjunjie wjj123
    //hushenming hsm1967118
    //donganmin 132249
    //sunxuelian sxl123
}
