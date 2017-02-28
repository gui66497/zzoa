package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import zzjz.bean.PermissionAllotInfo;
import zzjz.bean.User;
import zzjz.service.PermissionAllotService;

import java.sql.*;
import java.util.List;

@Service
public class PermissionAllotServiceImpl implements PermissionAllotService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public int getTotalCount(String deleteFlag) {
		String sql = "select count(1) from tb_role where delete_flag=" + deleteFlag;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionAllotInfo> list(String deleteFlag, int index,
										  int pageSize) {
		String sql = "select a.*,b.system_name,c.user_name from tb_role a,tb_system b,tb_user c "
				+ " where a.system_id=b.system_id and a.creator=c.user_id and b.delete_flag=0 and a.delete_flag=" + deleteFlag
				+ " ORDER BY a.role_name LIMIT " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new PermissionAllotInfoMapper());
	}

	public String insert(String userId,long roleId,String creator) {
		final String finalUserId = userId;
		final long finalRoleId = roleId;
		final String finalCreator = creator;
		String tempSql = "insert into tb_user_role (user_id,role_id,creator,create_time,delete_flag)"
				+ " values (?,?,?,now(),0)";
		final String sql = tempSql;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, finalUserId);
				ps.setLong(2, finalRoleId);
				ps.setString(3, finalCreator);
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
	public PermissionAllotInfo getPermissionAllotInfoById(String roleId, int deleteFlag) {
//		String sql = "select a.*,b.system_name from tb_role a,tb_system b "
//				+ "where a.system_id=b.system_id and b.delete_flag=0 "
//				+ " and role_id=" + roleId + " and a.delete_flag=" + deleteFlag;
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
				"	AND role_id = " + roleId +
				"	AND a.delete_flag = " + deleteFlag;
		List<PermissionAllotInfo> rs = jdbcTemplate.query(sql, new PermissionAllotInfoMapper());
		if(rs.size() == 0){
			return null;
		}
		return rs.get(0);
	}

	public void delete(long userRoleId, String deleteFlag) {
		String sql = "update tb_user_role set delete_flag=" + deleteFlag
				+ " where user_role_id=" + userRoleId;
		jdbcTemplate.update(sql);
	}

	public String update(String userRoleId,String userId,long roleId,String creator) {
		String sql = "update tb_user_role set role_id=" + roleId
				+ ",user_id='" + userId
				+ "',creator='" + creator
				+ "',create_time=now() where user_role_id=" + userRoleId;
		jdbcTemplate.update(sql);
		return userRoleId;
	}

	private static final class PermissionAllotInfoMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			PermissionAllotInfo info = new PermissionAllotInfo();
			info.setCreateTime(rs.getString("create_time"));
			info.setCreator(rs.getString("creator"));
			info.setDeleteFlag(rs.getInt("delete_flag"));
			info.setId(rs.getLong("role_id"));
			info.setName(rs.getString("role_name"));
			info.setSystemId(rs.getLong("system_id"));
			info.setSystemName(rs.getString("system_name"));
			try {
				info.setCreatorName(rs.getString("user_name"));
			} catch (Exception e) {
			}
			info.setRemark(rs.getString("remark"));
			info.setSign(rs.getString("role_sign"));
			return info;
		}

	}

	@SuppressWarnings("unchecked")
	public List<User> getUserByRoleId(long roleId) {
		String sql = "select b.*,c.unit_name from tb_user_role a,tb_user b,tb_unit c "
				+ "where  a.user_id=b.user_id and b.unit_id=c.unit_id and a.delete_flag=0 and b.delete_flag=0 and a.role_id=" + roleId;
		return jdbcTemplate.query(sql, new UserMapper());
	}

	public int getTotalCountBySystemId(long systemId, String deleteFlag) {
		String sql = "select count(1) from tb_role where delete_flag=" + deleteFlag
				+ " and system_id=" + systemId;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionAllotInfo> listBySystemId(long systemId,
													String deleteFlag, int index, int pageSize) {
		String sql = "select a.*,b.system_name,c.user_name from tb_role a,tb_system b,tb_user c "
				+ " where a.system_id=b.system_id and a.creator=c.user_id and b.delete_flag=0 "
				+ "and a.delete_flag=" + deleteFlag
				+ " and b.system_id=" + systemId
				+ " ORDER BY a.role_name LIMIT " + index
				+ "," + pageSize;
		return jdbcTemplate.query(sql, new PermissionAllotInfoMapper());
	}

	public void delete(long roleId) {
		String sql = "delete from tb_user_role where role_id=" + roleId;
		jdbcTemplate.update(sql);
	}
	
	private static final class UserMapper implements RowMapper{   
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
            user.setUserId(rs.getString("USER_ID"));
            user.setUserName(rs.getString("USER_NAME"));
            user.setUnitId(rs.getString("UNIT_ID"));
            user.setUnitName(rs.getString("unit_name"));
            return user;
        }        
    }
}
