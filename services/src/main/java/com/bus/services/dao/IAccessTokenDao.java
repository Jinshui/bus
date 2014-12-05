package com.bus.services.dao;

import com.bus.services.model.AccessToken;

public interface IAccessTokenDao extends IDao<AccessToken, Long> {
    AccessToken getAccessToken();
}
