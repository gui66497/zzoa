package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class User {
    private Long id;        // ID

    private String userId;        // 用户ID

    private String userName;    // 用户名

    private String unitId;        // 单位ID

    private String unitName;    // 单位名称

    private String roleId;        //角色ID(可存储多个，以逗号相隔)

    private String roleName;    //角色名称(可存储多个，以逗号相隔)

    private String createTime;    //创建时间

    private String password;    //密码

    private String systemId;    //系统id

    /**
     * getSystemId.
     *
     * @return SYSTEM_ID
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * setSystemId.
     *
     * @param systemId SYSTEM_ID
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * getPassword.
     *
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword.
     *
     * @param password PASSWORD
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getCreateTime.
     *
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime.
     *
     * @param createTime CREATE_TIME
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * getRoleName.
     *
     * @return ROLE_NAME
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * setRoleName.
     *
     * @param roleName ROLE_NAME
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * getRoleId.
     *
     * @return ROLE_ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * setRoleId.
     *
     * @param roleId ROLE_ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * getUnitName.
     *
     * @return UNIT_NAME
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * setUnitName.
     *
     * @param unitName UNIT_NAME
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * getUserId.
     *
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setUserId.
     *
     * @param userId USER_ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getUserName.
     *
     * @return USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName.
     *
     * @param userName USER_NAME
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getUnitId.
     *
     * @return UNIT_ID
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * setUnitId.
     *
     * @param unitId UNIT_ID
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * getId.
     *
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }
}