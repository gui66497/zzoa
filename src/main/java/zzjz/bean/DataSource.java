package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 梅宏振
 * @version 2016年7月21日 下午4:36:04
 * @ClassName: DataSource
 * @Description: 数据源信息实体类
 */
@XmlRootElement
public class DataSource {
    /**
     * 数据源链接字符串
     */
    private String connection;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String columns;

    /**
     * getConnection.
     *
     * @return CONNECTION
     */
    public String getConnection() {
        return connection;
    }

    /**
     * setConnection.
     *
     * @param connection CONNECTION
     */
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * getTableName.
     *
     * @return TABLE_NAME
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * setTableName.
     *
     * @param tableName TABLE_NAME
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * getColumns.
     *
     * @return COLUMNS
     */
    public String getColumns() {
        return columns;
    }

    /**
     * setColumns.
     *
     * @param columns COLUMNS
     */
    public void setColumns(String columns) {
        this.columns = columns;
    }
}
