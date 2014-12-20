package com.bus.services.services;

import com.bus.services.exceptions.HttpException;
import com.bus.services.model.Reservation;
import com.bus.services.model.Route;
import com.bus.services.model.weixin.*;
import com.bus.services.repositories.ReservationRepository;
import com.bus.services.util.DateUtil;
import com.bus.services.util.HttpUtils;
import com.bus.services.util.WeixinXMLParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@Path("/weixin")
@Service
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class WeixinService {
    private static final Logger log = LoggerFactory.getLogger(WeixinService.class);
    @Value("${wx.menu.create.uri}")
    private String CREATE_MENU_URL;
    @Value("${wx.send.custom.message.uri}")
    private String SEND_CUSTOM_MESSAGE_URL;
    @Value("${wx.subscribe.welcome.msg}")
    private String WELCOME_MSG;
    @Value("${wx.msg.max.routes:5}")
    private int MAX_ROUTES;
    @Value("${wx.msg.no_route}")
    private String MSG_NO_ROUTE;
    @Value("${wx.msg.detail.url}")
    private String MSG_DETAIL_URL;
    @Value("${wx.msg.image.url}")
    private String MSG_IMAGE_URL;
    @Value("${wx.msg.route.title}")
    private String MSG_ROUTE_TITLE;
    @Value("${wx.msg.route.more}")
    private String MSG_ROUTE_MORE;
    @Value("${wx.msg.route.more.url}")
    private String MSG_ROUTE_MORE_URL;
    @Value("${wx.msg.reservations.url}")
    private String MSG_MY_RESERVATIONS_URL;
    @Value("${wx.msg.reservations.title}")
    private String MSG_MY_RESERVATIONS_TITLE;
    @Value("${wx.msg.reservation.cancel}")
    private String MSG_CANCEL_RESERVATION;
    @Value("${wx.msg.reservations.empty}")
    private String MSG_MY_RESERVATIONS_EMPTY;
    @Value("${wx.msg.reservations.description}")
    private String MSG_MY_RESERVATIONS_DESCRIPTION;
    @Value("${wx.msg.other.contact.us}")
    private String MSG_OTHER_CONTACT_US;
    @Value("${wx.msg.other.announcements}")
    private String MSG_OTHER_ANNOUNCEMENTS;
    @Value("${wx.msg.other.new.route}")
    private String MSG_OTHER_NEW_ROUTE;
    @Value("${wx.msg.admin.urls}")
    private String MSG_ADMIN_URLS;

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AccessTokenService accessTokenService;
    @Resource
    private RoutesService routesService;
    @Resource
    private ReservationRepository reservationRepository;

    @GET
    public String validateUrl(@QueryParam("signature")String signature, @QueryParam("timestamp")String timestamp, @QueryParam("nonce")String nonce, @QueryParam("echostr")String echostr){
        log.info("validateUrl signature: {}, timestamp: {}, nonce: {}, echostr: {}", signature, timestamp, nonce, echostr);
        return echostr;
    }

    @POST
    @Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_XML})
    public Response processMessage(String xmlMessage){
        BaseResponse responseMessage = null;
        try{
            BaseRequest messageObject = WeixinXMLParser.parseMessage(xmlMessage);
            if(messageObject == null){
                log.warn("invalid request was received.");
                return Response.ok().entity("").build();
            }
            log.info("request message: {}", messageObject);
            if(messageObject instanceof TextRequest){
                TextRequest textRequest = (TextRequest)messageObject;
                if("1784#admin#".equals(textRequest.getContent())){
                    TextResponse response = new TextResponse(messageObject);
                    response.setContent(MSG_ADMIN_URLS);
                    responseMessage = response;
                }else{
                    //TODO: what to do here?
                    log.warn("Nothing to do with text message {}", ((TextRequest) messageObject).getContent());
                }
            }else if(messageObject instanceof EventRequest){
                responseMessage = handleEventMessage((EventRequest)messageObject);
            }else{
                //TODO: What to do with location, image, video, voice, link request?
                log.warn("Unsupported type {}", messageObject.getMessageType());
            }
        }catch (Exception e){
            log.error("Error handling message " + xmlMessage, e);
        }
        log.info("response message: {}", responseMessage);
        return Response.ok().entity(responseMessage).build();
    }

    @Async
    public void sendCustomMessage(Reservation reservation, boolean add){
        String jsonString = null;
        try {
            CustomMessage message = buildCustomMessage(reservation, add);
            jsonString = objectMapper.writeValueAsString(message);
            log.info("sending custom message to user {}, {} ", message.getToUser(), jsonString);
            String url = String.format(SEND_CUSTOM_MESSAGE_URL, accessTokenService.getAccessToken());
            HttpUtils.executePost(url, jsonString);
        }catch (Exception e) {
            log.error(String.format("Error sending message to %s : %s", reservation.getPassenger().getId(), jsonString), e);
        }
    }

    private CustomMessage buildCustomMessage(Reservation reservation, boolean add){
        if(add){
            CustomPhotoTextMessage customPhotoTextMessage = new CustomPhotoTextMessage();
            customPhotoTextMessage.setToUser(reservation.getPassenger().getId());
            Article article = new Article();
            article.setTitle(MSG_MY_RESERVATIONS_TITLE);
            article.setUrl(String.format(MSG_MY_RESERVATIONS_URL, reservation.getPassenger().getId()));
            article.setDescription(String.format(MSG_MY_RESERVATIONS_DESCRIPTION,
                    DateUtil.toChineseDate(reservation.getFullDate()),
                    reservation.getRoute().getStartStation(),
                    reservation.getRoute().getMiddleStations()+", "+reservation.getRoute().getEndStation(),
                    reservation.getRoute().getStartStationDesc(),
                    reservation.getVehicle().getLicenseTag(),
                    reservation.getVehicle().getDriverName(),
                    reservation.getVehicle().getDriverContact(),
                    reservation.getRoute().getPrice()));
            customPhotoTextMessage.addArticle(article);
            return customPhotoTextMessage;
        }else{
            CustomTextMessage message = new CustomTextMessage();
            message.setToUser(reservation.getPassenger().getId());
            message.setContent(String.format(MSG_CANCEL_RESERVATION,
                    DateUtil.toChineseDate(reservation.getFullDate()),
                    reservation.getRoute().getStartStation(),
                    reservation.getRoute().getEndStation()));
            return message;
        }
    }

    @POST
    @Path("/menu/add")
    public Response createMenu(WeixinMenus menus){
        try {
            if(menus == null){
                menus = WeixinMenus.getDefault();
            }
            String jsonString = objectMapper.writeValueAsString(menus);
            String url = String.format(CREATE_MENU_URL, accessTokenService.getAccessToken());
            String result = HttpUtils.executePost(url, jsonString);
            return Response.ok().entity(result).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request body").build();
        }catch (HttpException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Create Menu Failed: " + e.getMessage()).build();
        }
    }

    private BaseResponse handleEventMessage(EventRequest eventMessage){
        switch (eventMessage.getEventType()){
            case subscribe:{
                if(eventMessage instanceof ScanEventRequest){ //scan subscribe
                    log.info("A new user followed by scanning code: {}", eventMessage.getFromUser());
                }else{ //subscribe
                    log.info("A new user followed: {}", eventMessage.getFromUser());
                }
                TextResponse response = new TextResponse(eventMessage);
                response.setContent(WELCOME_MSG);
                return response;
            }
            case unsubscribe:
                log.info("An user unfollowed: {}", eventMessage.getFromUser());
                break;
            case SCAN: //Already followed
                ScanEventRequest scanEventMessage = (ScanEventRequest)eventMessage;
                TextResponse response = new TextResponse(eventMessage);
                response.setContent(WELCOME_MSG);
                return response;
            case CLICK:{
                MenuEventRequest clickEventMessage = (MenuEventRequest)eventMessage;
                return handleClickMessage(clickEventMessage);
            }
            case VIEW:
                MenuEventRequest viewEventMessage = (MenuEventRequest)eventMessage;
                log.warn("User {} is viewing {}", viewEventMessage.getFromUser(), viewEventMessage.getEventKey());
                break;
            default:
                log.warn("Unsupported event message was received: {}", eventMessage);
                break;
        }
        return null;
    }
    private BaseResponse handleClickMessage(MenuEventRequest clickEventMessage){
        switch (clickEventMessage.getEventKey()){
            case MenuEventRequest.ADD_RESERVATION:{
                List<Route> routeList = routesService.getAllRoutes();
                if(routeList == null || routeList.isEmpty()){
                    TextResponse response = new TextResponse(clickEventMessage);
                    response.setContent(MSG_NO_ROUTE);
                    return response;
                }else{
                    PhotoTextResponse photoTextResponse = new PhotoTextResponse(clickEventMessage);
                    for(int i=0; i<routeList.size() && i < MAX_ROUTES; i++){
                        Route route = routeList.get(i);
                        Article article = new Article();
                        article.setUrl(String.format(MSG_DETAIL_URL, route.getId(), clickEventMessage.getFromUser()));
                        article.setPicUrl(String.format(MSG_IMAGE_URL, route.getMapFileName()));
                        article.setTitle(String.format(MSG_ROUTE_TITLE, route.getName(), route.getStartStation(), route.getEndStation(), route.getMiddleStations()));
                        article.setDescription("");
                        photoTextResponse.addArticle(article);
                    }
                    if(routeList.size() > MAX_ROUTES){
                        Article article = new Article();
                        article.setUrl(String.format(MSG_ROUTE_MORE_URL, clickEventMessage.getFromUser()));
                        article.setTitle(MSG_ROUTE_MORE);
                        photoTextResponse.addArticle(article);
                    }
                    photoTextResponse.setArticleCount(photoTextResponse.getArticles().size());
                    return photoTextResponse;
                }
            }
            case MenuEventRequest.MY_RESERVATIONS:{
                List<Reservation> reservations = reservationRepository.findValidReservations(clickEventMessage.getFromUser());
                if(reservations.isEmpty()){
                    TextResponse response = new TextResponse(clickEventMessage);
                    response.setContent(MSG_MY_RESERVATIONS_EMPTY);
                    return response;
                }else{
                    PhotoTextResponse photoTextResponse = new PhotoTextResponse(clickEventMessage);
                    Reservation reservation = reservations.get(0);
                    Article article = new Article();
                    article.setUrl(String.format(MSG_MY_RESERVATIONS_URL, clickEventMessage.getFromUser()));
                    article.setTitle(MSG_MY_RESERVATIONS_TITLE);
                    article.setDescription(String.format(MSG_MY_RESERVATIONS_DESCRIPTION,
                            DateUtil.toChineseDate(reservation.getFullDate()),
                            reservation.getRoute().getStartStation(),
                            reservation.getRoute().getMiddleStations()+", "+reservation.getRoute().getEndStation(),
                            reservation.getRoute().getStartStationDesc(),
                            reservation.getVehicle().getLicenseTag(),
                            reservation.getVehicle().getDriverName(),
                            reservation.getVehicle().getDriverContact(),
                            reservation.getRoute().getPrice()));
                    photoTextResponse.addArticle(article);
                    photoTextResponse.setArticleCount(1);
                    return photoTextResponse;
                }
            }
            case MenuEventRequest.CONTACT_US:{
                TextResponse response = new TextResponse(clickEventMessage);
                response.setContent(MSG_OTHER_CONTACT_US);
                return response;
            }
            case MenuEventRequest.ANNOUNCEMENTS:{
                TextResponse response = new TextResponse(clickEventMessage);
                response.setContent(MSG_OTHER_ANNOUNCEMENTS);
                return response;
            }
            case MenuEventRequest.REQUEST_NEW_ROUTE:{
                TextResponse response = new TextResponse(clickEventMessage);
                response.setContent(MSG_OTHER_NEW_ROUTE);
                return response;
            }
        }
        return null;
    }
}
