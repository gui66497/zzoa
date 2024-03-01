package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 * @version 2017/9/1 8:49
 * @ClassName: MyProcessDefinition
 * @Description: 流程的定义
 */
@XmlRootElement
public class MyProcessDefinition {

    /**
     * 流程定义id
     */
    private String id;

    /**
     * 流程name
     */
    private String name;

    /**
     * 流程定义的key
     */
    private String key;

    /**
     * 版本
     */
    private int version;

    /**
     * getId.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
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
     * getVersion.
     *
     * @return VERSION
     */
    public int getVersion() {
        return version;
    }

    /**
     * setVersion.
     *
     * @param version VERSION
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * getKey.
     *
     * @return KEY
     */
    public String getKey() {
        return key;
    }

    /**
     * setKey.
     *
     * @param key KEY
     */
    public void setKey(String key) {
        this.key = key;
    }
}
