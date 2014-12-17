package com.bus.services.util;

import com.bus.services.model.weixin.*;
import org.apache.cxf.helpers.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class WeixinXMLParser {

    public static final String REQ_MSG_TYPE_EVENT = "event";
    public static final String REQ_MSG_TYPE_TEXT = "text";
    public static final String REQ_MSG_TYPE_IMAGE = "image";
    public static final String REQ_MSG_TYPE_VIDEO = "video";
    public static final String REQ_MSG_TYPE_VOICE = "voice";
    public static final String REQ_MSG_TYPE_LINK = "link";
    public static final String REQ_MSG_TYPE_LOCATION = "location";

    public static final String REQ_EVENT_TYPE_SUBSCRIBE = "subscribe";
    public static final String REQ_EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    public static final String REQ_EVENT_TYPE_SCAN = "SCAN";
    public static final String REQ_EVENT_TYPE_LOCATION = "LOCATION";
    public static final String REQ_EVENT_TYPE_CLICK = "CLICK";
    public static final String REQ_EVENT_TYPE_VIEW = "VIEW";


    public static final String RESP_MSG_TYPE_MUSIC = "music";
    public static final String RESP_MSG_TYPE_NEWS = "news";

    //Keys for all requests
    public static final String REQ_KEY_TOUSERNAME = "ToUserName";
    public static final String REQ_KEY_FROMUSERNAME = "FromUserName";
    public static final String REQ_KEY_CREATETIME = "CreateTime";
    public static final String REQ_KEY_MSGTYPE = "MsgType";
    //Keys for general requests
    public static final String REQ_KEY_CONTENT = "Content";
    public static final String REQ_KEY_MSGID = "MsgId";
    public static final String REQ_KEY_PICURL = "PicUrl";
    public static final String REQ_KEY_MEDIAID = "MediaId";
    public static final String REQ_KEY_FORMAT = "Format";
    public static final String REQ_KEY_THUMBMEDIAID = "ThumbMediaId";
    public static final String REQ_KEY_LOCATION_X = "Location_X";
    public static final String REQ_KEY_LOCATION_Y = "Location_Y";
    public static final String REQ_KEY_SCALE = "Scale";
    public static final String REQ_KEY_LABEL = "Label";
    public static final String REQ_KEY_TITLE = "Title";
    public static final String REQ_KEY_DESCRIPTION = "Description";
    public static final String REQ_KEY_URL = "Url";
    //Keys for event requests
    public static final String REQ_KEY_EVENT = "Event";
    public static final String REQ_KEY_EVENTKEY = "EventKey";
    public static final String REQ_KEY_TICKET = "Ticket";
    public static final String REQ_KEY_LATITUDE = "Latitude";
    public static final String REQ_KEY_LONGITUDE = "Longitude";
    public static final String REQ_KEY_PRECISION = "Precision";

    private static String getTextOfFirstElementByTagName(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        if(nodeList != null && nodeList.getLength() > 0){
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    private static int getIntegerOfFirstElementByTagName(Element element, String tagName){
        String text = getTextOfFirstElementByTagName(element, tagName);
        try{
            return Integer.parseInt(text);
        }catch (Exception e){
            return 0;
        }
    }

    public static BaseRequest parseMessage(String xml) throws IOException, SAXException, ParserConfigurationException {
        Document document = XMLUtils.parse(xml);
        BaseRequest baseRequest = null;
        String msgType = getTextOfFirstElementByTagName(document.getDocumentElement(), REQ_KEY_MSGTYPE);
        switch (msgType){
            case REQ_MSG_TYPE_EVENT:
                baseRequest = parseEventRequest(document.getDocumentElement());
                baseRequest.setMessageType(MessageType.event);
                break;
            case REQ_MSG_TYPE_TEXT:
                TextRequest textRequest = new TextRequest();
                textRequest.setMessageType(MessageType.text);
                textRequest.setContent(getTextOfFirstElementByTagName(document.getDocumentElement(), REQ_KEY_CONTENT));
                textRequest.setMessageId(getTextOfFirstElementByTagName(document.getDocumentElement(), REQ_KEY_MSGID));
                baseRequest = textRequest;
                break;
            case REQ_MSG_TYPE_IMAGE:
            case REQ_MSG_TYPE_VIDEO:
            case REQ_MSG_TYPE_VOICE:
            case REQ_MSG_TYPE_LINK:
            case REQ_MSG_TYPE_LOCATION:
                baseRequest = new BaseRequest();
                baseRequest.setMessageType(MessageType.valueOf(msgType));
            default:
                break;
        }
        if(baseRequest != null){
            fillBasicProperties(document.getDocumentElement(), baseRequest);
        }
        return baseRequest;
    }

    private static EventRequest parseEventRequest(Element rootElement){
        String eventType = getTextOfFirstElementByTagName(rootElement, REQ_KEY_EVENT);
        switch(eventType){
            case REQ_EVENT_TYPE_SUBSCRIBE:{
                EventRequest request = new EventRequest();
                request.setEventType(EventType.subscribe);
                return request;
            }
            case REQ_EVENT_TYPE_UNSUBSCRIBE:{
                EventRequest request = new EventRequest();
                request.setEventType(EventType.unsubscribe);
                return request;
            }
            case REQ_EVENT_TYPE_SCAN:{
                ScanEventRequest scanRequest = new ScanEventRequest();
                scanRequest.setEventType(EventType.SCAN);
                scanRequest.setEventKey(getTextOfFirstElementByTagName(rootElement, REQ_KEY_EVENTKEY));
                scanRequest.setTicket(getTextOfFirstElementByTagName(rootElement, REQ_KEY_TICKET));
                return scanRequest;
            }
            case REQ_EVENT_TYPE_LOCATION:
                EventRequest request = new EventRequest();
                request.setEventType(EventType.LOCATION);
                //Not supported at this time, ignore other properties
                return request;
            case REQ_EVENT_TYPE_CLICK:{
                MenuEventRequest menuEventRequest = new MenuEventRequest();
                menuEventRequest.setEventType(EventType.CLICK);
                menuEventRequest.setEventKey(getTextOfFirstElementByTagName(rootElement, REQ_KEY_EVENTKEY));
                return menuEventRequest;
            }
            case REQ_EVENT_TYPE_VIEW:{
                MenuEventRequest menuEventRequest = new MenuEventRequest();
                menuEventRequest.setEventType(EventType.VIEW);
                menuEventRequest.setEventKey(getTextOfFirstElementByTagName(rootElement, REQ_KEY_EVENTKEY));
                return menuEventRequest;
            }
        }
        return null;
    }

    private static void fillBasicProperties(Element rootElement, BaseRequest baseRequest){
        baseRequest.setCreateTime(getIntegerOfFirstElementByTagName(rootElement, REQ_KEY_CREATETIME));
        baseRequest.setFromUser(getTextOfFirstElementByTagName(rootElement, REQ_KEY_FROMUSERNAME));
        baseRequest.setToUser(getTextOfFirstElementByTagName(rootElement, REQ_KEY_TOUSERNAME));
    }
}
