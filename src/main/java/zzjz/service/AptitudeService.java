package zzjz.service;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Aptitude;
import zzjz.bean.AptitudeRequest;
import zzjz.bean.Column;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: AptitudeService
 * @Description: 资质证书信息服务接口
 */
@Service
public interface AptitudeService {

    /**
     * 分页获取资质证书信息.
     *
     * @param request 资质证书请求信息
     * @return 列表
     */
    Page<Aptitude> getAptitudePageList(AptitudeRequest request);

    /**
     * 获取资质证书列表.
     *
     * @param aptitude 资质证书信息
     * @return 列表
     */
    List<Aptitude> getAptitudeList(Aptitude aptitude);

    /**
     * 查询资质证书信息.
     *
     * @param id 主键
     * @return 资质证书信息
     */
    Aptitude query(Integer id);

    /**
     * 添加资质证书信息.
     *
     * @param aptitude 资质证书信息
     * @return 结果
     */
    boolean add(Aptitude aptitude);

    /**
     * 更新资质证书信息.
     *
     * @param aptitude 资质证书信息
     * @return 结果
     */
    boolean update(Aptitude aptitude);

    /**
     * 删除资质证书信息.
     *
     * @param id 主键
     * @return 结果
     */
    boolean delete(Integer id);

    /**
     * 获取有效资质证书信息.
     * @param userId 员工号
     * @return 列表
     */
    List<Aptitude> getEffectiveAptitudeList(String userId);

    /**
     * 获取失效资质证书信息.
     * @param userId 员工号
     * @return 列表
     */
    List<Aptitude> getInvalidAptitudeList(String userId);

    /**
     * 获取所有展示列.
     *
     * @return List
     */
    List<Column> getAllColumn();

    /**
     * 根据数组构造导出实体.
     * @param exportArr 导出列数组
     * @return 结果
     */
    List<ExcelExportEntity> getExcelExportEntityByArr(String[] exportArr);

    /**
     * 获取资质列表信息(Map形式).
     *
     * @param request request
     * @return 资质列表信息
     */
    List<Map<String, Object>> getAptitudeMap(AptitudeRequest request);
}
