package zzjz.util;

import zzjz.bean.DepartRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author 房桂堂
 *         create 2017-05-15 16:04
 * @version 1.0
 * @className: RoleUtil
 * @description: 这里用一句话描述这个类的作用
 **/

public class RoleUtil {
    private static final int INT3 = 3;

    private static Map<String, Integer> roleMap = new HashMap<String, Integer>() {
        {
            put("user", 0);
            put("hr", 0);
            put("firstLeader", 1);
            put("secondLeader", 2);
            put("thirdLeader", INT3);
        }
    };


    /**
     * 获取上级角色.
     *
     * @param role 角色
     * @return 角色
     */
    public static String getUpperRole2(String role) {
        Integer index = roleMap.get(role);
        for (Map.Entry<String, Integer> entry : roleMap.entrySet()) {
            if (entry.getValue().equals(index + 1)) {
                return entry.getKey();
            }
        }
        return "";
    }

    /**
     * 判断两个角色的大小.
     *
     * @param originRole 元角色
     * @param targetRole 目标角色
     * @return 大小
     */
    public static String judgeBig(String originRole, String targetRole) {
        Integer oInt = roleMap.get(originRole);
        Integer tInt = roleMap.get(targetRole);
        if (oInt != null && tInt != null) {
            if (Objects.equals(oInt, tInt)) {
                return "equals";
            } else if (oInt > tInt) {
                return "bigger";
            } else if (oInt < tInt) {
                return "smaller";
            }
        }
        return "";
    }

    /**
     *
     * @param departRoleList departRoleList
     * @return String
     */
    public static String getTopRole(List<DepartRole> departRoleList) {
        int topIndex = 0;
        String topRole = "";
        for (DepartRole departRole : departRoleList) {
            String role = departRole.getGroupId();
            int index = roleMap.get(role);
            if (index >= topIndex) {
                topIndex = index;
                topRole = role;
            }
        }
        return topRole;
    }

    /*public static void main(String[] args) {
        System.out.println(judgeBig("user", "hr"));

    }*/
}
