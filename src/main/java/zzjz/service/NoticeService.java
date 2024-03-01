package zzjz.service;


import org.springframework.stereotype.Service;
import zzjz.bean.Notice;
import zzjz.bean.NoticeRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年08月30日15:07
 * @ClassName: NoticeService
 * @Description: 告知服务接口
 */
@Service
public interface NoticeService {
    /**
     * 获取告知信息列表.
     *
     * @param request NoticeRequest
     * @return List
     */
    List<Notice> getNoticeList(NoticeRequest request);

    /**
     * 新增告知信息.
     *
     * @param notice Notice
     * @return boolean
     */
    boolean addNoticeInfo(Notice notice);

    /**
     * 确认告知信息已读.
     *
     * @param notice Notice
     * @return boolean
     */
    boolean readNoticeInfo(Notice notice);

}
