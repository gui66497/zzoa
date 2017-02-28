package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.PagingEntity;
import zzjz.bean.User;
import zzjz.service.UserService;
import zzjz.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** 
* @ClassName: UserServiceImpl 
* @Description: 用户Serversicesh 
* @author 梅宏振
* @date 2015年3月4日 下午1:35:20
*/
@Service
public class UserServiceImpl implements UserService {
	
	/** 
	* @Title: convertDate
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param dateStr
	* @param sdf
	* @return
	* @throws 
	*/
	public Date convertDate(String dateStr, SimpleDateFormat sdf) {
		Date date = new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private zzjz.mapper.UserMapper userMapper;

	@Override
	public User getUserById2(String userId) {
		User user = userMapper.getUserByUserId(userId);
		return user;
	}

	@Override
	public List<User> getUserList2() {
		return userMapper.getUserList();
	}

	@Override
	public User getUserByName2(String userName) {
		return userMapper.getUserByName(userName);
	}

	@Override
	public boolean addUser2(User user) {
		int res = userMapper.insertSelective(user);
		return res > 0;
	}

	@Override
	public Page<User> getUserListPage(PagingEntity pagingEntity) {
		PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
		Page<User> userPage = (Page<User>) userMapper.getUserList();
		long a = userPage.getTotal();
		return userPage;
	}

	@Override
	public Page<User> getUserListPageWithName(String userName, PagingEntity pagingEntity) {
		PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
		Page<User> userPage = (Page<User>) userMapper.getUserListWithName(userName);
		long a = userPage.getTotal();
		return userPage;
	}

	public boolean deleteUser(long userId) {
		int res = userMapper.deleteByUserId(userId);
		return res > 0;
	}









	public List<User> getUserInfoById(List<Integer> userIds) {
		String userIdStr = StringUtil.listConvertToString(userIds, ",");
		StringBuilder SQL = new StringBuilder("SELECT USER_ID,USER_NAME,UNIT_ID FROM TB_USER ");
		SQL.append("WHERE DELETE_FLAG=0 AND USER_ID IN(").append(userIdStr).append(")");
		return this.jdbcTemplate.query(SQL.toString(),new UserMapper()); 
	}

	public List<User> getUserInfoById(String userId) {
		StringBuilder SQL = new StringBuilder("SELECT USER_ID,USER_NAME,UNIT_ID FROM TB_USER ");
		SQL.append("WHERE DELETE_FLAG=0 AND USER_ID IN('").append(userId).append("')");
		return this.jdbcTemplate.query(SQL.toString(),new UserMapper()); 
	}

	@Override
	public List<User> getUserDetailBySystemId(String systemId) {
		String sql = "SELECT u.*,GROUP_CONCAT(r.role_id) AS role_id,GROUP_CONCAT(r.role_name) AS role_name " +
				"FROM tb_role r " +
				"JOIN tb_user_role ur ON r.role_id = ur.role_id " +
				"JOIN tb_user u ON ur.user_id = u.user_id " +
				"WHERE 1=1 " +
				" AND r.delete_flag = 0 " +
				"AND ur.delete_flag = 0 " +
				"AND u.delete_flag = 0 ";
		if (!"0".equals(systemId)) {//systemId存在
			sql +=  "AND r.system_id = " + systemId;
		}
		sql += " GROUP BY u.user_id ";
		return this.jdbcTemplate.query(sql,new UserMapper());
	}

	/**
	* @Title: getRecordIdList
	* @Description: title为空的时候获取当前用户拥有的或者编辑的词条列表 ，否则是根据标题查询当前用户拥有的或者编辑的词条列表
	* @param userId	用户ID
	* @param title	词条标题  
	* @param statisticsType	统计类型
	* @param paging	分页实体类
	* @return 当前用户拥有的或者编辑的词条列表  
	* @author 顾征根
	* @date 2015年3月2日 下午2:56:06
	*/
	public List<Long> getRecordIdList(String userId, String title, int statisticsType, PagingEntity paging){
		int pageNo = paging.getPageNo();	//页码
		int pageSize = paging.getPageSize();	//每页记录数
		String SQL = "";
		if(StringUtils.isNotBlank(title)){
			SQL = "SELECT" +
					"		a.RECORD_ID" +
					"	FROM" +
					"		tb_article_available a" +
					"	WHERE" +
					"		a.DELETE_FLAG = 0" +
					"	AND a.STATISTICS_TYPE = "+statisticsType+
					"	AND a.RECORD_ID IN (" +
					"		SELECT" +
					"			DISTINCT(tb1.RECORD_ID)" +
					"		FROM" +
					"			tb_article_column_value tb1" +
					"		LEFT JOIN tb_article_column tb2 ON tb1.COLUMN_ID = tb2.COLUMN_ID" +
					"		WHERE" +
					"			tb2.COLUMN_NAME = '标题'" +
					"		AND tb1.COLUMN_VALUE LIKE '%"+title+"%'" +
					"		AND tb1.RECORD_ID IN (" +
					"			SELECT DISTINCT" +
					"				(t3.RECORD_ID)" +
					"			FROM" +
					"				tb_article_column_value t3" +
					"			LEFT JOIN tb_article_column t4 ON t3.COLUMN_ID = t4.COLUMN_ID" +
					"			WHERE" +
					"				t4.COLUMN_NAME = '编者'" +
					"			AND t3.COLUMN_VALUE = '"+userId+"'" +
					"			OR t4.COLUMN_NAME = '最后修改者'" +
					"			AND t3.COLUMN_VALUE = '"+userId+"'" +
					"			AND t3.DELETE_FLAG = 0" +
					"			AND t4.DELETE_FLAG = 0" +
					"			ORDER BY" +
					"				t4.CREATE_TIME DESC" +
					"		)" +
					"	)" +
					"	ORDER BY" +
					"		a.available DESC" +
					"	LIMIT "+((pageNo-1)*pageSize)+","+(pageNo*pageSize-1);
		}else{
				SQL = "SELECT" +
					"		a.RECORD_ID" +
					"	FROM" +
					"		tb_article_available a" +
					"	WHERE" +
					"		a.DELETE_FLAG = 0" +
					"	AND a.STATISTICS_TYPE = "+statisticsType+
					"	AND a.RECORD_ID IN (" +
					"		SELECT DISTINCT" +
					"			(t1.RECORD_ID)" +
					"		FROM" +
					"			tb_article_column_value t1" +
					"		LEFT JOIN tb_article_column t2 ON t1.COLUMN_ID = t2.COLUMN_ID" +
					"		WHERE" +
					"			t2.COLUMN_NAME = '编者'" +
					"		AND t1.COLUMN_VALUE = '"+userId+"'" +
					"		OR t2.COLUMN_NAME = '最后修改者'" +
					"		AND t1.COLUMN_VALUE = '"+userId+"'" +
					"		AND t1.DELETE_FLAG = 0" +
					"		AND t2.DELETE_FLAG = 0" +
					"		ORDER BY" +
					"			t1.CREATE_TIME DESC" +
					"	)" +
					"	ORDER BY" +
					"		a.AVAILABLE DESC" +
					"	LIMIT "+((pageNo-1)*pageSize)+","+(pageNo*pageSize-1);
		}
		List<Long> recordIds = this.jdbcTemplate.queryForList(SQL, Long.class);
		return recordIds;
	}

	/** 
	* @Title: getCountByUserId
	* @Description: 获取当前用户拥有的或者编辑的词条记录数 
	* @param userId	用户ID
	* @param title 词条标题
	* @return 当前用户拥有的或者编辑的词条记录数 	
	* @author 顾征根
	* @date 2015年3月2日 下午2:56:51
	*/
	public long getCountByUserId(String userId, String title){
		String SQL = "";
		if(StringUtils.isBlank(title)){
			
		}else{
			
		}
		SQL = "	SELECT" +
				"		COUNT(DISTINCT(t1.RECORD_ID))" +
				"	FROM" +
				"		tb_article_column_value t1" +
				"	LEFT JOIN tb_article_column t2 ON t1.COLUMN_ID = t2.COLUMN_ID" +
				"	WHERE" +
				"		t2.COLUMN_NAME = '编者'" +
				"	AND t1.COLUMN_VALUE = '"+userId+"'" +
				"	OR t2.COLUMN_NAME = '最后修改者'" +
				"	AND t1.COLUMN_VALUE = '"+userId+"'" +
				"	AND t1.DELETE_FLAG = 0" +
				"	AND t2.DELETE_FLAG = 0" +
				"	ORDER BY" +
				"		t1.CREATE_TIME DESC";
		List<Long> counts = this.jdbcTemplate.queryForList(SQL, Long.class);
		if(counts.size() > 0){
			return counts.get(0);
		}
		return 0;
	}
	
	
	
	private static final class UserMapper implements RowMapper {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserId(rs.getString("USER_ID"));
            user.setUserName(rs.getString("USER_NAME"));
			user.setPassword(rs.getString("password"));
            user.setUnitId(rs.getString("UNIT_ID"));
			if (isExistColumn(rs, "role_id")) {
				user.setRoleId(rs.getString("role_id"));
			}
			if (isExistColumn(rs, "role_name")) {
				user.setRoleName(rs.getString("role_name"));
			}
			user.setCreateTime(rs.getString("create_time"));
            try {
				user.setUnitName(rs.getString("unit_name"));
			} catch (Exception e) {
			}
            return user;
        }

		boolean isExistColumn(ResultSet rs, String columnName) {
			try {
				if (rs.findColumn(columnName) > 0) {
					return true;
				}
			} catch (SQLException e) {
				System.out.println("通过异常判断结果集中是否存在这个属性：" + e.getMessage());
				return false;
			}
			return  false;
		}
    }

	public int getTotalCount(String adminUserId) {
		String sql = "SELECT count(1) " +
				"FROM tb_user WHERE " +
				"delete_flag = 0 AND user_id != " + adminUserId;
		String rs = jdbcTemplate.queryForObject(sql, String.class);
		return Integer.valueOf(rs);
	}

	public List<Long> getRecordIdList(String authorId, int statisticsType,
			int index, int pageSize) {
		final String finalAuthorId = authorId;
		final int finalStatisticsType = statisticsType;
		final int finalIndex = index;
		final int finalPageSize = pageSize;
		String tempSql = "	SELECT" +
				"		c.record_id" +
				"	FROM" +
				"		(" +
				"			SELECT DISTINCT" +
				"				record_id" +
				"			FROM" +
				"				tb_article_edit_history" +
				"			WHERE" +
				"				delete_flag = 0" +
				"			AND confirm_state = 1" +
				"			AND user_id = ?" +
				"			UNION" +
				"				SELECT DISTINCT" +
				"					b.record_id" +
				"				FROM" +
				"					tb_article_column a" +
				"				JOIN tb_article_column_value b ON a.column_id = b.column_id" +
				"				WHERE" +
				"					a.delete_flag = 0" +
				"				AND b.delete_flag = 0" +
				"				AND a.column_name = '创建者'" +
				"				AND b.column_value = ?" +
				"		) c" +
				"	LEFT OUTER JOIN (" +
				"		SELECT" +
				"			*" +
				"		FROM" +
				"			tb_article_available" +
				"		WHERE" +
				"			delete_flag = 0" +
				"		AND statistics_type = ?" +
				"	) d ON c.record_id = d.record_id" +
				"	ORDER BY" +
				"		d.available DESC";
		if(index >= 0 && pageSize >= 0){
			tempSql += "	LIMIT ?,?";
		}
		final String sql = tempSql;
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, finalAuthorId);
				ps.setString(2, finalAuthorId);
				ps.setInt(3, finalStatisticsType);
				if (finalIndex >= 0 && finalPageSize >= 0) {
					ps.setInt(4, finalIndex);
					ps.setInt(5, finalPageSize);
				}
				return ps;
			}
		};
		return jdbcTemplate.query(psc, new LongMapper());
	}

	public int getCreateAndEditTotalCount(String authorId) {
		final String finalAuthorId = authorId;
		final String sql = "SELECT" +
				"	count(1)" +
				"FROM" +
				"	(" +
				"		SELECT DISTINCT" +
				"			record_id" +
				"		FROM" +
				"			tb_article_edit_history" +
				"		WHERE" +
				"			delete_flag = 0" +
				"		AND confirm_state = 1" +
				"		AND user_id = ?" +
				"		UNION" +
				"			SELECT DISTINCT" +
				"				b.record_id" +
				"			FROM" +
				"				tb_article_column a" +
				"			JOIN tb_article_column_value b ON a.column_id = b.column_id" +
				"			WHERE" +
				"				a.delete_flag = 0" +
				"			AND b.delete_flag = 0" +
				"			AND a.column_name = '创建者'" +
				"			AND b.column_value = ?" +
				"	) c";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, finalAuthorId);
				ps.setString(2, finalAuthorId);
				return ps;
			}
		};
		
		List<String> rs = jdbcTemplate.query(psc, new StringMapper());
		return Integer.parseInt(rs.get(0));
	}

	@Override
	public boolean login(String userName, String password) {
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			return false;
		}
		final String finalUserName = userName;
		final String finalPassword = password;
		final StringBuffer sql = new StringBuffer("SELECT USER_NAME from TB_USER WHERE binary USER_NAME=? AND binary PASSWORD=? AND DELETE_FLAG=0");
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql.toString());
				ps.setString(1, finalUserName);
				ps.setString(2, finalPassword);
				return ps;
			}
		};

		List<String> result = jdbcTemplate.query(psc, new StringMapper());
		if(result!=null && result.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public User getUserByName(String userName) {
		StringBuilder SQL = new StringBuilder("SELECT USER_ID,USER_NAME,password,UNIT_ID,unit_name,create_time FROM TB_USER ");
		SQL.append("WHERE DELETE_FLAG=0 AND user_name = '").append(userName).append("'");
		List<User> users = this.jdbcTemplate.query(SQL.toString(), new UserMapper());
		if (users !=null && users.size() > 0){
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean update(User user) {
		/*String sql = "update tb_user set user_name='" + userEntity.getUserName()
				+ "',create_time=now(),unit_name='" + userEntity.getUnitName()
				+ "',password='" + userEntity.getPassword();
		sql += "' where user_id= " + userEntity.getUserId();
		try {
			jdbcTemplate.execute(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return false;
		}
		return true;*/
		int res = userMapper.updateByUserId(user);
		return res > 0;
	}

	@Override
	@Transactional
	public boolean allocateRole(String systemId, String userId, String roleId, String createUserId) {

		String getCountSql = "SELECT COUNT(1) FROM tb_user_role ur " +
				"JOIN tb_role r ON ur.role_id = r.role_id " +
				"WHERE ur.user_id = " + userId +
				" AND r.system_id = " + systemId;
		Integer roleCount = jdbcTemplate.queryForObject(getCountSql, Integer.class);
		if (roleCount > 0) {
			//先删除该user下的原有角色关系
			String deleteSql = "UPDATE tb_user_role SET delete_flag = 1 WHERE role_id IN ( " +
					"SELECT * FROM( " +
					"SELECT r.role_id " +
					"FROM tb_user_role ur " +
					"JOIN tb_role r ON ur.role_id = r.role_id " +
					"WHERE ur.user_id = " + userId +
					" AND r.system_id = " + systemId +
					" ) AS x" +
					")";
			try {
				jdbcTemplate.execute(deleteSql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return false;
			}
		}

		//循环添加角色
		String[] roleIds = StringUtils.split(roleId, ",");

		for (String roleIdTemp : roleIds) {
			String insertSql = "INSERT INTO tb_user_role(user_id,role_id,create_time,delete_flag,creator) VALUES (" +
					userId+ "," + roleIdTemp + "," +"now(), 0," + createUserId + ")";
			try {
				jdbcTemplate.execute(insertSql);
			} catch (DataAccessException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}


	private static final class StringMapper implements RowMapper{
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {   
            return rs.getString(1);
        }        
    }
	
	private static final class LongMapper implements RowMapper{   
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {   
            return rs.getLong(1);
        }        
    }
}
