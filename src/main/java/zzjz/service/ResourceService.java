package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ResourceService.
 *
 * @author Administrator
 * @version 2017/8/28 13:48
 */
@Service
public interface ResourceService {

    /**
     * 添加资源文件.
     *
     * @param resource 资源信息
     * @return 结果
     */
    Resource add(Resource resource);

    /**
     * @param fileName        文件名
     * @param fileInputStream 文件输入流
     * @return 结果
     * @throws IOException IOException
     */
    boolean upload(String fileName, InputStream fileInputStream) throws IOException;

    /**
     * 根据id删除资源信息.
     *
     * @param id id
     * @return 结果
     */
    boolean deleteById(Integer id);

    /**
     * 根据id获取资源信息.
     *
     * @param id id
     * @return 资源信息
     */
    Resource queryResourceById(Integer id);

    /**
     * 获取目录下的所有资源文件.
     *
     * @param catalogId 目录id
     * @param staffId   用户ID
     * @return 资源信息
     */
    List<Resource> queryResByCatalogIdAndStaffId(int catalogId, String staffId);

    /**
     * 修改资源信息(只修改文件名).
     * @param resource 资源实体
     * @return 结果
     */
    boolean updateResource(Resource resource);

    /**
     * 修改资源信息.
     * @param resource 资源实体
     * @return 结果
     */
    boolean updateResourceAll(Resource resource);

    /**
     * 移动资源文件.
     * @param resource 原文件
     * @param targetPath 目标目录路径
     * @return 结果
     * @throws IOException IOException
     */
    boolean moveResource(Resource resource, String targetPath) throws IOException;

    /**
     * 重命名资源文件.
     * @param resource  资源文件
     * @return 新文件的路径
     * @throws IOException IOException
     */
    String renameResource(Resource resource) throws IOException;
}
