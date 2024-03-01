package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class RoleInfo {

    private long id;

    private String name;

    private String sign;

    private String creator;

    private String createTime;

    private String remark = "";

    private int deleteFlag;

    private long systemId;

    private String systemName;

    private String creatorName = "";

    private List<PermissionInfo> permissionInfoList = new ArrayList<PermissionInfo>();

    /**
     * getCreatorName.
     *
     * @return CREATOR_NAME
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * setCreatorName.
     *
     * @param creatorName CREATOR_NAME
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * getSystemName.
     *
     * @return SYSTEM_NAME
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * setSystemName.
     *
     * @param systemName SYSTEM_NAME
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * getPermissionInfoList.
     *
     * @return PERMISSION_INFO_LIST
     */
    public List<PermissionInfo> getPermissionInfoList() {
        return permissionInfoList;
    }

    /**
     * setPermissionInfoList.
     *
     * @param permissionInfoList PERMISSION_INFO_LIST
     */
    public void setPermissionInfoList(List<PermissionInfo> permissionInfoList) {
        this.permissionInfoList = permissionInfoList;
    }

    /**
     * getId.
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName.
     *
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param name NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getSign.
     *
     * @return SIGN
     */
    public String getSign() {
        return sign;
    }

    /**
     * setSign.
     *
     * @param sign SIGN
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * getCreator.
     *
     * @return CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * setCreator.
     *
     * @param creator CREATOR
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * getRemark.
     *
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * setRemark.
     *
     * @param remark REMARK
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * getDeleteFlag.
     *
     * @return DELETE_FLAG
     */
    public int getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * setDeleteFlag.
     *
     * @param deleteFlag DELETE_FLAG
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
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
