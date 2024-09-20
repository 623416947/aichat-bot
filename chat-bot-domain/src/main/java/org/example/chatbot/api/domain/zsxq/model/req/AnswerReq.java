package org.example.chatbot.api.domain.zsxq.model.req;


// 请求问答接口信息

public class AnswerReq {

    private Req_data req_data;

    public AnswerReq(Req_data req_data) {
        this.req_data = req_data;
    }

    public Req_data getReq_data() {
        return req_data;
    }

    public void setReq_data(Req_data req_data) {
        this.req_data = req_data;
    }
}
