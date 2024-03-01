package zzjz.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzjz.service.ZipService;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:31
 * @ClassName: ActivitiServiceImpl
 * @Description: 工作流实现类
 */
@Service
public class ZipServiceImpl implements ZipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipServiceImpl.class);

    private static final Integer INT2048 = 2048;

    private static final Integer INT128 = 128;

    @Override
    public boolean zip(String path, String fileName, InputStream inputStream) throws Exception {
        LOGGER.info("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path + File.separator + fileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        out.putNextEntry(new ZipEntry(fileName)); // 创建zip压缩进入点base

        BufferedInputStream bi = new BufferedInputStream(inputStream);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[INT2048];
        int bytesRead;
        while (-1 != (bytesRead = bi.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }

        LOGGER.info("加密中...");
        String key = "tKvmMft2ICniJANMLltDpA=="; //秘钥，随机生成
        byte[] bytIn = bos.toByteArray();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(INT128, new SecureRandom(key.getBytes("utf8")));
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        LOGGER.info("加密完成.");

        // 写文件
        byte[] bytOut = cipher.doFinal(bytIn);
        bo.write(bytOut); // 将字节流写入当前zip目录

        bi.close(); // 输入流关闭
        bos.close();
        bo.close();
        out.close();
        LOGGER.info("压缩完成");
        return true;
    }

    @Override
    public byte[] unZip(String path, String fileName) throws Exception {
        long startTime = System.currentTimeMillis();
        ZipInputStream zin = new ZipInputStream(new FileInputStream(path + File.separator + fileName)); //输入源zip路径
        BufferedInputStream bin = new BufferedInputStream(zin);
        LOGGER.info(fileName + "开始解压...");
        ZipEntry entry = zin.getNextEntry();
        File fout = new File(path + File.separator + "tmp", entry.getName());
        if (!fout.exists()) {
            boolean rs = (new File(fout.getParent())).mkdirs();
            LOGGER.info("创建文件夹:" + fout.getParent() + (rs == true ? "成功" : "失败"));
        }
        FileOutputStream out = new FileOutputStream(fout);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int b;
        while ((b = bin.read()) != -1) {
            bout.write(b);
        }
        bout.close();
        out.close();
        bin.close();
        zin.close();

        LOGGER.info(fileName + "解压成功");
        LOGGER.info(fileName + "开始解密...");
        String key = "tKvmMft2ICniJANMLltDpA==";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fout));
        byte[] bytIn = new byte[(int) fout.length()];
        bis.read(bytIn);
        bis.close();
        boolean rs = fout.delete();
        LOGGER.info("删除文件：" + fout + (rs == true ? "成功" : "失败"));

        // AES加密
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(INT128, new SecureRandom(key.getBytes("utf8")));
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        // 写文件
        byte[] bytOut = cipher.doFinal(bytIn);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(bytOut);
        LOGGER.info(fileName + "解密成功");
        long endTime = System.currentTimeMillis();
        LOGGER.info("耗费时间： " + (endTime - startTime) + " ms");
        return bos.toByteArray();
    }

    @Override
    public boolean delete(String path, String fileName) {
        File file = new File(path, fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 生成随机秘钥.
     *
     * @return byte
     * @throws Exception Exception
     */
    public static byte[] initKey() throws Exception {
        //实例化
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //设置密钥长度
        kgen.init(INT128);
        //生成密钥
        SecretKey skey = kgen.generateKey();
        //返回密钥的二进制编码
        return skey.getEncoded();
    }
}
