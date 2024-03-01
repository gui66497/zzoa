package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Process;
import zzjz.bean.ProcessRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年11月02日14:19
 * @ClassName: ProcessService
 * @Description: 流程配置服务接口
 */
@Service
public interface ProcessService {

    /**
     * 分页查询.
     *
     * @param request 请求信息
     * @return Page
     */
    Page<Process> getPageList(ProcessRequest request);

    /**
     * 获取流程配置信息.
     *
     * @param process 请求信息
     * @return 流程配置列表
     */
    List<Process> getSelections(Process process);

    /**
     * 根据流程信息获取用户列表.
     *
     * @param processId 流程号
     * @param nodeId    节点ID
     * @return 用户列表
     */
    List<Process> getStaffList(String processId, String nodeId);

    /**
     * 查询.
     *
     * @param id id
     * @return Process
     */
    Process selectById(Integer id);

    /**
     * 新增.
     *
     * @param process Process
     * @return Boolean
     */
    Boolean insert(Process process);

    /**
     * 修改.
     *
     * @param process Process
     * @return Boolean
     */
    Boolean update(Process process);

    /**
     * 删除.
     *
     * @param id id
     * @return Boolean
     */
    Boolean delete(Integer id);
}
