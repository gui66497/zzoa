package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Notice;
import zzjz.bean.NoticeRequest;
import zzjz.service.NoticeService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class NoticeServiceTest extends JunitBaseTest {

    private static final Integer ID = 1;

    private static final Integer STAFF_ID = 1;

    private static final Integer NOTICE_ID = 1;

    @Autowired
    NoticeService noticeService;

    /**
     * 测试获取告知信息列表.
     */
    @Test
    public void testGetNoticeList() {
        NoticeRequest request = new NoticeRequest();
        List<Notice> list = noticeService.getNoticeList(request);
        assert list != null;
    }

    /**
     * 测试新增告知信息.
     */
    @Test
    public void testAddNoticeInfo() {
        Notice notice = new Notice();
        notice.setId(ID);
        notice.setNoticeId(Long.valueOf(NOTICE_ID));
        notice.setStaffId(Long.valueOf(STAFF_ID));
        notice.setNoticeDesc("1");
        notice.setCreateTime("1");
        notice.setIsRead("0");
        boolean rs = noticeService.addNoticeInfo(notice);
        assert rs;
    }

    /**
     * 测试确认告知信息已读.
     */
    @Test
    public void testReadNoticeInfo() {
        Notice notice = new Notice();
        notice.setId(ID);
        notice.setNoticeId(Long.valueOf(NOTICE_ID));
        notice.setStaffId(Long.valueOf(STAFF_ID));
        notice.setNoticeDesc("1");
        notice.setCreateTime("1");
        notice.setIsRead("0");
        boolean rs = noticeService.readNoticeInfo(notice);
        assert rs;
    }
}
