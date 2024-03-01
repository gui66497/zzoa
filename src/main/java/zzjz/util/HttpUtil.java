package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 房桂堂
 *         create 2017-05-25 15:59
 * @version 1.0
 * @className: HttpUtil
 * @description: 这里用一句话描述这个类的作用.
 **/
public class HttpUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private static final CloseableHttpClient HTTP_CLIENT;

    private static final Integer INT60000 = 60000;

    private static final Integer INT20000 = 20000;

    private static final Integer INT200 = 200;

    /**
     * 编码.
     */
    public static final String CHARSET = "UTF-8";

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(INT60000).setSocketTimeout(INT20000).build();
        HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, CHARSET);
    }

    /**
     * @param url    url
     * @param params params
     * @return String
     */
    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, CHARSET);
    }

    /**
     * HTTP Get 获取内容.
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != INT200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("################httpClient调用出错！，原因为：" + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * HTTP Post 获取内容.
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }

            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            httpPost.setHeader("userId", "1480489022639");
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != INT200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url      url
     * @param jsParams jsParams
     * @return String
     */
    public static String doPost(String url, String jsParams) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            StringEntity stringEntity = new StringEntity(jsParams);
            stringEntity.setContentType("application/json");
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(stringEntity);
            httpPost.setHeader("userId", "1480489022639");
            CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != INT200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        String getData = doGet("http://localhost:8080/tyqxgl/rest/user/list", null);
        System.out.println(getData);
        System.out.println("----------------------分割线-----------------------");
        String postData = doPost("http://localhost:8080/tyqxgl/rest/permissionInfo/list",
         "{\"systemId\":186,\"paging\":{\"pageSize\":10,\"pageNo\":1}}");
        System.out.println(postData);
    }*/
}
