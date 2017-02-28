package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @ClassName: OutdataRequest
 * @Description: 外部数据request
 * @Author guzhenggen
 * @Date: 2016/7/28 14:10
 */
@XmlRootElement
public class OutdataRequest {

    private long systemId;          //系统ID
    private long authorityId;       //权限ID
    private long roleId;            //角色ID
    private long targetId;          //外部数据ID
    private String dataSource;      //外部数据数据源
    private String targetName;      //外部数据名称
    private long userId;            //用户ID
    private String systemSign;      //系统标识
    private String authoritySign;   //权限标识
    private String roleSign;        //角色标识
    private List<Long> userIdList;  //用户ID列表

    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAuthoritySign() {
        return authoritySign;
    }

    public void setAuthoritySign(String authoritySign) {
        this.authoritySign = authoritySign;
    }

    public String getRoleSign() {
        return roleSign;
    }

    public void setRoleSign(String roleSign) {
        this.roleSign = roleSign;
    }

    public String getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }
}
