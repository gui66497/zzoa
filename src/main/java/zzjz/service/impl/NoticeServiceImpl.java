package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Notice;
import zzjz.bean.NoticeRequest;
import zzjz.mapper.NoticeMapper;
import zzjz.service.NoticeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年08月30日15:10
 * @ClassName: NoticeServiceImpl
 * @Description: 告知实现类
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Integer MINT5 = -5;

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public List<Notice> getNoticeList(NoticeRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, MINT5);
        request.setCreateTime(sdf.format(calendar.getTime()));
        return noticeMapper.getNoticePageList(request);
    }

    @Override
    public boolean addNoticeInfo(Notice notice) {
        return noticeMapper.insert(notice) > 0;
    }

    @Override
    public boolean readNoticeInfo(Notice notice) {
        notice = noticeMapper.selectByPrimaryKey(notice.getId());
        notice.setIsRead("1");
        return noticeMapper.updateByPrimaryKey(notice) > 0;
    }
}
