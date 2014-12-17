package com.bus.services.model.weixin;


import javax.xml.bind.annotation.*;

@XmlSeeAlso({
        MenuEventRequest.class,
        TextRequest.class,
        EventRequest.class,
        VoiceRequest.class
})
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseRequest {
    @XmlElement(name="FromUserName")
    private String fromUser;
    @XmlElement(name="ToUserName")
    private String toUser;
    @XmlElement(name="MsgType")
    private MessageType messageType;
    @XmlElement(name="CreateTime")
    private long createTime;

    public BaseRequest(){
        this(null);
    }

    public BaseRequest(BaseRequest request){
        if(request != null){
            this.setToUser(request.getFromUser());
            this.setFromUser(request.getToUser());
        }
        this.setCreateTime(System.currentTimeMillis()/1000);
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String toString(){
        return "Type: " + messageType + ", From: " + fromUser;
    }
}
