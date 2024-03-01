package zzjz.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description Todo
 * @Author 房桂堂
 * @Date 2019/9/27 10:27
 */
public class Test5 {

    private static List<String> result = new ArrayList<>();

    /*public static void main(String[] args) throws IOException {
        File file = new File("E:\\zzjz_work\\DeepInsight\\wz\\data\\output3\\IBM\\google-1568791310.6540623.json");
        List<String> strList = Files.readLines(file, Charset.defaultCharset());
        String jsonStr = strList.toString();
        JsonParser p = new JsonParser();
        JsonElement e = p.parse(jsonStr);
        jsonTree(e);

        System.out.println(1);

        String last = StringUtils.join(result, " ");
    }*/

    public static void main(String[] args) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://192.168.1.35:5601/api/logstash/pipelines");

        BASE64Encoder encoder = new BASE64Encoder();
        String encoding = encoder.encode("elastic:changeme".getBytes(StandardCharsets.UTF_8));
        httpGet.setHeader("Authorization", "Basic " + encoding);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String res = EntityUtils.toString(entity, "utf-8");
        System.out.println(res);
    }

    public static void jsonTree(JsonElement e) {
        if (e.isJsonNull()) {
            System.out.println(e.toString());
            return;
        }

        if (e.isJsonPrimitive()) {
            System.out.println(e.getAsString());
            result.add(e.getAsString());
            return;
        }

        if (e.isJsonArray()) {
            JsonArray ja = e.getAsJsonArray();
            if (null != ja) {
                for (JsonElement ae : ja) {
                    jsonTree(ae);
                }
            }
            return;
        }

        if (e.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> es = e.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> en : es) {
                jsonTree(en.getValue());
            }
        }
    }

}
