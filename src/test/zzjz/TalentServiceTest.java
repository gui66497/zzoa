package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.PagingEntity;
import zzjz.bean.Talent;
import zzjz.bean.TalentRequest;
import zzjz.service.TalentService;

/**
 * @author 彭鹏
 * @version 2017/9/30.
 */
public class TalentServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    @Autowired
    TalentService talentService;

    /**
     * 测试添加人才信息.
     */
    @Test
    public void testAddTalentInfo() {
        Talent talent = new Talent();
        talent.setId(ID);
        talent.setUserName("测试");
        talent.setSex("1");
        talent.setEmail("aaa@aaa.com");
        talent.setPhone("15367573434");
        talent.setType("测试工程师");
        talent.setKeyword("测试");
        talent.setSource("前程无忧");
        talent.setArea("上海");
        talent.setWordDesc("aasdsdada");
        talent.setResumeName("测试_20170929162205.mht");
        talent.setCreateTime("2017-09-29 16:22:05");
        talent = talentService.addTalentInfo(talent);
        assert talent != null;
    }

    /**
     * 测试分页获取人才信息列表.
     */
    @Test
    public void testQueryTalentPageList() {
        TalentRequest request = new TalentRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageSize(PAGE_SIZE);
        pagingEntity.setPageNo(1);
        request.setPaging(pagingEntity);
        Page<Talent> page = talentService.queryTalentPageList(request);
        assert page != null;
    }

    /**
     * 测试修改人才信息.
     */
    @Test
    public void testUpdateTalentInfo() {
        Talent talent = talentService.queryTalentById(ID);
        boolean rs = talentService.updateTalentInfo(talent);
        assert rs;
    }

    /**
     * 测试根据主键获取人才信息.
     */
    @Test
    public void testQueryTalentById() {
        Talent talent = talentService.queryTalentById(ID);
        assert talent != null;
    }

    /**
     * 测试根据电话和邮箱查询人才信息.
     */
    @Test
    public void testQueryTalentByPhoneAndEmail() {
        Talent talent = talentService.queryTalentById(ID);
        talent = talentService.queryTalentByPhoneAndEmail(talent.getPhone(), talent.getEmail());
        assert talent != null;
    }

    /**
     * 测试根据主键删除人才信息.
     */
    @Test
    public void testDeleteTalentById() {
        boolean rs = talentService.deleteTalentById(ID);
        assert rs;
    }
}
