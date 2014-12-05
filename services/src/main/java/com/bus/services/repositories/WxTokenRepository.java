package com.bus.services.repositories;

import com.bus.services.model.WxToken;
import com.bus.services.util.CollectionUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WxTokenRepository extends BaseRepository<WxToken>{

    public WxTokenRepository() {
        super(WxToken.class);
    }

    public WxToken getAccessToken(){
        List<WxToken> wxTokens = findAll();
        if(CollectionUtil.isNotEmpty(wxTokens)){
            return CollectionUtil.getLast(wxTokens);
        }
        return null;
    }
}
