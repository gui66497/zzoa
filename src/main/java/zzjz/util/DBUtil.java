package zzjz.util;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.sql.*;

public class DBUtil {

	public static final String DATA_SOURCE_SEPARATE = "!";
	public static final String DATA_SEPARATE = ";";
	public static final String COLUMN_SEPARATE = ",";
	public static final int DATA_COUNT = 3;
	public static Logger log = Logger.getLogger(DBUtil.class);
	
	/** 
	* @Title: getConnection
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param dataSource
	* @return
	* @throws 
	*/
	public static Connection getConnection(String dataSource){
		Connection conn = null;
		String[] dataSourceArr = dataSource.split(COLUMN_SEPARATE);
		if(dataSourceArr.length < 3){
			return conn;
		}
		String url = dataSourceArr[0];
		String driverClass = "";
		if(url.startsWith("jdbc:oracle")){
			driverClass = "oracle.jdbc.driver.OracleDriver";
		}else if(url.startsWith("jdbc:mysql")){
			driverClass = "com.mysql.jdbc.Driver";
			
		}
		String user = dataSourceArr[1];
		String password = dataSourceArr[2];
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			log.debug(e.getMessage());
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/** 
	* @Title: executeQuery
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param conn
	* @param sql
	* @return
	* @throws 
	*/
	public static ResultSet executeQuery(Connection conn,String sql) {
		Statement statement = null;
		ResultSet rs = null;
		if(null == conn || StringUtils.isEmpty(sql)){
			return rs;
		}
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		return rs;
	}

	public static void close(Statement statement) {
		if(null != statement){
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet rs) {
		if(null != rs){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection conn) {
		if(null != conn){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
