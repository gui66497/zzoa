package zzjz.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author 彭鹏
 * @version 2017年08月29日11:08
 * @ClassName: ZipService
 * @Description: 文件压缩解压服务接口
 */
@Service
public interface ZipService {

    /**
     * 压缩加密文件.
     *
     * @param path        path
     * @param fileName    fileName
     * @param inputStream inputStream
     * @return boolean
     * @throws Exception Exception
     */
    boolean zip(String path, String fileName, InputStream inputStream) throws Exception;

    /**
     * 解压解密文件.
     *
     * @param path     path
     * @param fileName fileName
     * @return byte
     * @throws Exception Exception
     */
    byte[] unZip(String path, String fileName) throws Exception;

    /**
     * 删除文件.
     *
     * @param path     path
     * @param fileName fileName
     * @return boolean
     */
    boolean delete(String path, String fileName);
}
