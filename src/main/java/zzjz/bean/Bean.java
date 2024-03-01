package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 * @ClassName: Bean
 * @Description: 单字段列表
 */
@XmlRootElement
public class Bean {
    private String col;

    /**
     * getCol.
     *
     * @return COL
     */
    public String getCol() {
        return col;
    }

    /**
     * setCol.
     *
     * @param col COL
     */
    public void setCol(String col) {
        this.col = col;
    }
}
