package zzjz.service;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Column;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.Turnover;
import zzjz.bean.TurnoverRequest;
import java.util.List;
import java.util.Map;

/**
 * 异动Service.
 * @author fgt
 * @version 2017/6/14 8:55
 */
@Service
public interface TurnoverService {

    /**
     * 分页获取员工的异动信息.
     * @param request request
     * @return 异动信息
     */
    Page<Turnover> queryPage(TurnoverRequest request);

    /**
     * 添加异动信息.
     * @param turnover 异动信息
     * @return 结果
     * @throws Exception Exception
     */
    boolean addTurnover(Turnover turnover) throws Exception;

    /**
     * 修改异动信息.
     * @param turnover 异动信息
     * @return 结果
     * @throws Exception Exception
     */
    boolean updateTurnover(Turnover turnover) throws Exception;

    /**
     * 根据id删除信息.
     * @param id id
     * @return 结果
     */
    boolean delById(int id);

    /**
     * 记录异动.
     * @param oldStaff 旧值
     * @param newStaff 新值
     */
    //void record(Staff oldStaff, Staff newStaff);

    /**
     * excel导入员工时初始化异动信息.
     * @param res 员工列表
     * @throws Exception Exception
     */
    void initData(List<StaffExcel> res) throws Exception;

    /**
     * 新增员工时添加异动信息.
     * @param staff 员工
     * @throws Exception Exception
     */
    void addOne(Staff staff) throws Exception;

    /**
     * 根据数组构造导出实体.
     * @param exportArr 导出列数组
     * @return 结果
     */
    List<ExcelExportEntity> getExcelExportEntityByArr(String[] exportArr);

    /**
     * 获取所有展示列.
     *
     * @return List
     */
    List<Column> getAllColumn();

    /**
     * 获取异动列表信息(Map形式).
     *
     * @param request request
     * @return 员工列表信息
     */
    List<Map<String, Object>> getTurnoverMap(TurnoverRequest request);
}
