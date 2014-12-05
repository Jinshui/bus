package com.bus.services.services;

import com.bus.services.model.WxToken;
import com.bus.services.repositories.WxTokenRepository;
import com.bus.services.util.HttpUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class AccessTokenService implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AccessTokenService.class);

    @Value("${wx.app.id}")
    private String APP_ID;

    @Value("${wx.app.secret}")
    private String APP_SECRET;

    @Value("${wx.token.refresh.uri}")
    private String WX_TOKEN_URI;
    @Resource
    private WxTokenRepository wxTokenRepository;

    private String accessToken;

    public void run() {
        refreshToken();
    }

    public String refreshToken() {
        log.info("Refreshing access token");
        try {
            String result = HttpUtils.executeGet(String.format(WX_TOKEN_URI, APP_ID, APP_SECRET));
            JSONObject obj = (JSONObject)new JSONParser().parse(result);
            WxToken accessToken = wxTokenRepository.getAccessToken();
            if ( accessToken == null ) {
                accessToken = new WxToken();
            }
            if(obj.containsKey("access_token")){
                accessToken.setAccessToken(obj.get("access_token").toString());
            }
            if(obj.containsKey("expires_in")){
                accessToken.setExpiresIn(Long.parseLong(obj.get("expires_in").toString()));
            }
            log.debug("====> Got new accessToken: {}", accessToken);
            wxTokenRepository.save(accessToken);
        } catch (Exception e) {
            log.error("failed to refresh access token due to the error below: ", e);
        }
        return accessToken;
    }

    public String getAccessToken(){
        if(accessToken == null){
            return refreshToken();
        }
        return accessToken;
    }

    public String getTaskName(){
        return "Access Token Refresher";
    }

    public static void main(String[] args)throws Exception {
        String result = HttpUtils.executeGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx401c9d1040415697&secret=49a5b51a88126756eb48a2d818d654e7");
        System.out.println("==============> Got result: " + result);
    }
}
