package zzjz.service.impl;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Column;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.Turnover;
import zzjz.bean.TurnoverRequest;
import zzjz.mapper.TurnoverMapper;
import zzjz.service.StaffService;
import zzjz.service.TurnoverService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TurnoverServiceImpl.
 *
 * @author fgt
 * @version 2017/6/14 9:00
 */
@Service
public class TurnoverServiceImpl implements TurnoverService {

    @Autowired
    TurnoverMapper turnoverMapper;

    @Autowired
    StaffService staffService;

    @Override
    public Page<Turnover> queryPage(TurnoverRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Turnover> turnoverPage = (Page<Turnover>) turnoverMapper.queryPage(request);
        return turnoverPage;
    }

    @Override
    public boolean addTurnover(Turnover turnover) throws Exception {
        if (turnover.getStaffId() < 1 || StringUtils.isBlank(turnover.getType())) {
            return false;
        }
        //先根据异动信息同步修改员工信息.
        boolean res = staffService.changeStaffByTurnover(turnover);
        if (res) {
            return turnoverMapper.insertSelective(turnover) > 0;
        }
        return false;
    }

    @Override
    public boolean updateTurnover(Turnover turnover) throws Exception {
        if (turnover.getStaffId() < 1 || StringUtils.isBlank(turnover.getType())) {
            return false;
        }
        //先根据异动信息同步修改员工信息.
        boolean res = staffService.changeStaffByTurnover(turnover);
        if (res) {
            return turnoverMapper.updateByPrimaryKey(turnover) > 0;
        }
        return false;
    }

    @Override
    public boolean delById(int id) {
        if (id < 0) {
            return false;
        }
        return turnoverMapper.delById(id) > 0;
    }

    /*@Override
    public void record(Staff oldStaff, Staff newStaff) {
        //员工离职 劳动合同变化
        if (oldStaff.getOnJob() != null && newStaff.getOnJob() != null && oldStaff.getOnJob() == 1 && newStaff.getOnJob() == 0) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(oldStaff.getStaffId());
            turnover.setType("在职情况");
            turnover.setAction("离职");
            turnover.setOldValue("在职");
            turnover.setNewValue("离职");
            if (StringUtils.isNotBlank(newStaff.getLeaveTime())) {
                turnover.setEffectTime(newStaff.getLeaveTime());
            }
            addTurnover(turnover);
        }
        //职位变化
        if (oldStaff.getPosition() != null && newStaff.getPosition() != null && !oldStaff.getPosition().equals(newStaff.getPosition())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(oldStaff.getStaffId());
            turnover.setType("职位");
            turnover.setAction("变化");
            turnover.setOldValue(oldStaff.getPosition());
            turnover.setNewValue(newStaff.getPosition());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowStr = sdf.format(new Date());
            turnover.setEffectTime(nowStr);
            addTurnover(turnover);
        }
        //学历变化
        if (oldStaff.getEducation() != null && newStaff.getEducation() != null && !oldStaff.getEducation().equals(newStaff.getEducation())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(oldStaff.getStaffId());
            turnover.setType("学历");
            turnover.setAction("变化");
            turnover.setOldValue(oldStaff.getEducation());
            turnover.setNewValue(newStaff.getEducation());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowStr = sdf.format(new Date());
            turnover.setEffectTime(nowStr);
            addTurnover(turnover);
        }
        //人员属性变化
        if (oldStaff.getItAttributes() != null && newStaff.getItAttributes() != null
                && !oldStaff.getItAttributes().equals(newStaff.getItAttributes())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(oldStaff.getStaffId());
            turnover.setType("人员属性");
            turnover.setAction("变化");
            turnover.setOldValue(oldStaff.getItAttributes());
            turnover.setNewValue(newStaff.getItAttributes());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowStr = sdf.format(new Date());
            turnover.setEffectTime(nowStr);
            addTurnover(turnover);
        }
    }*/

    @Override
    public void initData(List<StaffExcel> res) throws Exception {
        for (StaffExcel staffExcel : res) {
            //之前不存在的员工才进行异动信息的插入
            if (staffExcel.getIsExist() == 0) {
                Staff staff = new Staff();
                BeanUtils.copyProperties(staffExcel, staff);
                addOne(staff);
            }
        }
    }

    @Override
    public void addOne(Staff staff) throws Exception {
        //入职
        if (StringUtils.isNotBlank(staff.getEntryDate())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(staff.getStaffId());
            turnover.setType("入职");
            turnover.setEffectTime(staff.getEntryDate());
            turnover.setNewDepart(staff.getDepartName());
            turnover.setNewPosition(staff.getPosition());
            turnover.setNewDuty(staff.getDuty());
            addTurnover(turnover);
        }
        //转正
        if (staff.getIsFormal() == 1 && StringUtils.isNotBlank(staff.getFormalDate())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(staff.getStaffId());
            turnover.setType("转正");
            turnover.setAction("正常转正");
            turnover.setEffectTime(staff.getFormalDate());
            turnover.setOldDepart(staff.getDepartName());
            turnover.setOldPosition(staff.getPosition());
            turnover.setOldDuty(staff.getDuty());
            turnover.setNewDepart(staff.getDepartName());
            turnover.setNewPosition(staff.getPosition());
            turnover.setNewDuty(staff.getDuty());
            addTurnover(turnover);
        }
        //离职
        if (staff.getOnJob() == 0 && StringUtils.isNotBlank(staff.getLeaveTime())) {
            Turnover turnover = new Turnover();
            turnover.setStaffId(staff.getStaffId());
            turnover.setType("离职");
            turnover.setEffectTime(staff.getLeaveTime());
            turnover.setOldDepart(staff.getDepartName());
            turnover.setOldPosition(staff.getPosition());
            turnover.setOldDuty(staff.getDuty());
            turnover.setNewDepart(staff.getDepartName());
            turnover.setNewPosition(staff.getPosition());
            turnover.setNewDuty(staff.getDuty());
            addTurnover(turnover);
        }
    }

    @Override
    public List<Column> getAllColumn() {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("staffId", "员工Id", 1));
        columns.add(new Column("staffName", "员工姓名", 1));
        columns.add(new Column("type", "异动类型", 1));
        columns.add(new Column("action", "异动事项", 1));
        columns.add(new Column("effectTime", "生效日期", 1));
        columns.add(new Column("oldDepart", "原部门", 1));
        columns.add(new Column("newDepart", "现部门", 1));
        columns.add(new Column("oldPosition", "原岗位", 1));
        columns.add(new Column("newPosition", "现岗位", 1));
        columns.add(new Column("oldDuty", "原职务", 1));
        columns.add(new Column("newDuty", "现职务", 1));
        columns.add(new Column("createTime", "创建日期", 1));
        return columns;
    }

    @Override
    public List<ExcelExportEntity> getExcelExportEntityByArr(String[] exportArr) {
        List<ExcelExportEntity> entities = new ArrayList<>();
        List<Column> columns = getAllColumn();
        for (String one : exportArr) {
            for (Column column : columns) {
                if (column.getColumnName().equals(one)) {
                    ExcelExportEntity excelExportEntity = new ExcelExportEntity(column.getColumnName(), column.getColumnSign());
                    if ("生效日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("创建日期".equals(one)) {
                        excelExportEntity.setReplace(new String[]{"1901/01/01_null"});
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    entities.add(excelExportEntity);
                }
            }
        }
        return entities;
    }

    @Override
    public List<Map<String, Object>> getTurnoverMap(TurnoverRequest request) {
        return turnoverMapper.getTurnoverMap(request);
    }
}
