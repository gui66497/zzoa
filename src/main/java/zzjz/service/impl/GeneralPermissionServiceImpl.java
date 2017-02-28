package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import zzjz.bean.PermissionInfo;
import zzjz.service.GeneralPermissionService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
public class GeneralPermissionServiceImpl implements GeneralPermissionService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	public List<PermissionInfo> getPermissionInfoList(String userId,String systemId) {
		String SQL = "select distinct c.* from tb_user_role a,tb_role_authority b,"
				+ " tb_authority c where a.role_id=b.role_id"
				+ " and b.authority_id=c.authority_id and a.delete_flag=0"
				+ " and b.delete_flag=0 and c.delete_flag=0 and a.user_id=" + userId
				+ " and c.system_id=" + systemId;
		
		return this.jdbcTemplate.query(SQL, new PermissionMapper());
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private static final class PermissionMapper implements RowMapper{   
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {   
        	PermissionInfo p = new PermissionInfo();
        	p.setId(rs.getLong("authority_id"));
        	p.setName(rs.getString("authority_name"));
        	p.setSign(rs.getString("authority_sign"));
        	p.setRemark(rs.getString("remark"));
        	p.setSystemId(rs.getLong("system_id"));
            return p;
        }        
    }

	@Override
	public boolean checkPermission(String userId, String permissionName,
			String systemId) {
		String sql = "select distinct c.* from tb_user_role a,tb_role_authority b,"
				+ " tb_authority c where a.role_id=b.role_id"
				+ " and b.authority_id=c.authority_id and a.delete_flag=0"
				+ " and b.delete_flag=0 and c.delete_flag=0 and a.user_id=" + userId
				+ " and c.system_id=" + systemId + " and c.authority_name='" + permissionName
				+ "'";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if(count == 0){
			return false;
		}
		return true;
	}
	
}
