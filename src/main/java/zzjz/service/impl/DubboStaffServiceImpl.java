package zzjz.service.impl;

import com.dubbo.dubbointerface.DubboStaffService;
import com.dubbo.entity.Staff;
import com.dubbo.utils.POTransformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.StaffRequest;
import zzjz.mapper.StaffMapper;

import java.util.List;

public class DubboStaffServiceImpl implements DubboStaffService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboStaffServiceImpl.class);

    @Autowired
    private StaffMapper staffMapper;

    String mailSuffix = "@zzjunzhi.com";

    @Override
    public Staff getStaffByMail(String loginName) {
        Staff s = new Staff();
        StaffRequest staff = new StaffRequest();
        String mail = loginName + mailSuffix;
        staff.setEmail(mail);
        List<zzjz.bean.Staff> staffList = staffMapper.getStaffListAccurate(staff);
        if (staffList != null && staffList.size() > 0) {
            zzjz.bean.Staff staffZZ = staffList.get(0);
            LOGGER.info("开始调用DubboStaffServiceImpl,用户全名为："+staffZZ.getName());
            //实例转换
            POTransformUtils poTransformUtils = POTransformUtils.getInstance();
            return (Staff) poTransformUtils.poTransformer(s, staffZZ);
        }
        return null;
    }
}
