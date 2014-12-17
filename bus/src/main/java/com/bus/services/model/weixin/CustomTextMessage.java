package com.bus.services.model.weixin;

import java.util.HashMap;
import java.util.Map;

public class CustomTextMessage extends CustomMessage {
    public static final String KEY_CONTENT = "content";
    private Map<String, String> text;

    public CustomTextMessage(){
        this.setMessageType(MessageType.text);
    }

    public Map<String, String> getText() {
        if(text == null){
            text = new HashMap<>();
        }
        return text;
    }

    public void setContent(String content){
        this.getText().put(KEY_CONTENT, content);
    }
}
