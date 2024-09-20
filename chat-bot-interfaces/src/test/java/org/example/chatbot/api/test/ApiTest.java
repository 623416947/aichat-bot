package org.example.chatbot.api.test;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;

/**
 * test
 */
public class ApiTest {

    @Test
    public void query_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/88888418481122/topics?scope=all&count=20");

        get.addHeader("cookie", "zsxq_access_token=98FBD5B7-B780-5F7F-3814-C68246AEA132_5E7CBD24B8CCC15C; abtest_env=product; zsxqsessionid=3fad1c29ab706b2a4c71fc22dbbf5653; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22212554125442241%22%2C%22first_id%22%3A%221920fa98102648-0ca4a069a177538-26001151-2359296-1920fa981032bf9%22%2C%22props%22%3A%7B%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTkyMGZhOTgxMDI2NDgtMGNhNGEwNjlhMTc3NTM4LTI2MDAxMTUxLTIzNTkyOTYtMTkyMGZhOTgxMDMyYmY5IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMjEyNTU0MTI1NDQyMjQxIn0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22212554125442241%22%7D%7D");
        get.addHeader("content-type", "application/json; charset=UTF-8\n");

        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }

    }

    @Test
    public void answer() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/4848544141855188/comments");
        post.addHeader("cookie", "zsxq_access_token=98FBD5B7-B780-5F7F-3814-C68246AEA132_5E7CBD24B8CCC15C; abtest_env=product; zsxqsessionid=3fad1c29ab706b2a4c71fc22dbbf5653; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22212554125442241%22%2C%22first_id%22%3A%221920fa98102648-0ca4a069a177538-26001151-2359296-1920fa981032bf9%22%2C%22props%22%3A%7B%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTkyMGZhOTgxMDI2NDgtMGNhNGEwNjlhMTc3NTM4LTI2MDAxMTUxLTIzNTkyOTYtMTkyMGZhOTgxMDMyYmY5IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMjEyNTU0MTI1NDQyMjQxIn0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22212554125442241%22%7D%7D");
        post.addHeader("content-type", "application/json; charset=UTF-8\n");
        String paramJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"你也好\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"mentioned_user_ids\": []\n" +
                "  }\n" +
                "}";



        StringEntity stringEntry = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntry);

        CloseableHttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }
}
