package zzjz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;
import javax.naming.Name;

/**
 * @Description Ldap工具类.
 * @Author 房桂堂
 * @Date 2019/8/1 9:17
 */
@Component
public class LdapUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapUtil.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    /**
     * 判断是否存在此ldap信息.
     * @param name ldadId
     * @return 结果
     */
    public boolean isInLdap(String name) {
        try {
            DirContextAdapter obj = (DirContextAdapter) ldapTemplate.lookup("cn=" + name + ",ou=staff,ou=account");
        } catch (Exception e) {
            LOGGER.error("Ldap信息查找失败" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除ldap中的用户.
     * @param userName 类似guitang.fang
     * @return 结果
     */
    public boolean deleteUser(String userName) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "account")
                .add("ou", "staff")
                .add("cn", userName)
                .build();
        try {
            ldapTemplate.unbind(dn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
