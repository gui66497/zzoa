package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    private Long id;		// ID

    private String userId;		// 用户ID

    private String userName;	// 用户名

    private String unitId;		// 单位ID

    private String unitName;	// 单位名称

    private String roleId;		//角色ID(可存储多个，以逗号相隔)

    private String roleName;	//角色名称(可存储多个，以逗号相隔)

    private String createTime;	//创建时间

    private String password;	//密码

    private String systemId;	//系统id

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}