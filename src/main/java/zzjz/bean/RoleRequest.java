package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class RoleRequest {

    private long systemId;

    private PagingEntity paging;

    private String roleId;

    private String permissionId;

    private String dataSource;

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
     * getPermissionId.
     *
     * @return PERMISSION_ID
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * setPermissionId.
     *
     * @param permissionId PERMISSION_ID
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * getPaging.
     *
     * @return PAGING
     */
    public PagingEntity getPaging() {
        return paging;
    }

    /**
     * setPaging.
     *
     * @param paging PAGING
     */
    public void setPaging(PagingEntity paging) {
        this.paging = paging;
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

}
