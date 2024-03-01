package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Bean;

import java.io.File;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年08月29日11:08
 * @ClassName: ZipService
 * @Description: 备份服务接口
 */
@Service
public interface BackUpService {
    /**
     * @param backFile backFile
     * @param file     file
     * @return boolean
     */
    boolean backup(String backFile, File file);

    /**
     * mysql数据库备份.
     *
     * @param mysqlPath     数据库驱动路径
     * @param mysqlBackPath 数据库备份路径
     * @param jdbcUsername  数据库用户名
     * @param jdbcPassword  数据库密码
     * @return 结果
     */
    boolean mysqlBackUp(String mysqlPath, String mysqlBackPath, String jdbcUsername, String jdbcPassword);

    /**
     * mysql数据库还原.
     *
     * @param fileName      文件名
     * @param mysqlPath     数据库驱动路径
     * @param mysqlBackPath 数据库备份路径
     * @param jdbcUsername  数据库用户名
     * @param jdbcPassword  数据库密码
     * @return 结果
     */
    boolean mysqlReduce(String fileName, String mysqlPath, String mysqlBackPath, String jdbcUsername, String jdbcPassword);

    /**
     * 获取文件列表.
     *
     * @param mysqlBackPath 数据库备份路径
     * @return 列表
     */
    List<Bean> getFileList(String mysqlBackPath);
}
