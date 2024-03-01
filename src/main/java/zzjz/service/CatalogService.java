package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Catalog;
import java.io.IOException;
import java.util.List;

/**
 * CatalogService.
 *
 * @author Administrator
 * @version 2017/8/28 13:59
 */
@Service
public interface CatalogService {

    /**
     * 根据Id获取目录信息.
     *
     * @param id id
     * @return 目录信息
     */
    Catalog queryById(int id);

    /**
     * 新增目录信息.
     *
     * @param catalog 目录信息
     * @return 结果
     */
    boolean add(Catalog catalog);

    /**
     * 获取指定.
     *
     * @param catalogId 目录ID
     * @return 列表
     */
    List<Catalog> queryCataByCatalogId(int catalogId);

    /**
     * 根据id删除目录信息.
     *
     * @param id id
     * @return 结果
     */
    boolean deleteById(Integer id) throws IOException;

    /**
     * 修改目录信息.
     *
     * @param catalog 目录信息
     * @return 结果
     */
    boolean updateCatalog(Catalog catalog);

    /**
     * 递归获取目录路径.
     *
     * @param catalog 目录信息
     * @param res     返回值
     * @return 路径
     */
    String recursion(Catalog catalog, List<String> res);

    /**
     * 获取所有目录信息.
     * @return 目录信息
     */
    List<Catalog> getAllCatalog();
}
