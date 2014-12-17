package com.bus.services.model.weixin;

import com.bus.services.config.CxfConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.cxf.helpers.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class Test {

    static String xml = "<xml>\n" +
            "    <ToUserName><![CDATA[gh_ec38e9012693]]></ToUserName>\n" +
            "    <FromUserName><![CDATA[onfMQuCwQ0PMzVCf0E9PMWEloRYU]]></FromUserName>\n" +
            "    <CreateTime>1418745600</CreateTime>\n" +
            "    <MsgType><![CDATA[event]]></MsgType>\n" +
            "    <Event><![CDATA[unsubscribe]]></Event>\n" +
            "    <EventKey><![CDATA[]]></EventKey>\n" +
            "</xml>";

    public static void main(String[] args) throws Exception {
        Document doc = XMLUtils.parse(xml);
        Element element = doc.getDocumentElement();
        String msgType = getTextOfFirstElementByTagName(element, "MsgType");
        System.out.println(msgType);
    }

    public static String getTextOfFirstElementByTagName(Element element, String tagName){
        NodeList nodeList = element.getElementsByTagName(tagName);
        if(nodeList != null && nodeList.getLength() > 0){
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    public static void main1(String[] args) throws Exception {
        PhotoTextResponse baseMessage = new PhotoTextResponse();
        baseMessage.setCreateTime(12345l);
        baseMessage.setFromUser("abcde");
        baseMessage.setMessageType(MessageType.event);
        baseMessage.setToUser("fghijk");
        Article article = new Article();
        article.setPicUrl("http://abc.com");
        article.setDescription("llllll");
        article.setTitle("not bad");
        article.setUrl("http://aaaas.com");
        baseMessage.getArticles().add(article);
        article = new Article();
        article.setPicUrl("http://xxxx.com");
        article.setDescription("xxxx");
        article.setTitle("xxxx");
        article.setUrl("http://xxxx.com");
        baseMessage.getArticles().add(article);

        JAXBContext jaxbContext = JAXBContext.newInstance(BaseRequest.class, TextRequest.class, EventRequest.class, MenuEventRequest.class);
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        jaxbMarshaller.marshal(baseMessage, System.out);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        jaxbMarshaller.marshal(baseMessage, baos);
//        String xml = baos.toString();

        String xml = "<xml>\n" +
                "    <ToUserName><![CDATA[gh_ec38e9012693]]></ToUserName>\n" +
                "    <FromUserName><![CDATA[onfMQuCwQ0PMzVCf0E9PMWEloRYU]]></FromUserName>\n" +
                "    <CreateTime>1418745600</CreateTime>\n" +
                "    <MsgType><![CDATA[event]]></MsgType>\n" +
                "    <Event><![CDATA[unsubscribe]]></Event>\n" +
                "    <EventKey><![CDATA[]]></EventKey>\n" +
                "</xml>";

        Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
//        Object obj = unMarshaller.unmarshal(reader);
    }


    public static void main2(String[] args) throws JsonProcessingException {
        CustomPhotoTextMessage customPhotoTextMessage = new CustomPhotoTextMessage();
        customPhotoTextMessage.setToUser("xxxxxxxx");
        Article article = new Article();
        article.setPicUrl("http://xxxx.com");
        article.setDescription("xxxx");
        article.setTitle("xxxx");
        article.setUrl("http://xxxx.com");
        customPhotoTextMessage.addArticle(article);
        article = new Article();
        article.setPicUrl("http://xxxx.com");
        article.setDescription("xxxx");
        article.setTitle("xxxx");
        article.setUrl("http://xxxx.com");
        customPhotoTextMessage.addArticle(article);

        CxfConfig.BusObjectMapper objectMapper = new CxfConfig.BusObjectMapper();
        System.out.println(objectMapper.writeValueAsString(customPhotoTextMessage));


        CustomTextMessage textMessage = new CustomTextMessage();
        textMessage.setToUser("xxxxxxxx");
        textMessage.setContent("Hello world!");
        System.out.println(objectMapper.writeValueAsString(textMessage));
    }
}