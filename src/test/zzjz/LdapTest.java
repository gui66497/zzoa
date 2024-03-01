package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import zzjz.bean.LdapStaffAttributeMapper;
import zzjz.bean.Staff;
import zzjz.util.LdapUtil;
import java.util.List;

/**
 * @author 房桂堂
 * @description LdapTest
 * @version  2019/7/30 11:11
 */
public class LdapTest extends JunitBaseTest  {
    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    LdapUtil ldapUtil;

    /**
     * 根据uid 查找单个人员.
     */
    @Test
    public void findUser() {
        //BASE_DC 不用填
        DirContextAdapter obj = (DirContextAdapter) ldapTemplate.lookup("cn=chengcheng.ma,ou=staff,ou=account");
        System.out.println(obj);
    }

    /**
     * 获取所有 内部人员.
     * ou=staff,ou=account
     */
    @Test
    public void listUsers() {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "person"));
        //查询所有内部人员
        List<Staff> staffs = ldapTemplate.search("ou=staff,ou=account", filter.encode(), new LdapStaffAttributeMapper());
        for (Staff staff: staffs ) {
            System.out.println(staff.getUserName());
        }

    }

    @Test
    public void deleteUser() {
        System.out.println(ldapUtil.deleteUser("ggg.ff"));
    }

}
