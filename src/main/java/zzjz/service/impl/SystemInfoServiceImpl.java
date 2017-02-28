package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zzjz.bean.SystemInfo;
import zzjz.service.SystemInfoService;
import zzjz.util.ConstantUtil;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int getTotalCount(String deleteFlag) {
		String sql = "select count(1) from tb_system where delete_flag=" + deleteFlag;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public int getTotalCountList(String userId, String authorityName) {
		String sql = "select count(1) from tb_system " +
				" WHERE delete_flag='0' AND system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				" LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				" ON t1.role_id = t3.role_id WHERE t2.authority_name = '"+authorityName+"' AND t3.user_id='"+userId+"')";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int getTotalCountList(String userId) {
		String sql = "select count(1) from tb_system " +
				" WHERE delete_flag='0' AND system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				" LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				" ON t1.role_id = t3.role_id WHERE  t3.user_id='"+userId+"')";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int getTotalCountAdmList() {
		String sql = "select count(1) from tb_system where delete_flag='0'" ;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	public List<Long> getTotal(String deleteFlag) {
		String sql = "select system_id from tb_system where delete_flag=" + deleteFlag;
		return  jdbcTemplate.queryForList(sql, Long.class);
	}
/*	@SuppressWarnings("rawtypes")
	private static final class TotalMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			List<Long> list = new ArrayList<Long>();
			list.add(rs.getLong("system_id"));
			return list;
		}
	}*/
	public int getTotalCount(String userId, String systemName,
			String systemSign, String permissionName, String deleteFlag) {
//		String sql = "select count(1) from tb_system f,(select a.target_id from tb_role_authority a,"
//				+ " tb_user_role b,tb_authority c,tb_system d"
//				+ " where a.delete_flag=0 and b.delete_flag=0 and c.delete_flag=0"
//				+ " and d.delete_flag=0 and a.role_id=b.role_id"
//				+ " and a.authority_id=c.authority_id and c.system_id=d.system_id"
//				+ " and b.user_id='" + userId
//				+ "' and c.authority_name='" + permissionName
//				+ "' and d.system_name='" + systemName
//				+ "' and d.system_sign='" + systemSign
//				+ "') e"
//				+ " where f.system_id=e.target_id and f.delete_flag=" + deleteFlag;
		//2015-07-22 李飞修改，添加用户自己创建的系统
		String sql = "SELECT" +
				"	count(1)" +
				"FROM" +
				"	tb_system f," +
				"	(" +
				"		SELECT" +
				"			a.target_id" +
				"		FROM" +
				"			tb_role_authority a," +
				"			tb_user_role b," +
				"			tb_authority c," +
				"			tb_system d" +
				"		WHERE" +
				"			a.delete_flag = 0" +
				"		AND b.delete_flag = 0" +
				"		AND c.delete_flag = 0" +
				"		AND d.delete_flag = 0" +
				"		AND a.role_id = b.role_id" +
				"		AND a.authority_id = c.authority_id" +
				"		AND c.system_id = d.system_id" +
				"		AND b.user_id = '" + userId + "'" +
				"		AND c.authority_name = '" + permissionName + "'" +
				"		AND d.system_name = '" + systemName + "'" +
				"		AND d.system_sign = '" + systemSign + "'" +
				"		UNION" +
				"			SELECT" +
				"				system_id" +
				"			FROM" +
				"				tb_system" +
				"			WHERE" +
				"				delete_flag = 0" +
				"			AND creator = '" + userId + "'" +
				"	) e" +
				"	WHERE" +
				"	f.system_id = e.target_id" +
				"	AND f.delete_flag = 0";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public List<SystemInfo> list(String deleteFlag, int index, int pageSize) {
		String sql = "select a.*,b.USER_NAME from tb_system a join tb_user b "
				+ "on a.creator=b.user_id where "
				//+ "b.delete_flag=0 and "
				+ "a.delete_flag=" + deleteFlag
				+ " limit " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new SystemInfoMapper());
	}

	@SuppressWarnings("unchecked")
	public SystemInfo checkUnique(String systemName, String systemSign) {
		String sql = "select * from tb_system where system_name='" + systemName
				+ "' and system_sign='" + systemSign
				+ "'";
		List<SystemInfo> rs = jdbcTemplate.query(sql, new SystemInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	public String insert(SystemInfo systemInfo) {
		final SystemInfo finalSystemInfo = systemInfo;
		String tempSql = "insert into tb_system (system_name,system_sign,creator,create_time,remark,"
				+ "data_source,delete_flag) values (?,?,?,now(),?,?,0)";
		final String sql = tempSql;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, finalSystemInfo.getName());
				ps.setString(2, finalSystemInfo.getSign());
				ps.setString(3, finalSystemInfo.getCreator());
				ps.setString(4, finalSystemInfo.getRemark());
				ps.setString(5, finalSystemInfo.getDataSource());
				return ps;
			}
		};
		jdbcTemplate.update(psc,keyHolder);
		Long generatedId = keyHolder.getKey().longValue();
		if(null == generatedId){
			return "";
		}
		return generatedId + "";
	}

	@SuppressWarnings("unchecked")
	public SystemInfo getSystemInfoById(String key, int deleteFlag) {
		String sql = "select a.*,b.user_name from tb_system a join tb_user b "
				+ "on a.creator=b.user_id where "
				//+ "b.delete_flag=0 and "
				+ "a.delete_flag=" + deleteFlag
				+ " and a.system_id=" + key;
		List<SystemInfo> rs = jdbcTemplate.query(sql, new SystemInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	public void delete(long systemId, String deleteFlag) {
		String sql = "update tb_system set delete_flag=" + deleteFlag
				+ ", system_name=CONCAT('@deleted@',system_name),system_sign=CONCAT('@deleted@',system_sign)"
				+ " where system_id=" + systemId;
		jdbcTemplate.update(sql);
	}
	
	public void delete(long systemId) {
		String sql = "delete from tb_system where system_id=" + systemId;
		jdbcTemplate.update(sql);
	}

	public String update(SystemInfo systemInfo, boolean isUpdateDeleteFlag) {
		String sql = "update tb_system set creator='" + systemInfo.getCreator()
				+ "',create_time=now(),remark='" + systemInfo.getRemark()
				+ "',data_source='" + systemInfo.getDataSource()
				+ "',system_name='" + systemInfo.getName()
				+"',system_sign='" + systemInfo.getSign();
		if(isUpdateDeleteFlag){
			sql += "',delete_flag=" + systemInfo.getDeleteFlag();
		}
		sql += "' where system_id=" + systemInfo.getId();
		jdbcTemplate.execute(sql);
		return systemInfo.getId() + "";
	}

	private static final class SystemInfoMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SystemInfo systemInfo = new SystemInfo();
			systemInfo.setCreateTime(rs.getString("create_time"));
			systemInfo.setCreator(rs.getString("creator"));
			try {
				systemInfo.setCreatorName(rs.getString("user_name"));
			} catch (Exception e) {
			}
			systemInfo.setDataSource(rs.getString("data_source"));
			systemInfo.setDeleteFlag(rs.getInt("delete_flag"));
			systemInfo.setRemark(rs.getString("remark"));
			systemInfo.setId(rs.getLong("system_id"));
			systemInfo.setName(rs.getString("system_name"));
			systemInfo.setSign(rs.getString("system_sign"));
			return systemInfo;
		}

	}

	public List<SystemInfo> list(String userId, String permissionName,
								 String deleteFlag, int index, int pageSize) {
		String systemName = ConstantUtil.getSystemName();
		String systemSign = ConstantUtil.getSystemSign();
		return list(userId, systemName, systemSign, permissionName, deleteFlag, index, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<SystemInfo> list(String userId, String systemName,
								 String systemSign, String permissionName, String deleteFlag,
								 int index, int pageSize) {
		String sql = "select * from tb_system f,(select a.target_id from tb_role_authority a,"
				+ " tb_user_role b,tb_authority c,tb_system d"
				+ " where a.delete_flag=0 and b.delete_flag=0 and c.delete_flag=0"
				+ " and d.delete_flag=0 and a.role_id=b.role_id"
				+ " and a.authority_id=c.authority_id and c.system_id=d.system_id"
				+ " and b.user_id='" + userId
				+ "' and c.authority_name='" + permissionName
				+ "' and d.system_name='" + systemName
				+ "' and d.system_sign='" + systemSign + "'" + 
				"		UNION" +
				"			SELECT" +
				"				system_id" +
				"			FROM" +
				"				tb_system" +
				"			WHERE" +
				"				delete_flag = 0" +
				"			AND creator = '" + userId + "'" +
				") e"
				+ " where f.system_id=e.target_id and f.delete_flag=" + deleteFlag
				+ " limit " + index + "," + pageSize;
		return jdbcTemplate.query(sql, new SystemInfoMapper());
	}

	public int getTotalCount(String userId, String permissionName,
			String deleteFlag) {
		String systemName = ConstantUtil.getSystemName();
		String systemSign = ConstantUtil.getSystemSign();
		return getTotalCount(userId, systemName, systemSign, permissionName, deleteFlag);
	}

	public boolean checkDefaultSystem(String systemId) {
		String systemName = ConstantUtil.getSystemName();
		String systemSign = ConstantUtil.getSystemSign();
		return checkSystemInfor(systemId, systemName, systemSign);
	}

	/** 判断系统ID和系统名称、系统标识是否是同一条数据
	* @Title: checkSystemInfor
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param systemId
	* @param systemName
	* @param systemSign
	* @return
	* @throws 
	*/
	private boolean checkSystemInfor(String systemId, String systemName,
			String systemSign) {
		String sql = "select count(1) from tb_system where delete_flag=0 and system_id=" + systemId + " and system_name='"
			+ systemName + "' and system_sign='" + systemSign + "'";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		if(1 == count){
			return true;
		}
		return false;
	}

	public Map<String, String> getColumnValues(long systemId) {
		String sql = "select a.* from tb_system a where a.system_id=" + systemId;
		List<Map<String,String>> list = jdbcTemplate.query(sql, new ColumnMapper());
		if(0 == list.size()){
			return new HashMap<String, String>();
		}
		return list.get(0);
	}

	@Override
	public SystemInfo getSystemInfoById(long systemId) {
		String sql = "select * from tb_system where delete_flag = 0 and system_id= " + systemId;
		List<SystemInfo> rs = jdbcTemplate.query(sql, new SystemInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	@Override
	public List<SystemInfo> getSystemListByUserId(String authorityName, String userId) {
		String sql = "SELECT * FROM tb_system WHERE system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				"LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				"ON t1.role_id = t3.role_id WHERE t2.authority_name='"+authorityName+"' AND t3.user_id='"+userId+"')";

		return jdbcTemplate.query(sql, new SystemInfoMapper());
	}
	@Override
	public List<SystemInfo> getSystemListByUserId(int index, int pageSize, String authorityName, String userId) {
		String sql = "SELECT * FROM tb_system WHERE system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				"LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				"ON t1.role_id = t3.role_id WHERE t2.authority_name='"+authorityName+"' AND t3.user_id='"+userId+"')"+" LIMIT "+index+","+pageSize;;

		return jdbcTemplate.query(sql, new SystemInfoMapper());
	}
	private static final class ColumnMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> map = new HashMap<String, String>();
			map.put("create_time",rs.getString("create_time"));
			map.put("creator",rs.getString("creator"));
			map.put("data_source",rs.getString("data_source"));
			map.put("delete_flag",rs.getString("delete_flag"));
			map.put("remark",rs.getString("remark"));
			map.put("system_id",rs.getString("system_id"));
			map.put("system_name",rs.getString("system_name"));
			map.put("system_sign",rs.getString("system_sign"));
			return map;
		}

	}
	public void addDefaultAuthority(String path,String userId, String systemId) throws SQLException, IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)),"utf-8"));
		String line = null;
		DataSource dataSource = jdbcTemplate.getDataSource();
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		while((line = reader.readLine()) != null){
			if(StringUtils.isEmpty(line)) {
				continue;
			}
			line = getDefaultSQL(line,userId,systemId);
			stmt.addBatch(line);
		}
		stmt.executeBatch();
		stmt.close();
		reader.close();
	}
	//获取统一权限默认权限sql语句
	public String getDefaultSQL(String sql, String userId, String systemId){
		sql = sql.replaceAll("userId",userId);
		sql = sql.replaceAll("systemId",systemId);
		return sql;
	}

	public SystemInfo getSystemInfoBySign(String systemSign) {
		String sql = "select * from tb_system where delete_flag = 0 and system_sign= " + systemSign;
		List<SystemInfo> rs = jdbcTemplate.query(sql, new SystemInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}
}
