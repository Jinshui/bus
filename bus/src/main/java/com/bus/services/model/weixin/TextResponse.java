package com.bus.services.model.weixin;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextResponse extends BaseResponse {
    @XmlElement(name="Content")
    private String content;

    public TextResponse(){
        super();
    }

    public TextResponse(BaseRequest request){
        super(request);
        this.setMessageType(MessageType.text);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
