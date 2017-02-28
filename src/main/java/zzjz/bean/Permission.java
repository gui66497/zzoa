package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName: Permission
 * @Description: 权限外部实体
 * @author 房桂堂
 * @date 2016/7/26 13:38
 */
@XmlRootElement
public class Permission {

    private long id;                //权限ID
    private String name;            //权限名称
    private String sign;            //权限标识
    private long systemId;          //系统ID
    private String systemName;      //系统名称
    private String systemSign;      //系统标识
    private String targetName = ""; // 外部数据显示名称
    private String targetId = "-1"; // 外部数据ID

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
