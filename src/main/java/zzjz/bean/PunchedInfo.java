package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 汪浩
 * @version 2017/5/27
 */
@XmlRootElement
public class PunchedInfo {

    private List<PunchedCard> logs;

    /**
     * getLogs.
     *
     * @return LOGS
     */
    public List<PunchedCard> getLogs() {
        return logs;
    }

    /**
     * setLogs.
     *
     * @param logs LOGS
     */
    public void setLogs(List<PunchedCard> logs) {
        this.logs = logs;
    }
}
