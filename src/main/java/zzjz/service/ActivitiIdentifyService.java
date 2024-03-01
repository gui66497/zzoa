package zzjz.service;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Staff;
import zzjz.mapper.StaffMapper;

/**
 * ActivitiIdentifyService.
 *
 * @author fgt
 * @version 2017/5/8 9:02
 */
@Service
public class ActivitiIdentifyService {

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    IdentityService identityService;

    /**
     * 添加user.
     *
     * @param staff 员工信息
     */
    public void addUser(Staff staff) {
        String userId = staff.getUserName();
        User userTemp = identityService.createUserQuery().userId(userId).singleResult();
        if (userTemp != null) {
            return;
        }
        User user = identityService.newUser(userId);
        user.setFirstName(staff.getName());
        user.setPassword(staff.getPassword());
        identityService.saveUser(user);
    }

    /**
     * 删除用户和组的关系.
     */
    public void deleteAllMemberShip() {
        staffMapper.deleteAllMemberShip();
    }

    /**
     * 删除所有角色（组）.
     */
    public void deleteAllRole() {
        staffMapper.deleteAllRole();
    }

    /**
     * 删除所有用户.
     */
    public void deleteAllUser() {
        staffMapper.deleteAllUser();
    }
}
