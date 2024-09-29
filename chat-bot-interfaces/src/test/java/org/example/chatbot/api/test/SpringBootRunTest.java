package org.example.chatbot.api.test;


import com.alibaba.fastjson.JSON;
import org.example.chatbot.api.domain.ai.IOpenAi;
import org.example.chatbot.api.domain.zsxq.IZsxqApi;
import org.example.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import org.example.chatbot.api.domain.zsxq.model.vo.Talk;
import org.example.chatbot.api.domain.zsxq.model.vo.Topics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;


    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAi openAi;

    @Test
    public void test_zsxqApi() throws IOException {
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
        logger.info("测试结果:{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        logger.info("topicsLength:{}", topics.size());
        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            int commentsCount = topic.getComments_count();
            Talk talk = topic.getTalk();
            String text = talk.getText();
            logger.info("topicId:{} text:{} commentsCount:{}", topicId, text, commentsCount);
            if (commentsCount == 0) {
                zsxqApi.answer(groupId, cookie, topicId, text);
            }
        }
    }

    @Test
    public void test_openAi() throws IOException {
        String response = openAi.doChatGPT("帮我写个Java的快速排序");
        logger.info("测试结果:{}", response);
    }


}
