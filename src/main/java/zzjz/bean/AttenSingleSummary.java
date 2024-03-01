package zzjz.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月02日17:04
 * @ClassName: AttenSingleSummary
 * @Description: 单项事项统计关系
 */
@XmlRootElement
public class AttenSingleSummary {

    @Excel(name = "ID")
    private String staffId;

    @Excel(name = "员工姓名")
    private String name;

    @Excel(name = "部门")
    private String departId;

    @Excel(name = "三月")
    private Double month03;

    @Excel(name = "四月")
    private Double month04;

    @Excel(name = "五月")
    private Double month05;

    @Excel(name = "六月")
    private Double month06;

    @Excel(name = "七月")
    private Double month07;

    @Excel(name = "八月")
    private Double month08;

    @Excel(name = "九月")
    private Double month09;

    @Excel(name = "十月")
    private Double month10;

    @Excel(name = "十一月")
    private Double month11;

    @Excel(name = "十二月")
    private Double month12;

    @Excel(name = "一月")
    private Double month01;

    @Excel(name = "二月")
    private Double month02;

    @Excel(name = "共计")
    private Double month;
    //年假

    @Excel(name = "总年假")
    private Double monthAll;

    @Excel(name = "剩余年假")
    private Double monthLeft;

    @Excel(name = "共计")
    private Double monthUse;

    /**
     * 加班
     */
    private Double over01;

    private Double over02;

    private Double over03;

    private Double over04;

    private Double over05;

    private Double over06;

    private Double over07;

    private Double over08;

    private Double over09;

    private Double over10;

    private Double over11;

    private Double over12;

    /**
     * 调休剩余
     */
    private Double left01;

    private Double left02;

    private Double left03;

    private Double left04;

    private Double left05;

    private Double left06;

    private Double left07;

    private Double left08;

    private Double left09;

    private Double left10;

    private Double left11;

    private Double left12;

    /**
     * getStaffId.
     *
     * @return STAFF_ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * setStaffId.
     *
     * @param staffId STAFF_ID
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * getName.
     *
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param name NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getDepartId.
     *
     * @return DEPART_ID
     */
    public String getDepartId() {
        return departId;
    }

    /**
     * setDepartId.
     *
     * @param departId DEPART_ID
     */
    public void setDepartId(String departId) {
        this.departId = departId;
    }

    /**
     * getMonth03.
     *
     * @return MONTH03
     */
    public Double getMonth03() {
        return month03;
    }

    /**
     * setMonth03.
     *
     * @param month03 MONTH03
     */
    public void setMonth03(Double month03) {
        this.month03 = month03;
    }

    /**
     * getMonth04.
     *
     * @return MONTH04
     */
    public Double getMonth04() {
        return month04;
    }

    /**
     * setMonth04.
     *
     * @param month04 MONTH04
     */
    public void setMonth04(Double month04) {
        this.month04 = month04;
    }

    /**
     * getMonth05.
     *
     * @return MONTH05
     */
    public Double getMonth05() {
        return month05;
    }

    /**
     * setMonth05.
     *
     * @param month05 MONTH05
     */
    public void setMonth05(Double month05) {
        this.month05 = month05;
    }

    /**
     * getMonth06.
     *
     * @return MONTH06
     */
    public Double getMonth06() {
        return month06;
    }

    /**
     * setMonth06.
     *
     * @param month06 MONTH06
     */
    public void setMonth06(Double month06) {
        this.month06 = month06;
    }

    /**
     * getMonth07.
     *
     * @return MONTH07
     */
    public Double getMonth07() {
        return month07;
    }

    /**
     * setMonth07.
     *
     * @param month07 MONTH07
     */
    public void setMonth07(Double month07) {
        this.month07 = month07;
    }

    /**
     * getMonth08.
     *
     * @return MONTH08
     */
    public Double getMonth08() {
        return month08;
    }

    /**
     * setMonth08.
     *
     * @param month08 MONTH08
     */
    public void setMonth08(Double month08) {
        this.month08 = month08;
    }

    /**
     * getMonth09.
     *
     * @return MONTH09
     */
    public Double getMonth09() {
        return month09;
    }

    /**
     * setMonth09.
     *
     * @param month09 MONTH09
     */
    public void setMonth09(Double month09) {
        this.month09 = month09;
    }

    /**
     * getMonth10.
     *
     * @return MONTH10
     */
    public Double getMonth10() {
        return month10;
    }

    /**
     * setMonth10.
     *
     * @param month10 MONTH10
     */
    public void setMonth10(Double month10) {
        this.month10 = month10;
    }

    /**
     * getMonth11.
     *
     * @return MONTH11
     */
    public Double getMonth11() {
        return month11;
    }

    /**
     * setMonth11.
     *
     * @param month11 MONTH11
     */
    public void setMonth11(Double month11) {
        this.month11 = month11;
    }

    /**
     * getMonth12.
     *
     * @return MONTH12
     */
    public Double getMonth12() {
        return month12;
    }

    /**
     * setMonth12.
     *
     * @param month12 MONTH12
     */
    public void setMonth12(Double month12) {
        this.month12 = month12;
    }

    /**
     * getMonth01.
     *
     * @return MONTH01
     */
    public Double getMonth01() {
        return month01;
    }

    /**
     * setMonth01.
     *
     * @param month01 MONTH01
     */
    public void setMonth01(Double month01) {
        this.month01 = month01;
    }

    /**
     * getMonth02.
     *
     * @return MONTH02
     */
    public Double getMonth02() {
        return month02;
    }

    /**
     * setMonth02.
     *
     * @param month02 MONTH02
     */
    public void setMonth02(Double month02) {
        this.month02 = month02;
    }

    /**
     * getMonth.
     *
     * @return MONTH
     */
    public Double getMonth() {
        return month;
    }

    /**
     * setMonth.
     *
     * @param month MONTH
     */
    public void setMonth(Double month) {
        this.month = month;
    }

    /**
     * getMonthAll.
     *
     * @return MONTH_ALL
     */
    public Double getMonthAll() {
        return monthAll;
    }

    /**
     * setMonthAll.
     *
     * @param monthAll MONTH_ALL
     */
    public void setMonthAll(Double monthAll) {
        this.monthAll = monthAll;
    }

    /**
     * getMonthLeft.
     *
     * @return MONTH_LEFT
     */
    public Double getMonthLeft() {
        return monthLeft;
    }

    /**
     * setMonthLeft.
     *
     * @param monthLeft MONTH_LEFT
     */
    public void setMonthLeft(Double monthLeft) {
        this.monthLeft = monthLeft;
    }

    /**
     * getMonthUse.
     *
     * @return MONTH_USE
     */
    public Double getMonthUse() {
        return monthUse;
    }

    /**
     * setMonthUse.
     *
     * @param monthUse MONTH_USE
     */
    public void setMonthUse(Double monthUse) {
        this.monthUse = monthUse;
    }

    /**
     * getOver01.
     *
     * @return OVER01
     */
    public Double getOver01() {
        return over01;
    }

    /**
     * setOver01.
     *
     * @param over01 OVER01
     */
    public void setOver01(Double over01) {
        this.over01 = over01;
    }

    /**
     * getOver02.
     *
     * @return OVER02
     */
    public Double getOver02() {
        return over02;
    }

    /**
     * setOver02.
     *
     * @param over02 OVER02
     */
    public void setOver02(Double over02) {
        this.over02 = over02;
    }

    /**
     * getOver03.
     *
     * @return OVER03
     */
    public Double getOver03() {
        return over03;
    }

    /**
     * setOver03.
     *
     * @param over03 OVER03
     */
    public void setOver03(Double over03) {
        this.over03 = over03;
    }

    /**
     * getOver04.
     *
     * @return OVER04
     */
    public Double getOver04() {
        return over04;
    }

    /**
     * setOver04.
     *
     * @param over04 OVER04
     */
    public void setOver04(Double over04) {
        this.over04 = over04;
    }

    /**
     * getOver05.
     *
     * @return OVER05
     */
    public Double getOver05() {
        return over05;
    }

    /**
     * setOver05.
     *
     * @param over05 OVER05
     */
    public void setOver05(Double over05) {
        this.over05 = over05;
    }

    /**
     * getOver06.
     *
     * @return OVER06
     */
    public Double getOver06() {
        return over06;
    }

    /**
     * setOver06.
     *
     * @param over06 OVER06
     */
    public void setOver06(Double over06) {
        this.over06 = over06;
    }

    /**
     * getOver07.
     *
     * @return OVER07
     */
    public Double getOver07() {
        return over07;
    }

    /**
     * setOver07.
     *
     * @param over07 OVER07
     */
    public void setOver07(Double over07) {
        this.over07 = over07;
    }

    /**
     * getOver08.
     *
     * @return OVER08
     */
    public Double getOver08() {
        return over08;
    }

    /**
     * setOver08.
     *
     * @param over08 OVER08
     */
    public void setOver08(Double over08) {
        this.over08 = over08;
    }

    /**
     * getOver09.
     *
     * @return OVER09
     */
    public Double getOver09() {
        return over09;
    }

    /**
     * setOver09.
     *
     * @param over09 OVER09
     */
    public void setOver09(Double over09) {
        this.over09 = over09;
    }

    /**
     * getOver10.
     *
     * @return OVER10
     */
    public Double getOver10() {
        return over10;
    }

    /**
     * setOver10.
     *
     * @param over10 OVER10
     */
    public void setOver10(Double over10) {
        this.over10 = over10;
    }

    /**
     * getOver11.
     *
     * @return OVER11
     */
    public Double getOver11() {
        return over11;
    }

    /**
     * setOver11.
     *
     * @param over11 OVER11
     */
    public void setOver11(Double over11) {
        this.over11 = over11;
    }

    /**
     * getOver12.
     *
     * @return OVER12
     */
    public Double getOver12() {
        return over12;
    }

    /**
     * setOver12.
     *
     * @param over12 OVER12
     */
    public void setOver12(Double over12) {
        this.over12 = over12;
    }

    /**
     * getLeft01.
     *
     * @return LEFT01
     */
    public Double getLeft01() {
        return left01;
    }

    /**
     * setLeft01.
     *
     * @param left01 LEFT01
     */
    public void setLeft01(Double left01) {
        this.left01 = left01;
    }

    /**
     * getLeft02.
     *
     * @return LEFT02
     */
    public Double getLeft02() {
        return left02;
    }

    /**
     * setLeft02.
     *
     * @param left02 LEFT02
     */
    public void setLeft02(Double left02) {
        this.left02 = left02;
    }

    /**
     * getLeft03.
     *
     * @return LEFT03
     */
    public Double getLeft03() {
        return left03;
    }

    /**
     * setLeft03.
     *
     * @param left03 LEFT03
     */
    public void setLeft03(Double left03) {
        this.left03 = left03;
    }

    /**
     * getLeft04.
     *
     * @return LEFT04
     */
    public Double getLeft04() {
        return left04;
    }

    /**
     * setLeft04.
     *
     * @param left04 LEFT04
     */
    public void setLeft04(Double left04) {
        this.left04 = left04;
    }

    /**
     * getLeft05.
     *
     * @return LEFT05
     */
    public Double getLeft05() {
        return left05;
    }

    /**
     * setLeft05.
     *
     * @param left05 LEFT05
     */
    public void setLeft05(Double left05) {
        this.left05 = left05;
    }

    /**
     * getLeft06.
     *
     * @return LEFT06
     */
    public Double getLeft06() {
        return left06;
    }

    /**
     * setLeft06.
     *
     * @param left06 LEFT06
     */
    public void setLeft06(Double left06) {
        this.left06 = left06;
    }

    /**
     * getLeft07.
     *
     * @return LEFT007
     */
    public Double getLeft07() {
        return left07;
    }

    /**
     * setLeft07.
     *
     * @param left07 LEFT007
     */
    public void setLeft07(Double left07) {
        this.left07 = left07;
    }

    /**
     * getLeft08.
     *
     * @return LEFT08
     */
    public Double getLeft08() {
        return left08;
    }

    /**
     * setLeft08.
     *
     * @param left08 LEFT08
     */
    public void setLeft08(Double left08) {
        this.left08 = left08;
    }

    /**
     * getLeft09.
     *
     * @return LEFT09
     */
    public Double getLeft09() {
        return left09;
    }

    /**
     * setLeft09.
     *
     * @param left09 LEFT09
     */
    public void setLeft09(Double left09) {
        this.left09 = left09;
    }

    /**
     * getLeft10.
     *
     * @return LEFT10
     */
    public Double getLeft10() {
        return left10;
    }

    /**
     * setLeft10.
     *
     * @param left10 LEFT10
     */
    public void setLeft10(Double left10) {
        this.left10 = left10;
    }

    /**
     * getLeft11.
     *
     * @return LEFT11
     */
    public Double getLeft11() {
        return left11;
    }

    /**
     * setLeft11.
     *
     * @param left11 LEFT11
     */
    public void setLeft11(Double left11) {
        this.left11 = left11;
    }

    /**
     * getLeft12.
     *
     * @return LEFT12
     */
    public Double getLeft12() {
        return left12;
    }

    /**
     * setLeft12.
     *
     * @param left12 LEFT12
     */
    public void setLeft12(Double left12) {
        this.left12 = left12;
    }
}
