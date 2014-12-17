package com.bus.services.model.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class MenuEventRequest extends EventRequest {
    public static final String ADD_RESERVATION = "ADD_RESERVATION";
    public static final String MY_RESERVATIONS = "MY_RESERVATIONS";
    public static final String REQUEST_NEW_ROUTE = "REQUEST_NEW_ROUTE";
    public static final String ANNOUNCEMENTS = "ANNOUNCEMENTS";
    public static final String CONTACT_US = "CONTACT_US";

    @XmlElement(name="EventKey")
    private String eventKey;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String toString(){
        return super.toString() + ", EventKey: " + eventKey;
    }
}
