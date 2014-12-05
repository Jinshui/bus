package com.bus.services.dao.impl;

import com.bus.services.dao.IAccessTokenDao;
import com.bus.services.model.AccessToken;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AccessTokenDao extends HibernateDao<AccessToken, Long> implements IAccessTokenDao {

    public AccessToken getAccessToken() {
        List accessTokens = createCriteria().list();
        if(!CollectionUtils.isEmpty(accessTokens)){
            return (AccessToken)accessTokens.get(accessTokens.size() - 1);
        }
        return null;
    }
}
