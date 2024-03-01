package zzjz.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 参考：https://www.jianshu.com/p/3aeb49a9befd.
 * @Author 房桂堂
 * @Description LdapConfiguration
 * @Date 2019/7/30 11:08
 */
@Configuration
public class LdapConfiguration {
    private LdapTemplate ldapTemplate;


    @Value("${ldap.url}")
    String ldapUrl;

    @Value("${ldap.base_dc}")
    String ldapBaseDc;

//    String LDAP_URL = "ldap://192.168.11.155:389";
//    String BASE_DC = "dc=zzjz,dc=com";
    String USER_NAME = "cn=Manager,dc=zzjz,dc=com";
    String PASS_WORD = "ldap#1358";

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        Map<String, Object> config = new HashMap();

        contextSource.setUrl(ldapUrl);
        contextSource.setBase(ldapBaseDc);
        contextSource.setUserDn(USER_NAME);
        contextSource.setPassword(PASS_WORD);

        //  解决 乱码 的关键一句
        config.put("java.naming.ldap.attributes.binary", "objectGUID");

        contextSource.setPooled(true);
        contextSource.setBaseEnvironmentProperties(config);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        if (null == ldapTemplate) {
            ldapTemplate = new LdapTemplate(contextSource());
        }
        return ldapTemplate;
    }
}
