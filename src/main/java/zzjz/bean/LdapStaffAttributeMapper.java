package zzjz.bean;

import org.springframework.ldap.core.AttributesMapper;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * 将ldap返回的结果，转成指定对象.
 * @author 房桂堂
 * @version  2019/7/30 13:35
 */
public class LdapStaffAttributeMapper implements AttributesMapper {

    @Override
    public Object mapFromAttributes(Attributes attrs) throws NamingException {
        Staff staff = new Staff();
        if (attrs.get("uid") != null) {
            staff.setUserName( attrs.get("uid").get().toString());
        }
        if (attrs.get("uid") != null) {
            staff.setEmail( attrs.get("mail").get().toString());
        }
        if (attrs.get("displayname") != null) {
            staff.setName(attrs.get("displayname").get().toString());
        }
        if (attrs.get("userpassword") != null) {
            byte[] bytes = (byte[]) attrs.get("userpassword").get();
            String pass = new String(bytes);
            staff.setPassword(pass);
        }
        return staff;
    }
}
