package zzjz.service;

import zzjz.bean.DepartRole;
import zzjz.bean.GroupDepartInfo;
import zzjz.bean.GroupDepartRequest;

import java.util.List;

/**
 * @author 汪浩
 * @version 2017/5/15
 */
public interface GroupDepartService {

    /**
     * 获取部门组信息列表.
     *
     * @param departId departId
     * @return GroupDepartInfo
     */
    GroupDepartInfo getGroupListByDepartId(long departId);

    /**
     * 根据用户ID获取该用户的角色list.
     *
     * @param staffId staffId
     * @return List
     */
    List<DepartRole> getGroupList(String staffId);

    /**
     * 新增部门组角色.
     *
     * @param groupDepartRequest GroupDepartRequest
     * @return boolean
     */
    boolean addGroupDepart(GroupDepartRequest groupDepartRequest);

    //boolean updateGroupDepart(GroupDepartRequest roupDepartRequest);

}
