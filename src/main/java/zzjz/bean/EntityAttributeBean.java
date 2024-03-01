package zzjz.bean;


/**
 * 实体类属性配置.
 *
 * @author 梅宏振
 * @version 2017/9/6 8:43
 */
public class EntityAttributeBean {

    /**
     * ID
     */
    private String id;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;

    /**
     * 对应Oracle表字段
     */
    private String column;

    /**
     * 对应Oracle表字段描述
     */
    private String description;

    /**
     * 对应实体类中的属性
     */
    private String attribute;

    /**
     * 是否是List
     */
    private boolean isList;

    /**
     * 是否是实体元素
     */
    private boolean isEntity;

    /**
     * 数据源IP
     */
    private String ip;

    /**
     * 端口号
     */
    private String port;

    /**
     * 数据库SID
     */
    private String db;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 值
     */
    private String value;

    /**
     * List属性对应的Bean
     */
    private EntityConfigBean subBean;

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
     * getAttribute.
     *
     * @return ATTRIBUTE
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * setAttribute.
     *
     * @param attribute ATTRIBUTE
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * isList.
     *
     * @return IS_LIST
     */
    public boolean isList() {
        return isList;
    }

    /**
     * setIsList.
     *
     * @param isList IS_LIST
     */
    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    /**
     * isEntity.
     *
     * @param isEntity1 IS_ENTITY
     */
    public void setEntity(boolean isEntity1) {
        this.isEntity = isEntity1;
    }

    /**
     * setEntity.
     *
     * @return IS_ENTITY
     */
    public boolean isEntity() {
        return isEntity;
    }

    /**
     * EntityConfigBean.
     *
     * @return SUB_BEAN
     */
    public EntityConfigBean getSubBean() {
        return subBean;
    }

    /**
     * setSubBean.
     *
     * @param subBean SUB_BEAN
     */
    public void setSubBean(EntityConfigBean subBean) {
        this.subBean = subBean;
    }

    /**
     * getId.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getType.
     *
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * setType.
     *
     * @param type TYPE
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getColumn.
     *
     * @return COLUMN
     */
    public String getColumn() {
        return column;
    }

    /**
     * setColumn.
     *
     * @param column COLUMN
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * getDescription.
     *
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription.
     *
     * @param description DESCRIPTION
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getIp.
     *
     * @return IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * setIp.
     *
     * @param ip IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * getPort.
     *
     * @return PORT
     */
    public String getPort() {
        return port;
    }

    /**
     * setPort.
     *
     * @param port PORT
     */
    public void setPort(String port) {
        this.port = port;
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
     * getValue.
     *
     * @return VALUE
     */
    public String getValue() {
        return value;
    }

    /**
     * setValue.
     *
     * @param value VALUE
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * getDb.
     *
     * @return DB
     */
    public String getDb() {
        return db;
    }

    /**
     * setDb.
     *
     * @param db DB
     */
    public void setDb(String db) {
        this.db = db;
    }

}
