package zzjz.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zzjz.bean.Catalog;
import zzjz.mapper.CatalogMapper;
import zzjz.service.CatalogService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CatalogServiceImpl.
 *
 * @author Administrator
 * @version 2017/8/28 14:00
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);

    @Autowired
    CatalogMapper catalogMapper;

    @Value("${resource.path}")
    String rootPath;

    @Override
    public Catalog queryById(int id) {
        return catalogMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean add(Catalog catalog) {
        //先创建文件夹 在执行数据库操作
        String cataPath = recursion(catalog, new ArrayList<String>());
        String path = rootPath + File.separator + cataPath;
        File file = new File(path);
        boolean res = false;
        if (!file.exists()) {
            res = file.mkdirs();
        }
        if (res) {
            return catalogMapper.insertSelective(catalog) > 0;
        }
        return false;
    }

    /**
     * 递归获取目录层次结构 如abc/def/ghi.
     *
     * @param catalog 目录信息
     * @param res     返回值
     * @return 录层次结构
     */
    @Override
    public String recursion(Catalog catalog, List<String> res) {
        res.add(catalog.getName());
        if (catalog.getpId() != -1) {
            Catalog pCata = queryById(catalog.getpId());
            recursion(pCata, res);
        } else {
            Collections.reverse(res);
            return StringUtils.join(res, File.separator);
        }
        return StringUtils.join(res, File.separator);
    }

    @Override
    public List<Catalog> getAllCatalog() {
        return catalogMapper.getAllCatalog();
    }

    @Override
    public List<Catalog> queryCataByCatalogId(int catalogId) {
        return catalogMapper.queryCataByCatalogId(catalogId);
    }

    @Override
    public boolean deleteById(Integer id) throws IOException {
        //先删除目录文件
        Catalog catalog = queryById(id);
        String path = rootPath + File.separator + recursion(catalog, new ArrayList<String>());
        File file = new File(path);
        if (file.exists()) {
            FileUtils.deleteDirectory(file);
        }
        return catalogMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean updateCatalog(Catalog catalog) {
        Catalog oldCatalog = queryById(catalog.getId());
        //名字修改了
        if (!oldCatalog.getName().equals(catalog.getName())) {
            String oldPath = rootPath + File.separator + recursion(oldCatalog, new ArrayList<String>());
            File oldFile = new File(oldPath);
            if (!oldFile.exists()) {
                LOGGER.error(oldPath + " 该旧文件不存在！");
                return false;
            }
            String newPath = oldFile.getParent() + File.separator + catalog.getName();
            File newFile = new File(newPath);
            boolean res = oldFile.renameTo(newFile);
            if (!res) {
                LOGGER.error("改名失败！");
                return false;
            }
        }
        return catalogMapper.updateByPrimaryKeySelective(catalog) > 0;
    }
}
