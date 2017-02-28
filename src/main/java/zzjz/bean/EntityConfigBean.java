package zzjz.bean;

import java.util.List;

/**
 * 实体类配置信息类
 * @author 梅宏振
 *
 */
public class EntityConfigBean {

	private String id; 			//唯一ID
	private String className;	//对应类的全路径
	private List<EntityAttributeBean> attributes; //所有属性列表
	
	//附加属性
	private String name;		//数据表名称
	private String dsname;		//数据源
	private String table;		//对应Oracle表名
	private String column;		//表字段
	private String wjhzColumn;	//文件后缀表字段
	private String wjpathColumn;//实际路径表字段
	private String value;		//表字段对应值
	private String status;		//状态
	private String unitId;		//单位编码
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<EntityAttributeBean> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<EntityAttributeBean> attributes) {
		this.attributes = attributes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDsname() {
		return dsname;
	}
	public void setDsname(String dsname) {
		this.dsname = dsname;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWjhzColumn() {
		return wjhzColumn;
	}
	public void setWjhzColumn(String wjhzColumn) {
		this.wjhzColumn = wjhzColumn;
	}
	public String getWjpathColumn() {
		return wjpathColumn;
	}
	public void setWjpathColumn(String wjpathColumn) {
		this.wjpathColumn = wjpathColumn;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
}
