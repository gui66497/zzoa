package zzjz.service.impl;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.activiti.engine.IdentityService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zzjz.bean.Bean;
import zzjz.bean.Column;
import zzjz.bean.DepartRole;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;
import zzjz.bean.Turnover;
import zzjz.mapper.ColumnMapper;
import zzjz.mapper.NativePlaceMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.ActivitiIdentifyService;
import zzjz.service.DepartService;
import zzjz.service.StaffService;
import zzjz.util.IdNumberUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author 房桂堂
 * @version 2017/3/2 15:46
 * @ClassName: StaffServiceImpl
 * @Description: 员工服务实现类
 */
@Transactional
@Service
public class StaffServiceImpl implements StaffService {

    private static final Integer INT6 = 6;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private ColumnMapper columnMapper;

    @Autowired
    IdentityService identityService;

    @Autowired
    ActivitiIdentifyService activitiIdentifyService;

    @Autowired
    NativePlaceMapper nativePlaceMapper;

    @Autowired
    DepartService departService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Override
    public Page<Staff> getStaffListPage(StaffRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        //Page<Staff> staffPage = (Page<Staff>) staffMapper.getStaffList(request);
        return (Page<Staff>) staffMapper.getStaffList(request);
    }

    @Override
    public List<Staff> getAllSimpleStaffOnJob() {
        return staffMapper.getSimpleStaffOnJob();
    }

    @Override
    public List<Staff> getAllSimpleStaff() {
        return staffMapper.getSimpleStaff();
    }

    @Override
    public List<StaffExcel> getStaffList(StaffRequest request) {
        List<StaffExcel> staffExcelsList = new ArrayList<>();
        List<Staff> staffList = staffMapper.getStaffList(request);
        for (Staff staff : staffList) {
            StaffExcel staffExcel = new StaffExcel();
            staffExcel.setName(staff.getName());
            staffExcel.setDepartId(staff.getDepartId());
            staffExcel.setPosition(staff.getPosition());
            staffExcel.setEntryDate(staff.getEntryDate());
            staffExcel.setIsFormal(staff.getIsFormal());
            staffExcel.setFormalDate(staff.getFormalDate());
            staffExcel.setCompanyAge(staff.getCompanyAge());
            staffExcel.setIdNumber(staff.getIdNumber());
            staffExcel.setBirth(staff.getBirth() == null ? null : staff.getBirth());
            staffExcel.setAge(staff.getAge());
            staffExcel.setGender(staff.getGender());
            staffExcel.setPreSeniority(staff.getPreSeniority());
            staffExcel.setSeniority(staff.getSeniority() == null ? null : String.valueOf(staff.getSeniority()));
            staffExcel.setPayCard(staff.getPayCard());
            staffExcel.setPoliticalStatus(staff.getPoliticalStatus());
            staffExcel.setJobNumber(staff.getJobNumber());
            staffExcel.setContractStart(staff.getContractStart() == null ? null : staff.getContractStart());
            staffExcel.setContractEnd(staff.getContractEnd() == null ? null : staff.getContractEnd());
            staffExcel.setTel(staff.getTel());
            staffExcel.setGraduateSchool(staff.getGraduateSchool());
            staffExcel.setMajor(staff.getMajor());
            staffExcel.setEducation(staff.getEducation());
            staffExcel.setGraduateDate(staff.getGraduateDate() == null ? null : staff.getGraduateDate());
            staffExcel.setProfessionalLevel(staff.getProfessionalLevel());
            staffExcel.setProfessionalName(staff.getProfessionalName());
            staffExcel.setCertificateValidity(staff.getCertificateValidity() == null ? null : staff.getCertificateValidity());
            staffExcel.setNativePlace(staff.getNativePlace());
            staffExcel.setHomeAddress(staff.getHomeAddress());
            staffExcel.setCensusRegister(staff.getCensusRegister());
            staffExcel.setFileKeep(staff.getFileKeep());
            staffExcel.setEmergencyContactor(staff.getEmergencyContactor());
            staffExcel.setEmergencyTel(staff.getEmergencyTel());
            staffExcel.setCompany(staff.getCompany());
            staffExcel.setOnJob(staff.getOnJob());
            staffExcel.setLeaveTime(staff.getLeaveTime() == null ? null : staff.getLeaveTime());
            staffExcel.setItAttributes(staff.getItAttributes());
            staffExcel.setResearchStaff(staff.getResearchStaff());
            staffExcel.setInAssurance(staff.getInAssurance());
            staffExcelsList.add(staffExcel);
        }
        return staffExcelsList;
    }

    @Override
    public List<Map<String, Object>> getStaffMap(StaffRequest request) {
        return staffMapper.getStaffMap(request);
    }

    @Override
    public List<Staff> getTopStaffList() {
        return staffMapper.getStaffListByDepartId(Long.parseLong("1"));
    }

    @Override
    public boolean addStaff(Staff staff) {
        int res = staffMapper.insertSelective(staff);
        return res > 0;
    }

    @Override
    public List<Column> getAllColumn() {
        //由原来的数据库读取改为写死 fangguitang 20171218
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("staffId", "考勤号", 0));
        columnList.add(new Column("depart", "部门", 1));
        columnList.add(new Column("name", "姓名", 1));
        columnList.add(new Column("userName", "用户名", 0));
        columnList.add(new Column("gender", "性别", 1));
        columnList.add(new Column("position", "岗位", 1));
        columnList.add(new Column("entryDate", "入职日期", 1));
        columnList.add(new Column("education", "学历", 1));
        columnList.add(new Column("major", "专业", 1));
        columnList.add(new Column("onJob", "是否在职", 1));
        columnList.add(new Column("email", "邮箱", 0));
        columnList.add(new Column("duty", "职务", 0));
        columnList.add(new Column("isFormal", "是否转正", 0));
        columnList.add(new Column("formalDate", "转正日期", 0));
        columnList.add(new Column("companyAge", "司龄", 0));
        columnList.add(new Column("probationPeriod", "试用期", 0));
        columnList.add(new Column("idNumber", "身份证号", 0));
        columnList.add(new Column("birth", "出生日期", 0));
        columnList.add(new Column("age", "年龄", 0));
        columnList.add(new Column("preSeniority", "入职前工龄", 0));
        columnList.add(new Column("seniority", "工龄", 0));
        columnList.add(new Column("payCard", "工资卡号", 0));
        columnList.add(new Column("politicalStatus", "政治面貌", 0));
        columnList.add(new Column("jobNumber", "员工号", 0));
        columnList.add(new Column("contractStart", "合同起始日期", 0));
        columnList.add(new Column("contractEnd", "合同结束日期", 0));
        columnList.add(new Column("tel", "电话", 0));
        columnList.add(new Column("graduateSchool", "毕业院校", 0));
        columnList.add(new Column("graduateDate", "毕业日期", 0));
        columnList.add(new Column("professionalLevel", "职称/资格级别", 0));
        columnList.add(new Column("professionalName", "职称/资格名称", 0));
        columnList.add(new Column("certificateValidity", "证书有效期", 0));
        columnList.add(new Column("nativePlace", "籍贯", 0));
        columnList.add(new Column("homeAddress", "家庭住址", 0));
        columnList.add(new Column("censusRegister", "户籍", 0));
        columnList.add(new Column("fileKeep", "档案保管", 0));
        columnList.add(new Column("remark", "备注", 0));
        columnList.add(new Column("emergencyContactor", "紧急联系人", 0));
        columnList.add(new Column("emergencyTel", "联系电话", 0));
        columnList.add(new Column("company", "所属公司", 0));
        columnList.add(new Column("leaveTime", "离职日期", 0));
        columnList.add(new Column("itAttributes", "高新技术人员属性", 0));
        columnList.add(new Column("researchStaff", "高新技术研发人员", 0));
        columnList.add(new Column("post", "双软人员", 0));
        columnList.add(new Column("inAssurance", "参加社保", 0));
        columnList.add(new Column("resumeName", "简历文件", 0));
        columnList.add(new Column("isRemind", "是否需要考勤提醒", 0));
        //return columnMapper.getAllColumn();
        return columnList;
    }

    @Override
    public List<ExcelExportEntity> getExcelExportEntityByArr(String[] exportArr) {
        List<ExcelExportEntity> entities = new ArrayList<>();
        List<Column> columns = getAllColumn();
        for (String one : exportArr) {
            for (Column column : columns) {
                if (column.getColumnName().equals(one)) {
                    ExcelExportEntity excelExportEntity = new ExcelExportEntity(column.getColumnName(), column.getColumnSign());
                    if ("部门".equals(one)) {
                        excelExportEntity.setKey("departName");
                    }
                    if ("性别".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"男_1", "女_2", "未知_null"});
                    }
                    if ("是否转正".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"是_1", "否_0", "未知_null"});
                    }

                    if ("出生日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("合同起始日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("合同结束日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("毕业日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("证书有效期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("是否在职".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"在职_1", "离职_0", "未知_null"});
                    }
                    if ("离职日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("参加社保".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"是_1", "否_0", "未知_null"});
                    }
                    entities.add(excelExportEntity);
                }
            }
        }
        return entities;
    }

    @Override
    public StaffExcel timeFormate(StaffExcel staff) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        //出生日期 转正日期不用转换 因为会计算获得
        if (StringUtils.isNotBlank(staff.getEntryDate())) {
            staff.setEntryDate(sdf.format(new Date(staff.getEntryDate())));
        }
        if (StringUtils.isNotBlank(staff.getContractStart())) {
            staff.setContractStart(sdf.format(new Date(staff.getContractStart())));
        }
        if (StringUtils.isNotBlank(staff.getContractEnd())) {
            staff.setContractEnd(sdf.format(new Date(staff.getContractEnd())));
        }
        if (StringUtils.isNotBlank(staff.getLeaveTime())) {
            staff.setLeaveTime(sdf.format(new Date(staff.getLeaveTime())));
        }
        if (StringUtils.isNotBlank(staff.getGraduateDate())) {
            staff.setGraduateDate(sdf.format(new Date(staff.getGraduateDate())));
        }
        return staff;
    }

    @Override
    public String getSignPath(String name) {
        assert StringUtils.isNotBlank(name);
        String cPath = OutServiceImpl.class.getClassLoader().getResource("").getPath();
        cPath = cPath.replaceAll("WEB-INF/classes/", "");
        String signPath = cPath.substring(1) + "images/sign/" + name + ".png";
        File file = new File(signPath);
        assert file.exists();
        return signPath;
    }

    @Override
    public boolean updateCol(Column column) {
        return columnMapper.updateByName(column) > 0;
    }

    @Override
    public boolean delByStaffId(long staffId) {
        return staffMapper.delByStaffId(staffId) > 0;
    }

    @Override
    public boolean addStaffList(List<StaffExcel> res) {

        return staffMapper.addStaffList(res) > 0;
    }

    @Override
    public boolean uploadStaffs(List<StaffExcel> res) {
        //先删除原有员工信息
        for (StaffExcel staffExcel : res) {
            if (staffExcel.getIsExist() == 1) {
                Staff staffOld = getStaffByName(staffExcel.getName());
                //通过身份证信息判断是否是同一人 如果原来该人之前没有填写身份证信息 也默认是同一人
                if (staffOld.getIdNumber() == null || staffOld.getIdNumber().equals(staffExcel.getIdNumber())) {
                    //设置密码和staffId为之前的
                    staffExcel.setPassword(staffOld.getPassword());
                    staffExcel.setStaffId(staffOld.getStaffId());
                    staffMapper.delByStaffId(staffOld.getStaffId());
                }
            }
        }
        //批量新增有問題 暫時改為單個新增
        for (StaffExcel staffExcel : res) {
            Staff staff = new Staff();
            BeanUtils.copyProperties(staffExcel, staff);
            addStaff(staff);
        }
        return true;
    }

    @Override
    public List<Staff> getformalRemindList() {
        return staffMapper.formalRemindList();
    }

    @Override
    public Integer getformalRemindCount() {
        return staffMapper.formalRemindCount();
    }

    @Override
    public List<Staff> getbirthRemindList() {
        return staffMapper.birthRemindList();
    }

    @Override
    public Integer getbirthRemindCount() {
        return staffMapper.birthReminCount();
    }

    @Override
    public List<Staff> getcontRemindList() {
        return staffMapper.contRemindList();
    }

    @Override
    public Integer getcontRemindCount() {
        return staffMapper.contRemindCount();
    }

    @Override
    public List<Staff> getcertificateRemindList() {
        return staffMapper.certificateRemindList();
    }

    @Override
    public Integer getcertificateRemindCount() {
        return staffMapper.certificateRemindCount();
    }

    @Override
    public boolean login(String userName, String password) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return false;
        }
        int res = staffMapper.verify(userName, password);
        return res > 0;
    }

    @Override
    public Staff getStaffByUserName(String userName, boolean onlyOnJob) {
        StaffRequest staff = new StaffRequest();
        staff.setOnJob(onlyOnJob ? 1 : null);
        staff.setUserName(userName);
        List<Staff> staffList = staffMapper.getStaffListAccurate(staff);
        if (staffList != null && staffList.size() > 0) {
            return staffList.get(0);
        }
        return null;
    }

    @Override
    public Staff getStaffByMail(String mail) {
        StaffRequest staff = new StaffRequest();
        staff.setOnJob(1);
        staff.setEmail(mail);
        List<Staff> staffList = staffMapper.getStaffListAccurate(staff);
        if (staffList != null && staffList.size() > 0) {
            return staffList.get(0);
        }
        return null;
    }


    @Override
    public Staff getStaffByName(String name) {
        StaffRequest staff = new StaffRequest();
        staff.setName(name);
        List<Staff> staffList = staffMapper.getStaffListAccurate(staff);
        if (staffList != null && staffList.size() > 0) {
            return staffList.get(0);
        }
        return null;
    }

    @Override
    public Staff getStaffByUserId(String userId) {
        return staffMapper.selectByUserId(userId);
    }

    @Override
    public Staff getStaffByUserIdAll(String userId) {
        return staffMapper.selectByUserIdAll(userId);
    }

    @Override
    public Staff getStaffWithDefDepartByUserId(String userId) {
        return staffMapper.getStaffWithDefDepartByUserId(userId);
    }

    @Override
    public Staff getStaffWithRoleByUserId(String userId) {
        return staffMapper.getStaffWithRoleByUserId(userId);
    }

    @Override
    public boolean synStaffToAct() {
        //先删除membership表信息 再删除user表和group表
        deleteAllActivitiIdentifyData();

        List<Staff> staffList = staffMapper.getAllStaff();
        for (Staff staff : staffList) {
            saveActivitiUser(staff);
        }
        return true;
    }

    @Override
    public boolean editStaff(Staff staff) {
        int res = staffMapper.updateByStaffId(staff);
        return res > 0;
    }

    @Override
    public Staff completeStaffByIdNum(Staff staff) {
        String idNum = staff.getIdNumber();
        //不合法则跳出
        if (!IdNumberUtil.validateCard(idNum)) {
            return staff;
        }
        //计算户籍 不为空的话
        if (StringUtils.isBlank(staff.getNativePlace())) {
            String numSix = idNum.substring(0, INT6);
            String place = nativePlaceMapper.getPlaceByNum(numSix);
            staff.setNativePlace(place);
        }

        //计算年龄
        int age = IdNumberUtil.getAgeByIdCard(idNum);
        staff.setAge(staff.getAge() == null ? age : staff.getAge());

        //出生日期 yyyMMdd
        String birthDayStr = IdNumberUtil.getBirthByIdCard(idNum);

        //生日
        staff.setBirth(StringUtils.isBlank(staff.getBirth()) ? birthDayStr : staff.getBirth());

        //性别
        String genderStr = IdNumberUtil.getGenderByIdCard(idNum);
        int gender = 0;
        if ("M".equals(genderStr)) {
            gender = 1;
        } else if ("F".equals(genderStr)) {
            gender = 2;
        }
        staff.setGender(staff.getGender() != 0 ? staff.getGender() : gender);
        return staff;
    }

    @Override
    public StaffExcel completeStaffByIdNum(StaffExcel staff) {
        String idNum = staff.getIdNumber();
        //不合法则跳出
        if (!IdNumberUtil.validateCard(idNum)) {
            LOGGER.error("身份证号不合法!!!");
            return staff;
        }
        //计算户籍
        String numSix = idNum.substring(0, INT6);
        String place = nativePlaceMapper.getPlaceByNum(numSix);
        staff.setNativePlace(StringUtils.isBlank(staff.getNativePlace()) ? place : staff.getNativePlace());
        //计算年龄
        int age = IdNumberUtil.getAgeByIdCard(idNum);
        staff.setAge(staff.getAge() > 0 ? staff.getAge() : age);

        //出生日期 yyyMMdd
        String birthDayStr = IdNumberUtil.getBirthByIdCard(idNum);

        //生日
        staff.setBirth(birthDayStr);

        //性别
        String genderStr = IdNumberUtil.getGenderByIdCard(idNum);
        int gender = 0;
        if ("M".equals(genderStr)) {
            gender = 1;
        } else if ("F".equals(genderStr)) {
            gender = 2;
        }
        staff.setGender(gender);
        return staff;
    }

    @Override
    public boolean setDepartRole(long staffId, List<DepartRole> departRoles) {
        //先删除改用户的部门角色关联
        staffMapper.delDepartRoleByStaffId(staffId);
        //添加新关联
        if (departRoles == null) {
            return true;
        }
        for (DepartRole departRole : departRoles) {
            staffMapper.setDepartRole(departRole);
        }
        //如果员工只属于一个部门 就设置该部门为他的默认部门
        if (departRoles.size() == 1) {
            staffMapper.setDefaultDepart(staffId, departRoles.get(0).getDepartId());
        } else {
            for (DepartRole departRole : departRoles) {
                if (departRole.isChecked()) {
                    staffMapper.setDefaultDepart(staffId, departRole.getDepartId());
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public List<Staff> getStaffByDepartAndRole(String roleId, long departId) {
        return staffMapper.getStaffByDepartAndRole(roleId, departId);
    }

    /**
     * 根据员工id和部门id获取员工角色.
     *
     * @param staffId  员工id
     * @param departId 部门id
     * @return 员工角色
     */
    @Override
    public String getStaffByDepartAndUser(String staffId, long departId) {
        String result = "";
        List<DepartRole> list = staffMapper.getStaffByDepartAndUser(staffId, departId);
        if (list != null && list.size() > 0) {
            DepartRole departRole = list.get(0);
            result = departRole.getGroupId();
        }
        return result;
    }

    @Override
    public List<Bean> getAllPostList() {
        List<Bean> list = staffMapper.getAllPostList();
        Collection nuCon = new Vector();
        nuCon.add(null);
        list.removeAll(nuCon);
        return list;
    }

    @Override
    public String getEmailByStaffId(long staffId) {
        return staffMapper.getEmailByStaffId(staffId);
    }

    @Override
    public List<Staff> getStaffByDepart(long departId) {
        return staffMapper.getStaffByDepart(departId);
    }

    @Override
    public boolean delByStaffIds(String[] staffIds) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < staffIds.length; i++) {
            list.add(staffIds[i]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return staffMapper.batchDeleteByStaffIds(map);
    }

    @Override
    public List<Staff> getWithRole(StaffRequest staffRequest) {
        return staffMapper.getStaffListWithRole(staffRequest);
    }

    @Override
    public List<Staff> getDirectorList() {
        return staffMapper.getDirectorList();
    }

    @Override
    public boolean changeStaffByTurnover(Turnover turnover) throws Exception {
        //在执行插入数据之前需要根据异动类型进行相应的操作
        //岗位 职务 部门的变动以及转正,离职,入职
        Staff staff = getStaffByUserIdAll(String.valueOf(turnover.getStaffId()));
        staff.setStaffId(turnover.getStaffId());
        if (turnover.getNewDepart() != null && !turnover.getNewDepart().equals(turnover.getOldDepart())) {
            String newDepartName = turnover.getNewDepart();
            String departId = departService.getDepartIdByName(newDepartName);
            staff.setDepartId(departId);
        }
        if ((turnover.getOldPosition() == null && turnover.getNewPosition() != null) ||
                (turnover.getOldPosition() != null && !turnover.getOldPosition().equals(turnover.getNewPosition()))) {
            staff.setPosition(turnover.getNewPosition());
        }
        if ((turnover.getOldDuty() == null && turnover.getNewDuty() != null) ||
                (turnover.getOldDuty() != null && !turnover.getOldDuty().equals(turnover.getNewDuty()))) {
            staff.setDuty(turnover.getNewDuty());
        }
        if ("转正".equals(turnover.getType())) {
            //导入的时候存在这种情况 注释此段代码
            /*if (staff.getIsFormal() == 1) {
                throw new Exception("员工" + staff.getName() + "已经转正了!");
            }*/
            if ("正常转正".equals(turnover.getAction())) {
                staff.setIsFormal(1);
                staff.setFormalDate(turnover.getEffectTime());
            } else if ("延长转正期1个月".equals(turnover.getAction())) {
                String oldProbationPeriod = staff.getProbationPeriod();
                if (StringUtils.isBlank(oldProbationPeriod) || !oldProbationPeriod.contains("月")) {
                    throw new Exception("该员工的原试用期没有填写!");
                }
                String newProbationPeriod = (Integer.parseInt(oldProbationPeriod.substring(0, oldProbationPeriod.indexOf("月") - 1)) + 1) + "月";
                staff.setProbationPeriod(newProbationPeriod);
            } else if ("提前转正期1个月".equals(turnover.getAction())) {
                String oldProbationPeriod = staff.getProbationPeriod();
                if (StringUtils.isBlank(oldProbationPeriod) || !oldProbationPeriod.contains("月")) {
                    throw new Exception("该员工的原试用期没有填写!");
                }
                int probationInt = Integer.parseInt(oldProbationPeriod.substring(0, oldProbationPeriod.indexOf("月")));
                if (probationInt < 1) {
                    throw new Exception("该员工的试用期已经不能再提前了!");
                }
                String newProbationPeriod = (Integer.parseInt(oldProbationPeriod.substring(0, oldProbationPeriod.indexOf("月") - 1)) - 1) + "月";
                staff.setProbationPeriod(newProbationPeriod);
            }

        } else if ("离职".equals(turnover.getType())) {
            staff.setOnJob(0);
            staff.setLeaveTime(turnover.getEffectTime());
        } else if ("入职".equals(turnover.getType())) {
            //2018-03-13 房桂堂改 导入时如果该员工是兼职 就会出现问题
            //staff.setOnJob(1);
            staff.setEntryDate(turnover.getEffectTime());
        }
        return staffMapper.updateByStaffId(staff) > 0;
        //return false;
    }

    /**
     * 添加一个用户到Activiti
     *
     * @param staff 员工信息
     */
    private void saveActivitiUser(Staff staff) {

        //userName作为id  确保唯一性
        String staffId = staff.getUserName();
        org.activiti.engine.identity.User activitiUser = identityService.newUser(staffId);

        activitiUser.setFirstName(staff.getName());
        activitiUser.setLastName(StringUtils.EMPTY);
        activitiUser.setPassword(staff.getPassword());
        activitiUser.setEmail(StringUtils.EMPTY);
        identityService.saveUser(activitiUser);
        LOGGER.debug("add activiti user: {}", ToStringBuilder.reflectionToString(activitiUser));
    }

    /**
     * 清空工作流用户、角色以及关系
     */
    private void deleteAllActivitiIdentifyData() {
        activitiIdentifyService.deleteAllMemberShip();
        activitiIdentifyService.deleteAllRole();
        activitiIdentifyService.deleteAllUser();
    }
}
