package zzjz.mapper;

import org.springframework.stereotype.Repository;
import zzjz.bean.ActIdGroup;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017-10-16
 */
@Repository
public interface ActIdGroupMapper {
    /**
     * @return List
     */
    List<ActIdGroup> getActIdGroupList();
}