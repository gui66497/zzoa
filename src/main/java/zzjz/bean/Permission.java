package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 * @version 2016/7/26 13:38
 * @ClassName: Permission
 * @Description: 权限外部实体
 */
@XmlRootElement
public class Permission {

    /**
     * 权限ID
     */
    private long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限标识
     */
    private String sign;

    /**
     * 系统ID
     */
    private long systemId;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 系统标识
     */
    private String systemSign;

    /**
     * 外部数据显示名称
     */
    private String targetName = "";

    /**
     * 外部数据ID
     */
    private String targetId = "-1";

    /**
     * getId.
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getName.
     *
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param name NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getSign.
     *
     * @return SIGN
     */
    public String getSign() {
        return sign;
    }

    /**
     * setSign.
     *
     * @param sign SIGN
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * getSystemId.
     *
     * @return SYSTEM_ID
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * setSystemId.
     *
     * @param systemId SYSTEM_ID
     */
    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    /**
     * getSystemName.
     *
     * @return SYSTEM_NAME
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * setSystemName.
     *
     * @param systemName SYSTEM_NAME
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * getSystemSign.
     *
     * @return SYSTEM_SIGN
     */
    public String getSystemSign() {
        return systemSign;
    }

    /**
     * setSystemSign.
     *
     * @param systemSign SYSTEM_SIGN
     */
    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }

    /**
     * getTargetName.
     *
     * @return TARGET_NAME
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * setTargetName.
     *
     * @param targetName TARGET_NAME
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * getTargetId.
     *
     * @return TARGET_ID
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * setTargetId.
     *
     * @param targetId TARGET_ID
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
