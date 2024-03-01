package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Cont;
import zzjz.bean.ContRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: ContService
 * @Description: 合同信息服务接口
 */
@Service
public interface ContService {

    /**
     * 分页获取合同信息.
     *
     * @param request 请求信息
     * @return 列表
     */
    Page<Cont> getContPageList(ContRequest request);

    /**
     * 获取合同信息.
     *
     * @param cont 合同信息
     * @return 列表
     */
    List<Cont> getContList(Cont cont);

    /**
     * 根据ID获取合同信息.
     *
     * @param id 主键
     * @return 合同信息
     */
    Cont query(Integer id);

    /**
     * 添加合同信息.
     *
     * @param cont 合同信息
     * @return 结果
     */
    boolean add(Cont cont);

    /**
     * 更新合同信息.
     *
     * @param cont 合同信息
     * @return 结果
     */
    boolean update(Cont cont);

    /**
     * 删除合同信息.
     *
     * @param id 主键
     * @return 结果
     */
    boolean delete(Integer id);

    /**
     * 获取有效合同.
     * @param userId 员工号
     * @return 列表
     */
    List<Cont> getEffectiveContList(String userId);

    /**
     * 获取失效合同.
     * @param userId 员工号
     * @return 列表
     */
    List<Cont> getInvalidContList(String userId);
}
