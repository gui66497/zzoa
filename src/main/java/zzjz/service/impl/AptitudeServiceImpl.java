package zzjz.service.impl;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Aptitude;
import zzjz.bean.AptitudeRequest;
import zzjz.bean.Column;
import zzjz.bean.PagingEntity;
import zzjz.mapper.AptitudeMapper;
import zzjz.service.AptitudeService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: AptitudeService
 * @Description: 资质证书信息服务接口实现类
 */
@Service
public class AptitudeServiceImpl implements AptitudeService {

    @Autowired
    AptitudeMapper aptitudeMapper;

    @Override
    public Page<Aptitude> getAptitudePageList(AptitudeRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Aptitude> pageList = (Page<Aptitude>) aptitudeMapper.getAptitudePageList(request);
        return pageList;
    }

    @Override
    public List<Aptitude> getAptitudeList(Aptitude aptitude) {
        return aptitudeMapper.getAptitudeList(aptitude);
    }

    @Override
    public Aptitude query(Integer id) {
        return aptitudeMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean add(Aptitude aptitude) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        aptitude.setCreateDate(sdf.format(new Date()));
        return aptitudeMapper.insertSelective(aptitude) > 0;
    }

    @Override
    public boolean update(Aptitude aptitude) {
        return aptitudeMapper.updateByPrimaryKeySelective(aptitude) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return aptitudeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<Aptitude> getEffectiveAptitudeList(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkDate = sdf.format(new Date());
        return aptitudeMapper.getEffectiveAptitudeList(userId, checkDate);
    }

    @Override
    public List<Aptitude> getInvalidAptitudeList(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkDate = sdf.format(new Date());
        return aptitudeMapper.getInvalidAptitudeList(userId, checkDate);
    }

    @Override
    public List<Column> getAllColumn() {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("userName", "持有人", 1));
        columns.add(new Column("aptitudeType", "资质类型", 1));
        columns.add(new Column("certificateType", "证书类型", 1));
        columns.add(new Column("certificateName", "证书名称", 1));
        columns.add(new Column("certificateId", "证书编号", 1));
        columns.add(new Column("certificateLevel", "证书等级", 1));
        columns.add(new Column("validity", "有效期", 1));
        columns.add(new Column("beginDate", "起始日期", 1));
        columns.add(new Column("endDate", "截止日期", 1));
        columns.add(new Column("createDate", "登记时间", 1));
        columns.add(new Column("fileType", "保存形式", 1));
        columns.add(new Column("fileStatus", "保存状态", 1));
        columns.add(new Column("fileName", "文件名称", 1));
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
                    if ("起始日期".equals(one)) {
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("截止日期".equals(one)) {
                        excelExportEntity.setFormat("yyyy/MM/dd");
                        excelExportEntity.setDatabaseFormat("yyyy-MM-dd");
                    }
                    if ("登记时间".equals(one)) {
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
    public List<Map<String, Object>> getAptitudeMap(AptitudeRequest request) {
        return aptitudeMapper.getAptitudeMap(request);
    }
}
