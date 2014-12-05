package com.bus.services.util;

import com.bus.services.exceptions.HttpException;
import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    public static String executeGet(String url) throws Exception {
        log.debug("Executing GET request: {}", url);
        try{
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory())
                    .build();
            HttpClientConnectionManager cm = new BasicHttpClientConnectionManager(registry);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            HttpGet request = new HttpGet(url);
            StringResponseHandler stringResponseHandler = new StringResponseHandler();
            httpClient.execute(request, stringResponseHandler);
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
