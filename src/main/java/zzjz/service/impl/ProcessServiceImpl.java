package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Process;
import zzjz.bean.ProcessRequest;
import zzjz.mapper.ProcessMapper;
import zzjz.service.ProcessService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年11月02日14:19
 * @ClassName: ProcessServiceImpl
 * @Description: 流程配置服务实现类
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    ProcessMapper processMapper;

    @Override
    public Page<Process> getPageList(ProcessRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Process> pageList = processMapper.getPageList(request);
        return pageList;
    }

    @Override
    public List<Process> getSelections(Process process) {
        List<Process> list = new ArrayList<>();
        String filePath = this.getClass().getClassLoader().getResource("").getPath() + "\\diagrams";
        if (process.getProcessId() == null) { //获取所有流程
            list = getAllActivitiList(filePath);
        } else if (process.getProcessId() != null && process.getNodeId() == null) { //获取流程中所有节点
            list = getNodeListByParentId(process.getProcessId(), filePath);
        } else if (process.getProcessId() != null && process.getNodeId() != null) { //获取节点中所有用户
            list = getUserListByNodeId(process.getProcessId(), process.getNodeId());
        }
        return list;
    }

    @Override
    public List<Process> getStaffList(String processId, String nodeId) {
        if (processId == null || nodeId == null || "".equals(processId.trim()) || "".equals(nodeId.trim())) {
            return null;
        } else {
            return processMapper.getStaffList(processId, nodeId);
        }
    }

    @Override
    public Process selectById(Integer id) {
        return processMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean insert(Process process) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        process.setCreateTime(createTime);
        return processMapper.insertSelective(process) > 0;
    }

    @Override
    public Boolean update(Process process) {
        return processMapper.updateByPrimaryKeySelective(process) > 0;
    }

    @Override
    public Boolean delete(Integer id) {
        return processMapper.deleteByPrimaryKey(id) > 0;
    }

    private List<Process> getAllActivitiList(String filePath) {
        List<Process> list = new ArrayList<>();
        String[] arr = new String[]{"common", "leave", "out", "over", "punch", "trip", "vacation"};
        File fileDirectory = new File(filePath);
        if (fileDirectory.isDirectory()) {
            String[] filelist = fileDirectory.list();
            if (filelist == null) {
                return null;
            }
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filePath + "\\" + filelist[i]);
                if (readfile.isDirectory() && !Arrays.asList(arr).contains(filelist[i])) {
                    File file = new File(filePath + "\\" + filelist[i] + "\\" + filelist[i] + ".bpmn");
                    SAXReader saxReader = new SAXReader();
                    Document document = null;
                    try {
                        document = saxReader.read(file);
                        // 获取根元素
                        Element root = document.getRootElement();
                        // 获取所有子元素
                        Element element = (Element) root.elements("process").get(0);
                        Process process = new Process();
                        process.setProcessId(element.attributeValue("id"));
                        process.setProcessSign(element.attributeValue("name"));
                        list.add(process);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    private List<Process> getNodeListByParentId(String processId, String filePath) {
        List<Process> list = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new File(filePath + "\\" + processId + "\\" + processId + ".bpmn"));
            // 获取根元素
            Element root = document.getRootElement();
            // 获取所有子元素
            Element element = (Element) root.elements("process").get(0);

            List<Element> childList2 = element.elements("userTask");
            for (Element element1 : childList2) {
                Process process = new Process();
                process.setProcessId(processId);
                process.setNodeId(element1.attributeValue("id"));
                process.setNodeSign(element1.attributeValue("name"));
                list.add(process);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Process> getUserListByNodeId(String processId, String nodeId) {
        List<Process> list = processMapper.getStaffList(processId, nodeId);
        return list;
    }
}
