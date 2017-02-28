package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: DataSource
 * @Description: 数据源信息实体类
 * @author 梅宏振
 * @date 2016年7月21日 下午4:36:04
 */
@XmlRootElement
public class DataSource {
    private String connection;  //数据源链接字符串
    private String tableName;   //表名
    private String columns;     //字段名

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
