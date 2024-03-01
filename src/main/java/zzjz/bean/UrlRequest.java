package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 *         create 2017-05-26 9:50
 * @version 1.0
 * @className: UrlRequest
 * @description: 这里用一句话描述这个类的作用.
 **/
@XmlRootElement
public class UrlRequest {

    /**
     * url路径
     */
    private String url;

    /**
     * js参数
     */
    private String js;

    /**
     * getUrl.
     *
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl.
     *
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getJs.
     *
     * @return JS
     */
    public String getJs() {
        return js;
    }

    /**
     * setJs.
     *
     * @param js JS
     */
    public void setJs(String js) {
        this.js = js;
    }
}
