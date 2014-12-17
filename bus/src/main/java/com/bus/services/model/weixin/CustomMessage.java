package com.bus.services.model.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomMessage {
    @JsonProperty("touser")
    private String toUser;
    @JsonProperty("msgtype")
    private MessageType messageType;

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
}
