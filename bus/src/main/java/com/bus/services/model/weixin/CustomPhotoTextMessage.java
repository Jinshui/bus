package com.bus.services.model.weixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPhotoTextMessage extends CustomMessage {
    public static final String KEY_ARTICLES = "articles";
    private Map<String, List<Article>> news;

    public CustomPhotoTextMessage(){
        this.setMessageType(MessageType.news);
    }

    public Map<String, List<Article>> getNews() {
        if(news == null){
            news = new HashMap<>();
            news.put(KEY_ARTICLES, new ArrayList<Article>());
        }
        return news;
    }

    public void setNews(Map<String, List<Article>> news) {
        this.news = news;
    }

    public void addArticle(Article article){
        if(article != null){
            this.getNews().get(KEY_ARTICLES).add(article);
        }
    }
}
