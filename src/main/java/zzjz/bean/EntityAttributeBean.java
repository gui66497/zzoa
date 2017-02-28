package zzjz.bean;


/**
 * 实体类属性配置
 * @author 梅宏振
 *
 */
public class EntityAttributeBean {
	
	private String id;			//ID
	private String name;		//属性名称
	private String type;		//属性类型
	private String column;		//对应Oracle表字段
	private String description;	//对应Oracle表字段描述
	private String attribute;	//对应实体类中的属性
	private boolean isList;		//是否是List
	private boolean isEntity;	//是否是实体元素
	private String ip;			//数据源IP
	private String port;		//端口号
	private String db;			//数据库SID
	private String userName;	//用户名
	private String password;	//密码
	private String value;		//值
	private EntityConfigBean subBean; //List属性对应的Bean

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public boolean isList() {
		return isList;
	}
	public void setIsList(boolean isList) {
		this.isList = isList;
	}
	public void setEntity(boolean isEntity) {
		this.isEntity = isEntity;
	}
	public boolean isEntity() {
		return isEntity;
	}
	public EntityConfigBean getSubBean() {
		return subBean;
	}
	public void setSubBean(EntityConfigBean subBean) {
		this.subBean = subBean;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	
}
