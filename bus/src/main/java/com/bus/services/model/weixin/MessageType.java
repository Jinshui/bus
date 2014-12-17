package com.bus.services.model.weixin;

public enum MessageType {
    text, //request, response
    image, //request, response
    video, //request, response
    voice, //request, response
    location, //request only
    link, //request only
    //message type for event
    event, //request only
    //two more types response message
    music, //response only
    news //response only
}
