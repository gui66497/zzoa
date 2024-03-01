package zzjz.mapper;

import org.springframework.stereotype.Repository;
import zzjz.bean.GroupDepart;
import zzjz.bean.GroupDepartRequest;

import java.util.List;


/**
 * @author 汪浩
 * @version 2017-10-16
 */
@Repository
public interface GroupDepartMapper {
    /**
     * 根据departId获取角色信息.
     *
     * @param departId departId
     * @return List
     */
    List<GroupDepart> getGroupListByDepartId(Long departId);

    /**
     * @param staffId staffId
     * @return List
     */
    List<GroupDepart> getGroupList(long staffId);

    /**
     * @param groupDepartRequest GroupDepartRequest
     * @return int
     */
    int insert(GroupDepartRequest groupDepartRequest);

    /**
     * @param departId departId
     * @return int
     */
    int deleteByDepartId(Long departId);

}
