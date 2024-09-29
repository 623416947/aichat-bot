package org.example.chatbot.api.application.job;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.chatbot.api.domain.ai.IOpenAi;
import org.example.chatbot.api.domain.zsxq.IZsxqApi;
import org.example.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import org.example.chatbot.api.domain.zsxq.model.vo.Talk;
import org.example.chatbot.api.domain.zsxq.model.vo.Topics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;


// 问答任务
@EnableScheduling
@Configuration
public class ChatbotSchedule {

    private Logger logger = LoggerFactory.getLogger(ChatbotSchedule.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;

    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAi openAi;

    @Scheduled(cron = "0/59 * * * * ?")
    public void run() {
        try {
            if (new Random().nextBoolean()) {
                logger.info("随机打样……");
                return;
            }

            GregorianCalendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 23 || hour < 7) {
                logger.info("打样，下班~");
                return;
            }

            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("测试结果:{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            logger.info("topicsLength:{}", topics.size());
            boolean flag = false;
            for (Topics topic : topics) {
                String topicId = topic.getTopic_id();
                int commentsCount = topic.getComments_count();
                Talk talk = topic.getTalk();
                String text = talk.getText();
                logger.info("topicId:{} text:{} commentsCount:{}", topicId, text, commentsCount);
                if (commentsCount == 0) {
                    flag = true;
                    String answer = openAi.doChatGPT(text.trim());
                    boolean status = zsxqApi.answer(groupId, cookie, topicId, answer);
                    logger.info("编号:{}问题:{}回答:{}状态:{}", topicId, text, answer, status);
                }
            }
            if (!flag) {
                logger.info("没有未回答问题");
            }

        } catch (Exception e) {
            logger.error("自动回答问题异常", e);
        }

    }


}
