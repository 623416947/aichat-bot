package org.example.chatbot.api.domain.ai;


// Open Ai 接口

import java.io.IOException;

public interface IOpenAi {
    String doChatGPT(String question) throws IOException;

}
