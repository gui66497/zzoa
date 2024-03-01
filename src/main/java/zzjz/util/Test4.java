package zzjz.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.codec.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author 房桂堂
 * @description Test4
 * @date 2019/1/15 15:35
 */
public class Test4 {

    /*
        fgt     {MD5}6LoPeX3G6OrxuZKvGblakw==        zzjz1234
        陈晓梅   {MD5}rfN+ofvRzv0ZJCZJM1/kSg==        xmchen319616
        孙艳     {MD5}4QrcOUm6Wau+VuBX8g+IPg==        123456
        顾东健   {MD5}BSkzd7qm00hGrLXycMrwNA==        gu18806186309
        wh      {MD5}rYSRqdvcnFrvMTrb4Dsgvg==        870620wanghuiling
        汪慧     {MD5}eiU9S6pZlxF/5nz4CWXj2A==        870620huiling
        周丽     {MD5}3cudpMKUiaLfvL0e5X/LOA==        zhouli1994
        xyy     {MD5}4QrcOUm6Wau+VuBX8g+IPg==        123456
        李曼曼     {MD5}ZHnDapLhr2O7s7qszrQh8w==        limanman123
        谢翼远   {MD5}bRznqgodmI3Jaiq80Ye0Wg==        aptx4869
        钟贵彬   {MD5}YsitChXZ0co41d7nYqFuAQ==        1234qwer
        程道疆   {MD5}4PMsyFOJnsE7/1ZUOrhYVg==        zzjz12345
        刘工     {MD5}goAAbifA97ybGQGs0oH8wg==        huluhaoa3
        项君     {MD5}4QrcOUm6Wau+VuBX8g+IPg==        123456
        杨林泉   {MD5}zAPnR6avu8v4vnZorP6+5Q==        test123
        石峰     {MD5}JZZPM4PMGKS7ePAadRX3/g==        zzjz6666
        胡申明   {MD5}XQohbBM6MqcN7E/wQJxbLA==        hsm1967118
        汪坤     {MD5}6LoPeX3G6OrxuZKvGblakw==        zzjz1234
        马成成   {MD5}/GXVD1Bfvbi4+vBBB2QBmw==        zzjz8888
        孙尚志   {MD5}aD1hyL21ZXLMAcmsL1F3xQ==        s335631892
        陈庆华   {MD5}t4a4enNpLXQ3/jEPTHi3jg==        zzjz@123
        唐绿霜   {MD5}g4ABKDK2w5t9XyXbzvKB8g==        tls520603
        史服宇   {MD5}tTh819UkqyTqLDtt0NvOZQ==        sfy7406
        林强     {MD5}wkkUai9JIWhTgFBsu/Q2Qg==
        陈栎     {MD5}2LXYSoMkWYNzVjmcp0PXRA==
        吕明杰   {MD5}F/WkPQksMhWZTThQslZkhQ==3
        马宇     {MD5}3Ux9jfuh0hkTnqD+cktM9A==        my15895770272
        陈古鳞   {MD5}YsitChXZ0co41d7nYqFuAQ==        1234qwer
        杨振伟   {MD5}K+lHT72B1zvc0EZBL+y6Qw==
        董安敏   {MD5}6LoPeX3G6OrxuZKvGblakw==         zzjz1234
        代涛涛   {MD5}JZZPM4PMGKS7ePAadRX3/g==         zzjz6666
        戴定成   {MD5}6LoPeX3G6OrxuZKvGblakw==         zz1234
        李金润   {MD5}Xs6ojB55rub74S9KYUEtoQ==
        陈琳     {MD5}mXerjP3XzT23PC55M0SN4w==        123Cc456
        马啸     {MD5}bridaR35IVGPcJwiBpHnZg==        87290171a
        奚家熹   {MD5}pZQAGD/rzFpj4I/dfq7OTg==        ro123mantic
        杨铭     {MD5}s5oRQQqf+euZR40hzf6V4g==        19860710ym
        朱其志   {MD5}+IKgOKXwiiXpBkseEweqKw==         zqz1234
        刘书旗   {MD5}XZPOtw4r9dqoTsPQzSxzGg==         qwer1234
        王涛     {MD5}qAI7GosEDTC0owzoElFK5w==
        陈越     {MD5}45+b51RewIn0KZEUyL7eww==
        王文进   {MD5}ZBkGkil7ng2RSkfcHEMxqQ==         wwj123123
        王吉虹   {MD5}6A3Upsy3KoPdSJS5YL7q7A==         wjh1234
        陈韦成   {MD5}JSu5Q7R8Y3f0tOwm0sfSuA==         a13135110864
        扬书轩   {MD5}NF73kT0FHvE0g3xydMfH6g==
        鲍宏峰   {MD5}KX3B6A95jSiclo1rIMtLNQ==         bhf199121
        史秋娟   {MD5}6LoPeX3G6OrxuZKvGblakw==         zzjz1234
        柳川     {MD5}pHOvWUSj17zeW39i9Tuo8g==
        董浩良   {MD5}FbwGIsZcMFQwZ1JgIE2SQA==
        沈珺     {MD5}V25s6T+muvUc/o5GzFHo7Q==         agatha83
        李旭东   {MD5}03WcUEmqDe+4YuND3RTvDw==         wzsy4102
        邵红芸    {MD5}5wIddGXbnjH4bB10MPhbGQ==        shy123456
        卢超      {MD5}EQr/9gZeB8d2bKcISddRtg==
        谢正苏     {MD5}q+ClNi4RaGPC3Msq1cJpSQ==
        王颖      {MD5}vCtz9/QObWMFWyG/GS51ng==       woshi,xiao7
        隆海腾     {MD5}lgACBXvrJ7Rq/fSq+63sVQ==       LHT199442
        王俊杰     wang123
        邵达      {MD5}HDoqcgjhOjcjOft9ARgGBw==       ty941126
    */

    //
    public static void main(String[] args) throws IOException {
        String str = "{MD5}HDoqcgjhOjcjOft9ARgGBw==";
        String code = str.substring(str.lastIndexOf("}") + 1);
        System.out.println(getPureMd5(code));
    }

    /*public static void main(String[] args) throws IOException {
//核心应用类
        HttpClient httpClient = new DefaultHttpClient();

        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("name", "Scan_list"));
        qparams.add(new BasicNameValuePair("value", "192.168.1.1"));
        qparams.add(new BasicNameValuePair("conftype:", "nascan"));
        HttpEntity entity = new UrlEncodedFormEntity(qparams, "UTF-8");

        HttpPost httpPost = new HttpPost("http://192.168.1.7:8083/updateconfig");
        httpPost.setHeader("X-CSRFToken", "IjYzN2UzNzYyOTRiMjc4YWIzNDhhYzNmYWVmMjVlZWY0ODY5OTA0NWQi.XbGITA.tSTS6hVm5iPyJ3-CCgOpVGjcZKo");
        httpPost.setHeader("Cookie", "session=eyJjc3JmX3Rva2VuIjp7IiBiIjoiTmpNM1pUTTNOakk1TkdJeU56aGhZak0wT0dGak0yWmhaV1l5TldWbFpqUTROams1TURRMVpBPT0ifSwibG9naW4iOnsiIGIiOiJiRzluYVc1emRXTmpaWE56In19.XbGFeA.0arejBXTsWJm_2atcz23IGVXrXg");
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        //获取实体  
        HttpEntity httpEntity= response.getEntity();
        //打印StatusLine  
        System.out.println("StatusLine: " + response.getStatusLine());
        String resource = new Scanner(response.getEntity().getContent()).useDelimiter("\\Z").next();
        System.out.println(resource);
        *//*Map<String, String> params = new HashMap<>();
        params.put("name", "Scan_list");
        params.put("value", "192.168.1.1");
        params.put("conftype", "nascan");*//*
//        String AA = HttpUtil.doGet("http://192.168.1.7:8083/updateconfig", params);
//        System.out.println(AA);
    }*/

    /**
     * 根据ldap中的加密密码获取标准md5值.
     * @param code ldap中的密码
     * @return md5
     */
    public static String getPureMd5(String code) {
        byte[] res = Base64.decode(code.getBytes());
        // 16进制数字
        String md5code = new BigInteger(1, res).toString(16);
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * ldapui的加密逻辑.
     * @param password 密码
     * @return 加密后
     */
    public static String encryptPassword(String password) {
        String base64Password = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bs = md.digest();
            byte[] bs64Password = Base64.encode(bs);
            base64Password = new String(bs64Password);
            base64Password = "{MD5}" + base64Password;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return base64Password;
    }


}
