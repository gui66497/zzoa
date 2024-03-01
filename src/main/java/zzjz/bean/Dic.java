package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年10月16日11:24
 */
@XmlRootElement
public class Dic {
    private String dicId;

    private String dicName;

    private String dicKey;

    private String dicValue;

    private String status;

    /**
     * getDicId.
     *
     * @return DIC_ID
     */
    public String getDicId() {
        return dicId;
    }

    /**
     * setDicId.
     *
     * @param dicId DIC_ID
     */
    public void setDicId(String dicId) {
        this.dicId = dicId == null ? null : dicId.trim();
    }

    /**
     * getDicName.
     *
     * @return DIC_NAME
     */
    public String getDicName() {
        return dicName;
    }

    /**
     * setDicName.
     *
     * @param dicName DIC_NAME
     */
    public void setDicName(String dicName) {
        this.dicName = dicName == null ? null : dicName.trim();
    }

    /**
     * getDicKey.
     *
     * @return DIC_KEY
     */
    public String getDicKey() {
        return dicKey;
    }

    /**
     * setDicKey.
     *
     * @param dicKey DIC_KEY
     */
    public void setDicKey(String dicKey) {
        this.dicKey = dicKey == null ? null : dicKey.trim();
    }

    /**
     * getDicValue.
     *
     * @return DIC_VALUE
     */
    public String getDicValue() {
        return dicValue;
    }

    /**
     * setDicValue.
     *
     * @param dicValue DIC_VALUE
     */
    public void setDicValue(String dicValue) {
        this.dicValue = dicValue == null ? null : dicValue.trim();
    }

    /**
     * getStatus.
     *
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus.
     *
     * @param status STATUS
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}