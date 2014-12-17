package com.bus.services.model.weixin;

import javax.xml.bind.annotation.XmlElement;

public class ScanEventRequest extends EventRequest {
    @XmlElement(name="EventKey")
    private String eventKey;
    @XmlElement(name="Ticket")
    private String ticket;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String toString(){
        return super.toString() + ", EventKey: " + eventKey + ", Ticket: " + ticket;
    }
}
