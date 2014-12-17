package com.bus.services.util;

import com.bus.services.exceptions.HttpException;
import org.apache.cxf.helpers.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static CloseableHttpClient closeableHttpClient;
    private static CloseableHttpClient getCloseableHttpClient(){
        if(closeableHttpClient == null){
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory())
                    .build();
            HttpClientConnectionManager cm = new BasicHttpClientConnectionManager(registry);
            closeableHttpClient = HttpClients.custom().setConnectionManager(cm).build();
        }
        return closeableHttpClient;
    }

    public static String executeGet(String url) throws Exception {
        log.debug("Executing GET request: {}", url);
        try{
            HttpGet request = new HttpGet(url);
            StringResponseHandler stringResponseHandler = new StringResponseHandler();
            getCloseableHttpClient().execute(request, stringResponseHandler);
            return stringResponseHandler.getResult();
        }catch (Exception e){
            throw new HttpException("Error executing GET request " + url, e);
        }
    }

    public static String executePost(String url, String body) throws HttpException {
        log.debug("Executing POST request to {} : {}", url, body);
        try{
            HttpPost request = new HttpPost(url);
            request.setEntity(new StringEntity(body));
            request.setHeader("Content-Type", "application/json;charset=utf-8");
            StringResponseHandler stringResponseHandler = new StringResponseHandler();
            getCloseableHttpClient().execute(request, stringResponseHandler);
            return stringResponseHandler.getResult();
        }catch (Exception e){
            throw new HttpException("Error executing GET request " + url, e);
        }
    }

    static class StringResponseHandler implements ResponseHandler<String> {
        private String result = null;
        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            result = IOUtils.readStringFromStream(response.getEntity().getContent());
            log.debug("Received String Response: {}", result);
            return result;
        }
        public String getResult(){
            return result;
        }
    }
}
