package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zzjz.bean.PagingEntity;
import zzjz.bean.Permission;
import zzjz.bean.PermissionInfo;
import zzjz.service.PermissionService;
import zzjz.service.SystemInfoService;
import zzjz.util.ConfigUtil;
import zzjz.util.ConstantUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹雪东
 * @ClassName: PermissionServiceImpl
 * @Description: 权限服务类
 * @date 2015年5月9日 上午9:38:56
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SystemInfoService systemInfoService;

    public long insertPermission(PermissionInfo perInfo) {

        if (null == perInfo) {
            return 0l;
        }
        final String name = perInfo.getName();
        final String sign = perInfo.getSign();
        final String creator = perInfo.getCreatorId();
        final String remark = perInfo.getRemark();
        final long sysId = perInfo.getSystemId();
        final String relation_flag = perInfo.getRelation_flag();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            String sql = "insert into  TB_AUTHORITY "
                    + "(authority_name,authority_sign,creator,create_time,remark,delete_flag,system_id,relation_flag) "
                    + "values (?,?,?,now(),?,0,?,?)";

            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, sign);
                ps.setString(3, creator);
                ps.setString(4, remark);
                ps.setLong(5, sysId);
                ps.setString(6, relation_flag);

                return ps;
            }
        };

        jdbcTemplate.update(psc, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        if (null == generatedId) {
            return 0l;
        }
        return generatedId;

    }

    public int deletePermission(long id, String userId) {

        String sql = "update TB_AUTHORITY set DELETE_FLAG = 1 , create_time=now() ,"
                + "authority_name=CONCAT('@deleted@',authority_name)"
                + ",authority_sign=CONCAT('@deleted@',authority_sign),creator='"
                + userId + "' " + "where authority_id=" + id;

        return jdbcTemplate.update(sql);

    }

    public long updatePenmission(PermissionInfo perInfo) {
        if (null == perInfo) {
            return -1;
        }

        String sql = "update  TB_AUTHORITY  set " + "authority_name='"
                + perInfo.getName() + "'," + "authority_sign='"
                + perInfo.getSign() + "'," + "creator='"
                + perInfo.getCreatorId() + "'," + "create_time=now(),remark='"
                + perInfo.getRemark() + "',"
                // + "system_id="+perInfo.getSystemId() +","
                + " delete_flag=" + perInfo.getDelete_falg() + ", relation_flag = " + perInfo.getRelation_flag()+ ", system_id="+perInfo.getSystemId()
                + " where authority_id=" + perInfo.getId();

        jdbcTemplate.update(sql);

        return perInfo.getId();
    }

    public List<PermissionInfo> queryList(int index, int size) {
        String sql = "select * from TB_AUTHORITY a , TB_SYSTEM s ,tb_user b  where "
                + "a.system_id=s.system_id and  a.delete_flag=s.delete_flag  and "
                + "a.delete_flag=0 and a.creator=b.USER_ID 	 "
                + " limit "
                + index + "," + size;
        ;

        List<PermissionInfo> list = new ArrayList<PermissionInfo>();
        list = jdbcTemplate.query(sql, new PermissionMapper());

        return list;
    }

    public List<PermissionInfo> queryList(int index, int size, long sysId) {
        String sql = "select * from TB_AUTHORITY a , TB_SYSTEM s ,tb_user b  where "
                + "a.system_id=s.system_id and  a.delete_flag=s.delete_flag  and "
                + "a.delete_flag=0 and a.creator=b.USER_ID and  a.system_id="
                + sysId + " limit " + index + "," + size;
        List<PermissionInfo> list = new ArrayList<PermissionInfo>();
        list = jdbcTemplate.query(sql, new PermissionMapper());

        return list;
    }

    private static final class PermissionMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PermissionInfo p = new PermissionInfo();
            p.setId(rs.getLong("authority_id"));
            p.setName(rs.getString("authority_name"));
            p.setSystemId(rs.getLong("system_id"));
            try {
                p.setSystemName(rs.getString("system_name"));
                p.setCreatorName(rs.getString("USER_NAME"));
            } catch (Exception e) {
            }
            p.setSign(rs.getString("authority_sign"));
            p.setRemark(rs.getString("remark"));
            p.setCreateTime(rs.getString("create_time"));
            p.setCreatorId(rs.getString("creator"));
            p.setRelation_flag(rs.getString("relation_flag"));
            return p;
        }
    }

    private static final class ObjectPermissionMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PermissionInfo p = new PermissionInfo();
            p.setId(rs.getLong("authority_id"));
            p.setName(rs.getString("authority_name"));
            p.setSystemId(rs.getLong("system_id"));
            p.setSign(rs.getString("authority_sign"));
            p.setRemark(rs.getString("remark"));

            return p;
        }
    }

    @SuppressWarnings("unchecked")
    public PermissionInfo checkUnique(String name, String sign, String systemId) {

        String sql = "select * from tb_authority where authority_name='" + name
                + "' and authority_sign='" + sign + "' and  system_id="
                + systemId;
        List<PermissionInfo> rs = jdbcTemplate.query(sql,
                new PermissionMapper());
        if (rs.size() == 0) {
            return null;
        }
        return rs.get(0);
    }

    public int getTotalCount(String deleteFlag) {
        String sql = "select count(1) from TB_AUTHORITY a,TB_SYSTEM s,tb_user b WHERE a.system_id = s.system_id AND a.delete_flag = s.delete_flag  AND a.creator = b.USER_ID AND a.delete_flag="
                + deleteFlag;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public PermissionInfo queryById(long id, int deleteFlag) {

        String sql = "select * from TB_AUTHORITY a , TB_SYSTEM s ,tb_user b where "
                + "a.system_id=s.system_id and  s.delete_flag=0  and "
                + "a.delete_flag="
                + deleteFlag
                + " and a.creator=b.USER_ID and  authority_id=" + id;

        List<PermissionInfo> list = jdbcTemplate.query(sql,
                new PermissionMapper());
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public int getTotalCountBySystemId(long systemId, String deleteFlag) {
        String sql = "select count(1) from TB_AUTHORITY where system_id="
                + systemId + " and delete_flag=" + deleteFlag;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<PermissionInfo> getPermissionInfoListByTarget(String userId,
                                                              String targetId, String systemName, String systemSign) {
        String SQL = "SELECT * FROM tb_authority WHERE authority_id IN"
                + "(SELECT authority_id FROM tb_role_authority WHERE role_id IN"
                + "(SELECT role_id FROM tb_user_role WHERE user_id='" + userId
                + "' AND delete_flag=0) " + "AND target_id = '" + targetId
                + "' AND delete_flag=0) AND system_id="
                + "(SELECT system_id FROM tb_system WHERE system_name='"
                + systemName + "' " + "AND system_sign='" + systemSign
                + "' AND delete_flag = 0) AND delete_flag = 0";

        return jdbcTemplate.query(SQL, new PermissionMapper());
        // return this.jdbcTemplate.query(SQL, new PermissionMapper());
    }

    public List<PermissionInfo> getPermissionInfoList(String userId,
                                                      String systemName, String systemSign) {
        String SQL = "SELECT * FROM tb_authority WHERE authority_id IN"
                + "(SELECT authority_id FROM tb_role_authority WHERE role_id IN"
                + "(SELECT role_id FROM tb_user_role WHERE user_id='" + userId
                + "' AND delete_flag=0) "
                + " AND delete_flag=0) AND system_id="
                + "(SELECT system_id FROM tb_system WHERE system_name='"
                + systemName + "' " + "AND system_sign='" + systemSign
                + "' AND delete_flag = 0) AND delete_flag = 0";

        return jdbcTemplate.query(SQL, new PermissionMapper());
        // return this.jdbcTemplate.query(SQL, new PermissionMapper());
    }

    public List<Object> getCategoryList(String userId, String permissionName,
                                        String systemName, String systemSign) {
        String SQL = "SELECT target_id FROM tb_role_authority WHERE role_id IN"
                + "(SELECT role_id FROM tb_user_role WHERE user_id='" + userId
                + "' AND delete_flag=0) "
                + "AND authority_id =(SELECT authority_id FROM tb_authority "
                + "WHERE authority_name='" + permissionName
                + "' AND system_id = (SELECT system_id FROM tb_system "
                + "WHERE system_name='" + systemName + "' AND system_sign='"
                + systemSign + "' AND delete_flag =0) "
                + "AND delete_flag = 0) AND delete_flag=0";

        return jdbcTemplate.queryForList(SQL, Object.class);

    }
    public List<String> getAuthorityName(String userId,Long systemId,String deleteFlag){
        String sql = "SELECT " +
                "ay.authority_name" +
                " FROM " +
                "tb_user_role ur" +
                " LEFT JOIN tb_role_authority ra ON ra.role_id = ur.role_id" +
                " AND " +
                "ur.user_id = '"+userId+"' ";
                if(systemId!=-1) {
                    sql =  sql + "AND ra.target_id = '"+systemId+"'";
                }
                sql =  sql +   " LEFT JOIN tb_authority ay ON ra.authority_id = ay.authority_id" +
                " WHERE " +
                "ay.delete_flag = " +deleteFlag;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<Permission> getPermissionsByUserIdAndSysId(long systemId, String userId) {
        String sql = "SELECT a.authority_id,a.authority_name,a.authority_sign,s.system_id,s.system_name,s.system_sign,ra.target_id,ra.target_name " +
                "FROM tb_user_role ur " +
                "JOIN tb_role r ON ur.role_id = r.role_id " +
                "JOIN tb_system s ON r.system_id = s.system_id " +
                "LEFT JOIN tb_role_authority ra ON r.role_id = ra.role_id " +
                "JOIN tb_authority a ON ra.authority_id = a.authority_id " +
                "WHERE 1=1 " +
                "AND ur.user_id = '" + userId + "' "+
                "AND a.delete_flag = 0 " +
                "AND ra.delete_flag = 0 " +
                "AND ur.delete_flag = 0 " +
                "AND s.delete_flag = 0 " +
                "AND r.delete_flag = 0 ";

        if (systemId > -1) {
            sql += " AND r.system_id = " + systemId;
        }
        return jdbcTemplate.query(sql, new PermissionOutMapper());
    }


    public List<String> getTargetIdByPermissionName(String userId,
                                                    String systemName, String systemSign, String permissionName) {
        String SQL = "SELECT target_id FROM tb_role_authority WHERE role_id IN"
                + "(SELECT role_id FROM tb_user_role WHERE user_id='" + userId
                + "' AND delete_flag=0) "
                + "AND authority_id =(SELECT authority_id FROM tb_authority "
                + "WHERE authority_name='" + permissionName
                + "' AND system_id = (SELECT system_id FROM tb_system "
                + "WHERE system_name='" + systemName + "' AND system_sign='"
                + systemSign + "' AND delete_flag =0) "
                + "AND delete_flag = 0) AND delete_flag=0";

        return jdbcTemplate.queryForList(SQL, String.class);
    }

    public List<String> getTargetIdByPermissionName(String userId,
                                                    String permissionName) {
        String systemName = ConstantUtil.getSystemName();
        String systemSign = ConstantUtil.getSystemSign();
        return getTargetIdByPermissionName(userId, systemName, systemSign,
                permissionName);
    }


    public boolean checkPermission(String userId, String systemName,
                                   String systemSign, String targetId, String permissionName) {
        ConfigUtil.load("system.properties");
        String admin = ConfigUtil.getProperty("tyqxgl.administrator");
        if (userId.equals(admin)) {// 如果是超管可以有任何权限
            return true;
        }
        if (StringUtils.isEmpty(permissionName)) {// 如果权限名称为空，则没有权限
            return false;
        }
        if (permissionName.contains("查看") || permissionName.equals("更新用户角色")
                || !systemInfoService.checkDefaultSystem(targetId)) {// 如果是查看权限或者更新用户角色权限或者不是默认系统则根据权限表返回，否者返回无权限
            String SQL = "SELECT target_id FROM tb_role_authority WHERE role_id IN"
                    + "(SELECT role_id FROM tb_user_role WHERE user_id='"
                    + userId
                    + "' AND delete_flag=0) "
                    + "AND authority_id =(SELECT authority_id FROM tb_authority "
                    + "WHERE authority_name='"
                    + permissionName
                    + "' AND system_id = (SELECT system_id FROM tb_system "
                    + "WHERE system_name='"
                    + systemName
                    + "' AND system_sign='"
                    + systemSign
                    + "' AND delete_flag =0) "
                    + "AND delete_flag = 0) AND delete_flag=0 AND target_id='"
                    + targetId
                    + "'"
                    + "UNION"
                    + "	SELECT"
                    + "		system_id"
                    + "	FROM"
                    + "		tb_system"
                    + "	WHERE"
                    + "		delete_flag = 0"
                    + "	AND system_id = "
                    + targetId
                    + "	AND creator = '"
                    + userId + "'";

            List<String> rs = jdbcTemplate.queryForList(SQL, String.class);
            if (rs.size() == 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean checkPermission(String userId, String targetId,
                                   String permissionName) {
        String systemName = ConstantUtil.getSystemName();
        String systemSign = ConstantUtil.getSystemSign();
        return checkPermission(userId, systemName, systemSign, targetId,
                permissionName);
    }

    @Override
    public List<String> getSystemSignList(String userId, String permissionName,
                                          String systemName, String systemSign) {
        String sql = "select system_sign from tb_system f,(select a.target_id from tb_role_authority a,"
                + " tb_user_role b,tb_authority c,tb_system d"
                + " where a.delete_flag=0 and b.delete_flag=0 and c.delete_flag=0"
                + " and d.delete_flag=0 and a.role_id=b.role_id"
                + " and a.authority_id=c.authority_id and c.system_id=d.system_id"
                + " and b.user_id='"
                + userId
                + "' and c.authority_name='"
                + permissionName
                + "' and d.system_name='"
                + systemName
                + "' and d.system_sign='"
                + systemSign
                + "') e"
                + " where f.system_id=e.target_id and f.delete_flag=0"
                // + deleteFlag
                // + " limit " + index + "," + pageSize
                ;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Map<String, String> getColumnValues(long id) {
        String sql = "select a.* from tb_authority a where a.authority_id="
                + id;
        List<Map<String, String>> list = jdbcTemplate.query(sql,
                new ColumnMapper());
        if (0 == list.size()) {
            return new HashMap<String, String>();
        }
        return list.get(0);
    }

    private static final class ColumnMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, String> map = new HashMap<String, String>();
            map.put("create_time", rs.getString("create_time"));
            map.put("creator", rs.getString("creator"));
            map.put("delete_flag", rs.getString("delete_flag"));
            map.put("remark", rs.getString("remark"));
            map.put("authority_id", rs.getString("authority_id"));
            map.put("authority_name", rs.getString("authority_name"));
            map.put("authority_sign", rs.getString("authority_sign"));
            map.put("system_id", rs.getString("system_id"));
            return map;
        }

    }

    public void delete(long id) {
        String sql = "delete from tb_authority where authority_id=" + id;
        jdbcTemplate.update(sql);
    }

    public int getTotalCountByRoleId(long roleId, String deleteFlag) {
        String sql = "select count(1) from tb_role_authority where role_id="
                + roleId + " and delete_flag=" + deleteFlag;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getPermissionInfoByRoleId(long roleId, String deleteFlag, String relation_flag) {
        String sql = "select b.authority_id,CONCAT(b.authority_name,'-',count(*)) content"
                + " from tb_role_authority a,tb_authority b where a.authority_id=b.authority_id"
                + " and b.delete_flag=" + deleteFlag + " and a.delete_flag=0 and a.role_id=" + roleId + " and relation_flag=" + relation_flag
                + " GROUP BY b.authority_id,b.authority_name ORDER BY b.authority_id";
        return jdbcTemplate.query(sql, new MapMapper());
    }

	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getPermissionInfoByRoleId1(long roleId, String deleteFlag,String relation_flag) {
		String sql = "select b.authority_id,b.authority_name content"
				+ " from tb_role_authority a,tb_authority b where a.authority_id=b.authority_id"
				+ " and b.delete_flag=" + deleteFlag + " and a.delete_flag=0 and a.role_id=" + roleId+" and relation_flag="+relation_flag
				+ " GROUP BY b.authority_id,b.authority_name ORDER BY b.authority_id";
		return jdbcTemplate.query(sql, new MapMapper());
	}	
	private static final class MapMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,String> map = new HashMap<String, String>();
			map.put("id", rs.getString("AUTHORITY_ID"));
			map.put("content", rs.getString("CONTENT"));
			return map;
		}
	}

    @Override
    public boolean hasPermissionInSystem(long autorityId, long systemId) {
        String sql = "SELECT count(1) FROM tb_authority WHERE delete_flag=0 AND authority_id="+autorityId
                +" AND system_id="+systemId;
        return jdbcTemplate.queryForList(sql, Integer.class).get(0) > 0 ? true : false;
    }

    public List<PermissionInfo> getPermissionInfoListByRoleId(long roleId, PagingEntity pagingEntity) {
		int pageNo = pagingEntity.getPageNo();
		int pageSize = pagingEntity.getPageSize();
		int index = (pageNo - 1) * pageSize;
		String sql = "select b.authority_id,b.authority_name,b.authority_sign,a.target_id,a.target_name"
				+ " from tb_role_authority a,tb_authority b where a.authority_id=b.authority_id"
				+ " and b.delete_flag=0 and a.delete_flag=0 and a.role_id=" + roleId
				+ " ORDER BY b.authority_id LIMIT "
				+ index + "," + pageSize;
		return jdbcTemplate.query(sql, new PermissionInfoMapper());
	}

	public int getPermissionInfoListCountByRoleId(long roleId) {
		String sql = "select COUNT(1)"
				+ " from tb_role_authority a,tb_authority b where a.authority_id=b.authority_id"
				+ " and b.delete_flag=0 and a.delete_flag=0 and a.role_id=" + roleId
				+ " ORDER BY b.authority_id";
		List<Integer> counts = jdbcTemplate.queryForList(sql,Integer.class);
		if(counts != null && counts.size() > 0){
			return counts.get(0);
		}
		return 0;
	}

    public long getPermissionId(String sign, long systemId) {

        String sql = "select authority_id from TB_AUTHORITY where delete_flag= 0 and authority_sign=" + sign
                + " and system_id = "+ sign;

        List<Long> idList = jdbcTemplate.queryForList(sql, Long.class);
        if (idList != null && idList.size() > 0) {
            return idList.get(0);
        }
        return 0;
    }

    private static final class PermissionOutMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            Permission permission = new Permission();
            permission.setId(rs.getLong("authority_id"));
            permission.setName(rs.getString("authority_name"));
            permission.setSign(rs.getString("authority_sign"));
            permission.setSystemId(rs.getLong("system_id"));
            permission.setSystemName(rs.getString("system_name"));
            permission.setSign(rs.getString("system_sign"));
            permission.setTargetId(rs.getString("target_id"));
            permission.setTargetName(rs.getString("target_name"));
            return permission;
        }

    }

	private static final class PermissionInfoMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			PermissionInfo permissionInfo = new PermissionInfo();
			permissionInfo.setId(rs.getLong("authority_id"));
			permissionInfo.setName(rs.getString("authority_name"));
			permissionInfo.setSign(rs.getString("authority_sign"));
			permissionInfo.setTargetId(rs.getString("target_id"));
			permissionInfo.setTargetName(rs.getString("target_name"));
			return permissionInfo;
		}

	}




}
