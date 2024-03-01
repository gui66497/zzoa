package zzjz.rest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.UrlRequest;
import zzjz.util.HttpUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author 房桂堂
 * @version 2015年5月9日 上午10:08:59
 * @ClassName: UtilRest
 * @Description:
 */
@Component
@Path("util")
public class UtilRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(UtilRest.class);

    @Value("${tyqxgl.url}")
    private String tyqxglUrl;

    /**
     * 调用tyqxgl系统的接口.
     *
     * @param request 请求
     * @return 结果
     * @throws ConnectTimeoutException ConnectTimeoutException
     */
    @POST
    @Path("link")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String link(UrlRequest request) throws ConnectTimeoutException {
        LOGGER.info("开始调用/util/link");
        String url = request.getUrl();
        String js = request.getJs();
        if (StringUtils.isBlank(url)) {
            return "";
        }
        url = "http://" + tyqxglUrl + "/" + url;
        String res = HttpUtil.doPost(url, js);
        if (res == null) {
            throw new ConnectTimeoutException("连接超时！");
        }
        LOGGER.info("调用完成/util/link");
        return res;
    }
}
