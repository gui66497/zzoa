package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class PermissionAllotInfo {

    /**
     * 角色ID
     */
    private long id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 删除标记
     */
    private int deleteFlag;

    /**
     * 角色用户关系创建者ID
     */
    private String creator;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 系统ID
     */
    private long systemId;

    /**
     * 系统名称
     */
    private String systemName;

    private String creatorName = "";

    private String remark;

    /**
     * 角色关联的用户
     */
    private List<User> userList;

    /**
     * 角色标识
     */
    private String sign;

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
     * getCreatorName.
     *
     * @return CREATOR_NAME
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * setRemark.
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
     * setCreatorName.
     *
     * @param creatorName CREATOR_NAME
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
     * getUserList.
     *
     * @return USER_LIST
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * setUserList.
     *
     * @param userList USER_LIST
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
