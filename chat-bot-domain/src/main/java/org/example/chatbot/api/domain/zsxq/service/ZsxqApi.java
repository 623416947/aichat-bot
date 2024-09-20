package org.example.chatbot.api.domain.zsxq.service;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.chatbot.api.domain.zsxq.IZsxqApi;
import org.example.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import org.example.chatbot.api.domain.zsxq.model.req.AnswerReq;
import org.example.chatbot.api.domain.zsxq.model.req.Req_data;
import org.example.chatbot.api.domain.zsxq.model.res.AnswerRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZsxqApi implements IZsxqApi {

    private Logger logger = LoggerFactory.getLogger(ZsxqApi.class);


    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/" + groupId + "/topics?scope=all&count=20");

        get.addHeader("cookie", cookie);
        get.addHeader("content-type", "application/json; charset=uft8\n");
        get.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");


        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取提问数据。groupId:{} jsonStr:{}", groupId, jsonStr);
            return JSON.parseObject(jsonStr, UnAnsweredQuestionsAggregates.class);
        } else {
            throw new RuntimeException("queryUnAnsweredQuestionsTopicID Err Code is " + response.getStatusLine().getReasonPhrase());
        }

    }

    @Override
    public boolean answer(String groupId, String cookie, String topicId, String text) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/" + topicId + "/comments");
        post.addHeader("cookie", cookie);
        post.addHeader("content-type", "application/json; charset=UTF-8\n");
        post.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");

        /* 测试数据
        String paramJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"你也好\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"mentioned_user_ids\": []\n" +
                "  }\n" +
                "}";
        */


        AnswerReq answerReq = new AnswerReq(new Req_data(text));
        String paramJson = JSONObject.fromObject(answerReq).toString();


        StringEntity stringEntry = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntry);

        CloseableHttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("回答问题结果。groupId:{} topicID:{} jsonStr:{}", groupId, topicId, jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
            return answerRes.isSucceeded();
        } else {
            throw new RuntimeException("answer Err Code is " + response.getStatusLine().getStatusCode());
        }

    }
}
