package com.bus.services.model.weixin;

import javax.xml.bind.annotation.XmlElement;

public class VoiceRequest extends BaseRequest {
    @XmlElement(name="MediaId")
    private String mediaId;
    @XmlElement(name="Format")
    private String format;
    @XmlElement(name="MsgId")
    private String messageId;
}
