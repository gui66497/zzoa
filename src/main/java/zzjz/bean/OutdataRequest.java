package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author guzhenggen
 * @version 2016/7/28 14:10
 * @ClassName: OutdataRequest
 * @Description: 外部数据request
 */
@XmlRootElement
public class OutdataRequest {

    /**
     * 系统ID
     */
    private long systemId;

    /**
     * 权限ID
     */
    private long authorityId;

    /**
     * 角色ID
     */
    private long roleId;

    /**
     * 外部数据ID
     */
    private long targetId;

    /**
     * 外部数据数据源
     */
    private String dataSource;

    /**
     * 外部数据名称
     */
    private String targetName;

    /**
     * 用户ID
     */
    private long userId;

    /**
     * 系统标识
     */
    private String systemSign;

    /**
     * 权限标识
     */
    private String authoritySign;

    /**
     * 角色标识
     */
    private String roleSign;

    /**
     * 用户ID列表
     */
    private List<Long> userIdList;

    /**
     * getAuthorityId.
     *
     * @return AUTHORITY_ID
     */
    public long getAuthorityId() {
        return authorityId;
    }

    /**
     * setAuthorityId.
     *
     * @param authorityId AUTHORITY_ID
     */
    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    /**
     * getDataSource.
     *
     * @return DATA_SOURCE
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * setDataSource.
     *
     * @param dataSource DATA_SOURCE
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * getRoleId.
     *
     * @return ROLE_ID
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * setRoleId.
     *
     * @param roleId ROLE_ID
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * getSystemId.
     *
     * @return SYSTEM_ID
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * setSystemId.
     *
     * @param systemId SYSTEM_ID
     */
    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    /**
     * getTargetId.
     *
     * @return TARGET_ID
     */
    public long getTargetId() {
        return targetId;
    }

    /**
     * setTargetId.
     *
     * @param targetId TARGET_ID
     */
    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    /**
     * getTargetName.
     *
     * @return TARGET_NAME
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * setTargetName.
     *
     * @param targetName TARGET_NAME
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * getUserId.
     *
     * @return USER_ID
     */
    public long getUserId() {
        return userId;
    }

    /**
     * setUserId.
     *
     * @param userId USER_ID
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * getAuthoritySign.
     *
     * @return AUTHORITY_SIGN
     */
    public String getAuthoritySign() {
        return authoritySign;
    }

    /**
     * setAuthoritySign.
     *
     * @param authoritySign AUTHORITY_SIGN
     */
    public void setAuthoritySign(String authoritySign) {
        this.authoritySign = authoritySign;
    }

    /**
     * getRoleSign.
     *
     * @return ROLE_SIGN
     */
    public String getRoleSign() {
        return roleSign;
    }

    /**
     * setRoleSign.
     *
     * @param roleSign ROLE_SIGN
     */
    public void setRoleSign(String roleSign) {
        this.roleSign = roleSign;
    }

    /**
     * getSystemSign.
     *
     * @return SYSTEM_SIGN
     */
    public String getSystemSign() {
        return systemSign;
    }

    /**
     * setSystemSign.
     *
     * @param systemSign SYSTEM_SIGN
     */
    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }

    /**
     * getUserIdList.
     *
     * @return USER_ID_LIST
     */
    public List<Long> getUserIdList() {
        return userIdList;
    }

    /**
     * setUserIdList.
     *
     * @param userIdList USER_ID_LIST
     */
    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
