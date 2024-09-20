package org.example.chatbot.api.domain.zsxq;

import org.example.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;


import java.io.IOException;

/**
 * API 接口
 */
public interface IZsxqApi {



    UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException;

    boolean answer(String groupID, String cookie, String topicId, String text) throws IOException;


}
