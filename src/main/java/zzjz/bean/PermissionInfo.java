package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: PermissionInfo
 * @Description: 权限实体类
 * @author 曹雪东
 * @date 2015年5月9日 上午10:00:08
 */
@XmlRootElement
public class PermissionInfo {

	private long id;
	private String name;
	private String sign;
	private String creatorId;
	private String creatorName;
	private String createTime;
	private String remark;
	private long systemId;
	private String systemName;
	private int delete_falg;
	public void setRelation_flag(String relation_flag) {
		this.relation_flag = relation_flag;
	}

	private String targetName = ""; // 外部数据显示名称
	private String targetId = "-1"; // 外部数据ID
	private String targetTableName = ""; // 外部数据表名
	private String relation_flag;				//判断是否该权限需要和分类关联  0：不需要 1：需要

	public String getRelation_flag() {
		return relation_flag;
	}

	public void setReration_flag(String relation_flag) {
		this.relation_flag = relation_flag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public int getDelete_falg() {
		return delete_falg;
	}

	public void setDelete_falg(int delete_falg) {
		this.delete_falg = delete_falg;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

}
