package zzjz.bean;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * ExcelTest
 *
 * @author fgt
 * @version 2017/2/20 13:43
 */
public class ExcelTest {
    @Excel(name = "序号")
    private String                     index;

    @Excel(name = "资金性质")
    private String                     accountType;

    @Excel(name = "项目名称")
    private String                     projectName;

    @Excel(name = "申请金额")
    private String                     amountApplied;

    @Excel(name = "核定金额")
    private String                     approvedAmount;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAmountApplied() {
        return amountApplied;
    }

    public void setAmountApplied(String amountApplied) {
        this.amountApplied = amountApplied;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
}
