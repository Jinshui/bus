package com.bus.services.model;

import javax.persistence.*;

@Entity
@Table(name = "access_token")
public class AccessToken extends AbstractTimestamped<AccessToken>{
    @Column(nullable = false, length = 512)
    private String accessToken;
    @Column
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
