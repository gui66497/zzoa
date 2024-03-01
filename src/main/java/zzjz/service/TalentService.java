package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Talent;
import zzjz.bean.TalentRequest;

import java.io.IOException;

/**
 * @author 彭鹏.
 * @version 2017/7/6
 */
@Service
public interface TalentService {
    /**
     * 分页获取人才信息列表.
     *
     * @param talentRequest TalentRequest
     * @return Page
     */
    Page<Talent> queryTalentPageList(TalentRequest talentRequest);

    /**
     * 添加人才信息.
     *
     * @param talent Talent
     * @return Talent
     */
    Talent addTalentInfo(Talent talent);

    /**
     * 修改人才信息.
     *
     * @param talent Talent
     * @return boolean
     */
    boolean updateTalentInfo(Talent talent);

    /**
     * 根据主键删除人才信息.
     *
     * @param id id
     * @return boolean
     */
    boolean deleteTalentById(Integer id);

    /**
     * 根据主键获取人才信息.
     *
     * @param id id
     * @return Talent
     */
    Talent queryTalentById(Integer id);

    /**
     * 根据电话和邮箱查询人才信息.
     *
     * @param phone phone
     * @param email email
     * @return Talent
     */
    Talent queryTalentByPhoneAndEmail(String phone, String email);

    /**
     * 批量上传简历信息.
     *
     * @param fileName fileName
     * @param source   source
     * @param type     type
     * @param keyword  keyword
     * @param note     note
     * @return String
     * @throws IOException IOException
     */
    String addFile(String fileName, String source, String type, String keyword, String note) throws IOException;
}
