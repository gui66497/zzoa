package zzjz.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author fgt
 * @version 2017/2/20 13:43
 */
public class ExcelTest {
    @Excel(name = "序号")
    private String index;

    @Excel(name = "资金性质")
    private String accountType;

    @Excel(name = "项目名称")
    private String projectName;

    @Excel(name = "申请金额")
    private String amountApplied;

    @Excel(name = "核定金额")
    private String approvedAmount;

    /**
     * getIndex.
     *
     * @return INDEX
     */
    public String getIndex() {
        return index;
    }

    /**
     * setIndex.
     *
     * @param index INDEX
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * getAccountType.
     *
     * @return ACCOUNT_TYPE
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * setAccountType.
     *
     * @param accountType ACCOUNT_TYPE
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * getProjectName.
     *
     * @return PROJECT_NAME
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * setProjectName.
     *
     * @param projectName PROJECT_NAME
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * getAmountApplied.
     *
     * @return AMOUNT_APPLIED
     */
    public String getAmountApplied() {
        return amountApplied;
    }

    /**
     * setAmountApplied.
     *
     * @param amountApplied AMOUNT_APPLIED
     */
    public void setAmountApplied(String amountApplied) {
        this.amountApplied = amountApplied;
    }

    /**
     * getApprovedAmount.
     *
     * @return APPROVED_AMOUNT
     */
    public String getApprovedAmount() {
        return approvedAmount;
    }

    /**
     * setApprovedAmount.
     *
     * @param approvedAmount APPROVED_AMOUNT
     */
    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
}
