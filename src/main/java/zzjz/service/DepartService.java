package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;

/**
 * @author 彭鹏
 * @ClassName: DepartService
 * @Description: 部门服务接口
 * @date 2017年03月31日9:49
 */
@Service
public interface DepartService {
    /**
     * 分页获取部门列表信息
     * @param request request
     * @return 部门列表信息
     */
    Page<Depart> getDepartListPage(DepartRequest request);

    /**
     * 新增部门信息
     * @param depart depart
     * @return 结果
     */
    boolean addDepart(Depart depart);

    boolean updateDepart(Depart deapart);

    /**
     * 根据departId删除部门信息
     * @param Id departId
     * @return 结果
     */
    boolean delByDepartId(Integer Id);
}
