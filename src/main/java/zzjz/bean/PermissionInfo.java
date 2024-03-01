package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 曹雪东
 * @version 2015年5月9日 上午10:00:08
 * @ClassName: PermissionInfo
 * @Description: 权限实体类
 */
@XmlRootElement
public class PermissionInfo {

    private long id;            //id

    private String name;        //权限名称

    private String sign;        //权限标志

    private String creatorId;   //创建者id

    private String creatorName; //创建者名称

    private String createTime;  //创建时间

    private String remark;      //备注

    private long systemId;      //系统id

    private String systemName;  //系统name

    private int deleteFlag;    //删除标识

    private String targetName = ""; // 外部数据显示名称

    private String targetId = "-1"; // 外部数据ID

    private String targetTableName = ""; // 外部数据表名

    private String relationFlag;                //判断是否该权限需要和分类关联  0：不需要 1：需要

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the creatorId
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId the creatorId to set
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return the creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * @param creatorName the creatorName to set
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the systemId
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * @param systemId the systemId to set
     */
    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    /**
     * @return the systemName
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * @param systemName the systemName to set
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * @return the deleteFlag
     */
    public int getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the delete_falg to set
     */
    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the targetId
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return the targetTableName
     */
    public String getTargetTableName() {
        return targetTableName;
    }

    /**
     * @param targetTableName the targetTableName to set
     */
    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    /**
     * @return the relationFlag
     */
    public String getRelationFlag() {
        return relationFlag;
    }

    /**
     * @param relationFlag the relationFlag to set
     */
    public void setRelationFlag(String relationFlag) {
        this.relationFlag = relationFlag;
    }

}
