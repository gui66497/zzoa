package zzjz.service.impl;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zzjz.bean.Catalog;
import zzjz.bean.Resource;
import zzjz.mapper.ResourceMapper;
import zzjz.service.CatalogService;
import zzjz.service.ResourceService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceServiceImpl.
 *
 * @author Administrator
 * @version 2017/8/28 13:51
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    CatalogService catalogService;

    @Autowired
    ResourceMapper resourceMapper;

    @Value("${resource.path}")
    String rootPath;

    final String separator = File.separator;

    @Override
    public Resource add(Resource resource) {
        int catalogId = resource.getBelong();
        Catalog catalog = catalogService.queryById(catalogId);

        // 计算出最终路径
        String cataPath = catalogService.recursion(catalog, new ArrayList<String>());
        String filePath = rootPath + separator + cataPath + separator + resource.getFileName();
        resource.setFileName(filePath);
        boolean res = resourceMapper.insertSelective(resource) > 0;
        if (res) {
            return resource;
        }
        return null;
    }

    @Override
    public boolean upload(String fileName, InputStream fileInputStream) throws IOException {
        File file = new File(fileName);
        FileUtils.copyInputStreamToFile(fileInputStream, file);
        return true;
    }

    @Override
    public boolean deleteById(Integer id) {
        Resource resource = resourceMapper.selectByPrimaryKey(id);
        File file = new File(resource.getFileName());
        if (file.exists()) {
            boolean res = file.delete();
            if (res) {
                return resourceMapper.deleteByPrimaryKey(id) > 0;
            }
        }
        return false;
    }

    @Override
    public Resource queryResourceById(Integer id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Resource> queryResByCatalogIdAndStaffId(int catalogId, String staffId) {
        return resourceMapper.queryResByCatalogId(catalogId, staffId);
    }

    @Override
    public boolean updateResource(Resource resource) {
        return resourceMapper.updateByPrimaryKeySelective(resource) > 0;
    }

    @Override
    public boolean updateResourceAll(Resource resource) {
        return resourceMapper.updateByPrimaryKeySelective(resource) > 0;
    }

    @Override
    public boolean moveResource(Resource resource, String targetPath) throws IOException {
        File origin = new File(resource.getFileName());
        File targetDic = new File(targetPath);
        if (origin.exists()) {
            if (targetDic.exists() && targetDic.isDirectory()) {
                File target =  new File(targetPath + File.separator + resource.getName());
                Files.move(origin, target);
            } else {
                LOGGER.info("目标文件夹不存在!");
            }
        } else {
            LOGGER.info("源文件不存在!");
            return false;
        }
        return true;
    }

    @Override
    public String renameResource(Resource resource) throws IOException {
        Resource originResource = queryResourceById(resource.getId());
        String originFileName = originResource.getFileName();
        File originFile = new File(originFileName);
        if (originFile.exists()) {
            String newName = resource.getName();
            String newFileName = originFileName.substring(0, originFileName.lastIndexOf(File.separator) + 1) + newName;
            File newFile = new File(newFileName);
            return originFile.renameTo(newFile) ? newFileName : null;
        } else {
            throw new IOException("原文件不存在!");
        }
    }
}
