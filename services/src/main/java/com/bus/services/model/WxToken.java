package com.bus.services.model;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = WxToken.MONGO_COLLECTION)
public class WxToken extends BaseMongoPersistent<WxToken> {
    public final static String MONGO_COLLECTION = "WxToken";
    private String accessToken;
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String toString(){
        return "accessToken: " + accessToken + ", expiresIn: " + expiresIn;
    }
}
