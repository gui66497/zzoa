package zzjz.bean;

/**
 * 打卡机user实体类.
 *
 * @author Administrator
 * @version 2017/7/6 10:19
 */
public class ZkUserInfo {

    private String name;

    private String enabled;

    private String password;

    private String privilege;

    private String uid;

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
     * getEnabled.
     *
     * @return ENABLED
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * setEnabled.
     *
     * @param enabled ENABLED
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * getPassword.
     *
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword.
     *
     * @param password PASSWORD
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getPrivilege.
     *
     * @return PRIVILEGE
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * setPrivilege.
     *
     * @param privilege PRIVILEGE
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /**
     * getUid.
     *
     * @return UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * setUid.
     *
     * @param uid UID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
