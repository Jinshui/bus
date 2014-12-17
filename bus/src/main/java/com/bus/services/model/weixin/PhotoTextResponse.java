package com.bus.services.model.weixin;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoTextResponse extends BaseResponse{
    @XmlElement(name="ArticleCount")
    private int articleCount;
    @XmlElementWrapper(name = "Articles")
    @XmlElement(name="item")
    private List<Article> articles;

    public PhotoTextResponse(){
        super();
    }

    public PhotoTextResponse(BaseRequest request){
        super(request);
        this.setMessageType(MessageType.news);
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        if(articles == null)
            articles = new ArrayList<>();
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticle(Article article){
        if(article != null)
            getArticles().add(article);
    }
}
