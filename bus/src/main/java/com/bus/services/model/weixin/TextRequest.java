package com.bus.services.model.weixin;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextRequest extends BaseRequest {
    @XmlElement(name="Content")
    private String content;
    @XmlElement(name="MsgId")
    private String messageId;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String toString(){
        return super.toString() + ", content: " + content + ", msgId: " + messageId;
    }
}
