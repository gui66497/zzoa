package zzjz.util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库连接工厂类
 * 
 * @author 梅宏振
 * 
 */
public class ConnectionFactory {

	private Connection conn;
	private PreparedStatement psmt;
	private Logger LOGGER = Logger.getLogger(ConnectionFactory.class);

	/**
	 * 获取MySql数据库连接
	* @Title: getConnection
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @return
	* @throws
	 */
	public Connection getConnection() {
		LOGGER.debug("获取mysql连接开始.");
		//String url = LoadConfigUtil.getValue("url");
		//String userName = LoadConfigUtil.getValue("userName");
		//String password = LoadConfigUtil.getValue("password");
		String url = LoadConfigUtil.readFileConfig("system.properties", "url");
		String userName = LoadConfigUtil.readFileConfig("system.properties", "userName");
		String password = LoadConfigUtil.readFileConfig("system.properties", "password");
		System.out.println(">>>>USERNAME = " + userName);
		try {
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("mysql连接获取失败，" + e.getMessage());
		}
		LOGGER.debug("获取mysql连接结束，返回结果，conn=" + conn);
		return conn;
	}
			
			
	/**
	 * 获取指定的数据库连接
	 * 
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 */
	public Connection getConnection(String url, String userName, String password) {
		LOGGER.debug("获取Oracle连接开始，传入参数,url=" + url + ",userName=" + userName);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("Oracle连接获取失败，" + e.getMessage());
		}
		LOGGER.debug("获取Oracle连接结束，返回结果，conn=" + conn);
		return conn;
	}
	
	/**
	 * 获取数据库连接
	 * @param ip
	 * @param port
	 * @param db
	 * @param userName
	 * @param password
	 * @return
	 */
	public Connection getConnection(String ip,String port,String db,String userName,String password) {
		LOGGER.debug("获取Oracle连接开始，传入参数,ip="+ip +",port="+port+",db="+db+",userName="+userName+",password="+password);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			StringBuilder url = new StringBuilder("dbc:oracle:thin:@");
			url.append(ip).append(":").append(port).append(":").append(db);
			conn = DriverManager.getConnection (url.toString(), userName, password); 
		} catch(Exception e) {
			e.printStackTrace();
			LOGGER.debug("Oracle连接获取失败，" + e.getMessage());
		}
		LOGGER.debug("获取Oracle连接结束，返回结果，conn=" + conn);
		return conn;
	}
	

	@SuppressWarnings("null")
	public void destroyConnection(Connection conn) {
		conn = null;
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭PreparedStatement
	 * 
	 * @return
	 */
	public boolean closePreparedStatement() {
		try {
			if (psmt != null)
				psmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
