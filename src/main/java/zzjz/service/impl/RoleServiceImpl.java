package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import zzjz.bean.PermissionInfo;
import zzjz.bean.RoleInfo;
import zzjz.service.RoleService;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RoleServiceImpl
 * @Description: 角色服务类
 * @author 曹雪东
 * @date 2015年5月9日 下午4:04:35
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int getTotalCount(String deleteFlag) {
		String sql = "select count(1) from tb_role a,tb_system b "
				+ " where a.system_id=b.system_id and b.delete_flag=0 and a.delete_flag="
				+ deleteFlag;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int getTotalCountBySystemId(long systemId, String deleteFlag) {
		String sql = "select count(1) from tb_role  a,tb_system b "
				+ " where a.system_id=b.system_id and b.delete_flag=0 and a.delete_flag="
				+ deleteFlag + " and a.system_id=" + systemId;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public List<RoleInfo> list(String deleteFlag, int index, int pageSize) {
		String sql = "select a.*,b.system_name,c.user_name from tb_role a,tb_system b,tb_user c "
				+ " where a.system_id=b.system_id and a.creator=c.user_id "
				+ "and b.delete_flag=0 and a.delete_flag=" + deleteFlag
				+ " ORDER BY a.create_time DESC LIMIT " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new RoleInfoMapper());
	}

	@SuppressWarnings("unchecked")
	public List<RoleInfo> listBySystemId(long systemId, String deleteFlag,
										 int index, int pageSize) {
		String sql = "select a.*,b.system_name,c.user_name from tb_role a,tb_system b,tb_user c "
				+ " where a.system_id=b.system_id and a.creator=c.user_id and b.delete_flag=0 "
				+ "and a.delete_flag=" + deleteFlag
				+ " and b.system_id=" + systemId
				+ " ORDER BY a.create_time DESC LIMIT " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new RoleInfoMapper());
	}

	@SuppressWarnings("unchecked")
	public RoleInfo checkUnique(String roleName, String roleSign, String systemId) {
		String sql = "select * from tb_role where role_name='" + roleName
				+ "' and role_sign='" + roleSign
				+ "' and system_id=" + systemId;
		List<RoleInfo> rs = jdbcTemplate.query(sql, new RoleInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	public String insert(RoleInfo roleInfo) {
		final RoleInfo finalRoleInfo = roleInfo;
		String tempSql = "insert into tb_role (role_name,role_sign,creator,create_time,remark,"
				+ "delete_flag,system_id) values (?,?,?,now(),?,0,?)";
		final String sql = tempSql;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, finalRoleInfo.getName());
				ps.setString(2, finalRoleInfo.getSign());
				ps.setString(3, finalRoleInfo.getCreator());
				ps.setString(4, finalRoleInfo.getRemark());
				ps.setLong(5, finalRoleInfo.getSystemId());
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
	public RoleInfo getRoleInfoById(String roleId, int deleteFlag) {
//		String sql = "select a.*,b.system_name from tb_role a join tb_system b "
//				+ "on a.system_id=b.system_id where b.delete_flag=0 and a.role_id=" + roleId
//				+ " and a.delete_flag=" + deleteFlag;
		String sql = "	SELECT" +
				"		a.*, b.system_name," +
				"		c.user_name" +
				"	FROM" +
				"		tb_role a," +
				"		tb_system b," +
				"		tb_user c" +
				"	WHERE" +
				"		a.system_id = b.system_id" +
				"	AND a.creator = c.user_id" +
				"	AND b.delete_flag = 0" +
				"	AND a.role_id = " + roleId +
				"	AND a.delete_flag = " + deleteFlag;
		List<RoleInfo> rs = jdbcTemplate.query(sql, new RoleInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	public void delete(long roleId, String deleteFlag) {
		String sql = "update tb_role set delete_flag=" + deleteFlag
				+ ", role_name=CONCAT('@deleted@',role_name),role_sign=CONCAT('@deleted@',role_sign)"
				+ " where role_id=" + roleId;
		jdbcTemplate.update(sql);
	}
	
	public void delete(long roleId) {
		String sql = "delete from tb_role where role_id=" + roleId;
		jdbcTemplate.update(sql);
	}

	public void deleteRoleAuthority(long roleId) {
		String sql = "delete from tb_role_authority where role_id=" + roleId;
		jdbcTemplate.update(sql);
	}
	public void deleteRoleAuthoritybyTargetId(String sqlStr,String systemId){
		String sql = "update tb_role_authority set delete_flag = 1  where delete_flag = 0 and "+sqlStr;
		System.out.println("sql>>>>>>>>"+sql);
		jdbcTemplate.update(sql);
	}
	public String update(RoleInfo roleInfo) {
		String sql = "update tb_role set creator='" + roleInfo.getCreator()
				+ "',create_time=now(),remark='" + roleInfo.getRemark()
				+ "',role_name='" + roleInfo.getName()
				+"',role_sign='" + roleInfo.getSign()
				+"',delete_flag=" + roleInfo.getDeleteFlag()
				//+ ",system_id=" + roleInfo.getSystemId()
				+" where role_id=" + roleInfo.getId();
		jdbcTemplate.execute(sql);
		return roleInfo.getId() + "";
	}

	public String insertRoleAuthority(long roleId,PermissionInfo permissionInfo) {
		final PermissionInfo finalPermissionInfo = permissionInfo;
		final long finalRoleId = roleId;
		String tempSql = "insert into tb_role_authority (role_id,authority_id,creator,create_time,"
				+ "delete_flag,target_id,target_table_name,target_name) values (?,?,?,now(),0,?,?,?)";
		final String sql = tempSql;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, finalRoleId);
				ps.setLong(2, finalPermissionInfo.getId());
				ps.setString(3, finalPermissionInfo.getCreatorId());
				ps.setString(4, finalPermissionInfo.getTargetId());
				ps.setString(5, finalPermissionInfo.getTargetTableName());
				ps.setString(6, finalPermissionInfo.getTargetName());
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

	private static final class RoleInfoMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleInfo info = new RoleInfo();


			info.setId(rs.getLong("role_id"));
			info.setName(rs.getString("role_name"));
			info.setSystemId(rs.getLong("system_id"));
			info.setSign(rs.getString("role_sign"));
			try {
				info.setRemark(rs.getString("remark"));
				info.setDeleteFlag(rs.getInt("delete_flag"));
				info.setCreateTime(rs.getString("create_time"));
				info.setCreator(rs.getString("creator"));
				info.setSystemName(rs.getString("system_name"));
				info.setCreatorName(rs.getString("user_name"));
			} catch (Exception e) {
			}
			return info;
		}

	}

	public Map<String, String> getColumnValues(long roleId) {
		String sql = "select a.* from tb_role a where a.role_id=" + roleId;
		List<Map<String,String>> list = jdbcTemplate.query(sql, new ColumnMapper());
		if(0 == list.size()){
			return new HashMap<String, String>();
		}
		return list.get(0);
	}
	
	private static final class ColumnMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> map = new HashMap<String, String>();
			map.put("create_time",rs.getString("create_time"));
			map.put("creator",rs.getString("creator"));
			map.put("delete_flag",rs.getString("delete_flag"));
			map.put("remark",rs.getString("remark"));
			map.put("role_id",rs.getString("role_id"));
			map.put("role_name",rs.getString("role_name"));
			map.put("role_sign",rs.getString("role_sign"));
			map.put("system_id",rs.getString("system_id"));
			return map;
		}

	}

	public List<Map<String, String>> queryTargetList(String roleId,
			String permissionId) {
		String sql = "select target_id,target_table_name from tb_role_authority where delete_flag=0 and role_id=" + roleId
				+ " and authority_id=" + permissionId;
		return jdbcTemplate.query(sql, new MapMapper());
	}

	@Override
	public List<RoleInfo> getRoleList(String authorityName, String userId, int index, int pageSize) {
		String sql = "select a.*,b.system_name,c.user_name from tb_role a,tb_system b,tb_user c "
				+ " where a.system_id=b.system_id and a.creator=c.user_id "
				+ "and b.delete_flag=0 and a.delete_flag=0 and a.system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				"LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				"ON t1.role_id = t3.role_id WHERE t2.authority_name='"+authorityName+"' AND t3.user_id='"+userId+"')"
				+ " ORDER BY a.create_time DESC LIMIT " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new RoleInfoMapper());
	}

	public int getTotalCount(String authorityName, String userId) {
		String sql = "select count(1) from tb_role a,tb_system b "
				+ " where a.system_id=b.system_id and b.delete_flag=0 and a.delete_flag=0 and a.system_id IN(SELECT t1.target_id FROM tb_role_authority t1 " +
				"LEFT JOIN tb_authority t2 ON t1.authority_id = t2.authority_id LEFT JOIN tb_user_role t3  " +
				"ON t1.role_id = t3.role_id WHERE t2.authority_name='"+authorityName+"' AND t3.user_id='"+userId+"')";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public List<RoleInfo> getUserAllRoles(long userId) {
		String sql = "SELECT r.*,s.system_name AS system_name " +
				"FROM tb_user_role ur " +
				"JOIN tb_role r ON ur.role_id = r.role_id " +
				"JOIN tb_system s ON r.system_id = s.system_id " +
				"WHERE ur.user_id = " + userId +
				" AND ur.delete_flag = 0 " +
				"AND r.delete_flag = 0 " +
				"GROUP BY r.system_id ";
		return jdbcTemplate.query(sql, new RoleInfoMapper());
	}

	@Override
	public List<Long> getTargetIdList(String userId, long systemId, String dataSource, String authorityId) {
		String sql = "SELECT target_id FROM tb_role_authority tr INNER JOIN tb_authority ta ON tr.authority_id = ta.authority_id " +
				"WHERE ta.system_id = "+systemId+" AND tr.authority_id="+authorityId+" AND tr.role_id IN" +
				"(SELECT role_id FROM tb_user_role WHERE user_id="+userId+") AND tr.target_table_name='"+dataSource+"' AND tr.delete_flag=0";
		return jdbcTemplate.queryForList(sql, Long.class);
	}

	@Override
	public List<RoleInfo> getRoleListByUserIdAndSysId(long systemId, String userId, int index, int pageSize) {
		String sql = "SELECT r.role_id,r.role_name,r.role_sign,r.system_id,s.system_name " +
				"FROM tb_user_role ur " +
				"LEFT JOIN tb_role r ON ur.role_id = r.role_id " +
				"JOIN tb_system s ON r.system_id = s.system_id " +
				"WHERE 1=1 " +
				"AND ur.user_id = '" + userId + "' " +
				"AND ur.delete_flag = 0 " +
				"AND r.delete_flag = 0 " +
				"AND s.delete_flag = 0 ";

		if (systemId > -1) {
			sql += " AND r.system_id = " + systemId + " ";
		}
		sql += " LIMIT " + index + "," + pageSize;
		return jdbcTemplate.query(sql, new RoleInfoMapper());
	}

	@Override
	public int getTotalCountByUserIdAndSysId(long systemId, String userId) {
		String sql = "SELECT COUNT(1) " +
				"FROM tb_user_role ur " +
				"LEFT JOIN tb_role r ON ur.role_id = r.role_id " +
				"JOIN tb_system s ON r.system_id = s.system_id " +
				"WHERE 1=1 " +
				"AND ur.user_id = '" + userId + "' " +
				"AND ur.delete_flag = 0 " +
				"AND r.delete_flag = 0 " +
				"AND s.delete_flag = 0 ";
		if (systemId > -1) {
			sql += " AND r.system_id = " + systemId;
		}
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}


	@Override
	public boolean hasRoleInSystem(long roleId, long systemId) {
		String sql = "SELECT count(1) FROM tb_role WHERE delete_flag=0 AND role_id="+roleId
				+" AND system_id="+systemId;
		return jdbcTemplate.queryForList(sql, Integer.class).get(0) > 0 ? true : false;
	}

	@Override
	public boolean hasRoleAuthority(long roleId, long authorityId, long targetId) {
		String sql = "SELECT count(1) FROM tb_role_authority WHERE delete_flag=0 AND role_id="+roleId
				+" AND authority_id ="+authorityId+" AND target_id="+targetId;
		return jdbcTemplate.queryForList(sql, Integer.class).get(0) > 0 ? true : false;
	}

	@Override
	public boolean hasRoleAuthority(long roleId) {
		String sql = "SELECT count(1) FROM tb_role_authority WHERE delete_flag=0 AND role_authority_id="+roleId;
		return jdbcTemplate.queryForList(sql, Integer.class).get(0) > 0 ? true : false;
	}

	private static final class MapMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id",rs.getString("TARGET_ID"));
			map.put("targetTableName",rs.getString("TARGET_TABLE_NAME"));
			return map;
		}

	}
}
