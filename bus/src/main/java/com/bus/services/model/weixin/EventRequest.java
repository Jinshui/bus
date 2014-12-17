package com.bus.services.model.weixin;

import javax.xml.bind.annotation.*;

@XmlSeeAlso({MenuEventRequest.class, ScanEventRequest.class})
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventRequest extends BaseRequest {
    @XmlElement(name="Event")
    private EventType eventType;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String toString(){
        return super.toString() + ", Event: " + eventType;
    }
}
