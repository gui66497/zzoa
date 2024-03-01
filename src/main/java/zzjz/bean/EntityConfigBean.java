package zzjz.bean;

import java.util.List;

/**
 * 实体类配置信息类.
 *
 * @author 梅宏振
 * @version 2017/9/6 8:43
 */
public class EntityConfigBean {

    /**
     * 唯一ID
     */
    private String id;

    /**
     * 对应类的全路径
     */
    private String className;

    /**
     * 所有属性列表
     */
    private List<EntityAttributeBean> attributes;

    /**
     * 附加属性
     * 数据表名称
     */
    private String name;

    /**
     * 数据源
     */
    private String dsname;

    /**
     * 对应Oracle表名
     */
    private String table;

    /**
     * 表字段
     */
    private String column;

    /**
     * 文件后缀表字段
     */
    private String wjhzColumn;

    /**
     * 实际路径表字段
     */
    private String wjpathColumn;

    /**
     * 表字段对应值
     */
    private String value;

    /**
     * 状态
     */
    private String status;

    /**
     * 单位编码
     */
    private String unitId;

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
     * getClassName.
     *
     * @return CLASSNAME
     */
    public String getClassName() {
        return className;
    }

    /**
     * setClassName.
     *
     * @param className CLASSNAME
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * getAttributes.
     *
     * @return ATTRIBUTES
     */
    public List<EntityAttributeBean> getAttributes() {
        return attributes;
    }

    /**
     * setAttributes.
     *
     * @param attributes ATTRIBUTES
     */
    public void setAttributes(List<EntityAttributeBean> attributes) {
        this.attributes = attributes;
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
     * getDsname.
     *
     * @return DS_NAME
     */
    public String getDsname() {
        return dsname;
    }

    /**
     * setDsname.
     *
     * @param dsname DS_NAME
     */
    public void setDsname(String dsname) {
        this.dsname = dsname;
    }

    /**
     * getTable.
     *
     * @return TABLE
     */
    public String getTable() {
        return table;
    }

    /**
     * setTable.
     *
     * @param table TABLE
     */
    public void setTable(String table) {
        this.table = table;
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
     * getStatus.
     *
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus.
     *
     * @param status STATUS
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getWjhzColumn.
     *
     * @return WJ_HZ_COLUMN
     */
    public String getWjhzColumn() {
        return wjhzColumn;
    }

    /**
     * setWjhzColumn.
     *
     * @param wjhzColumn WJ_HZ_COLUMN
     */
    public void setWjhzColumn(String wjhzColumn) {
        this.wjhzColumn = wjhzColumn;
    }

    /**
     * getWjpathColumn.
     *
     * @return WJ_PATH_COLUMN
     */
    public String getWjpathColumn() {
        return wjpathColumn;
    }

    /**
     * setWjpathColumn.
     *
     * @param wjpathColumn WJ_PATH_COLUMN
     */
    public void setWjpathColumn(String wjpathColumn) {
        this.wjpathColumn = wjpathColumn;
    }

    /**
     * getUnitId.
     *
     * @return UNIT_ID
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * setUnitId.
     *
     * @param unitId UNIT_ID
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

}
