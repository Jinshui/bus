package com.bus.services.config;

import com.bus.services.config.cxf.InvokeFaultExceptionMapper;
import com.bus.services.services.RoutesService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * CxfConfig
 * @author <a href="mailto:tangjinshui@gmail.com">Tom Tang</a>
 */
@Configuration
public class CxfConfig {
    @Resource
    private RoutesService routesService;
    //Beans for metamore-ui
    private static class BusObjectMapper extends ObjectMapper {
        public BusObjectMapper() {
            setAnnotationIntrospector(new AnnotationIntrospectorPair(new JacksonAnnotationIntrospector(), new JaxbAnnotationIntrospector(getTypeFactory())));
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
            configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            configure(SerializationFeature.INDENT_OUTPUT, true);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new BusObjectMapper();
    }

    @Bean
    public Bus cxfBus() {
        Bus bus = CXFBusFactory.getDefaultBus();
        bus.getInInterceptors().add(new LoggingInInterceptor());
        bus.getInFaultInterceptors().add(new LoggingInInterceptor());
        bus.getOutInterceptors().add(new LoggingOutInterceptor());
        bus.getOutFaultInterceptors().add(new LoggingOutInterceptor());
        return bus;
    }

    @Bean
    public Server restServer() {
        List<Object> serviceBeans = new ArrayList<>();
        serviceBeans.add(routesService);
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress("");
        factoryBean.setServiceBeans(serviceBeans);

        Map<Object, Object> extMappings = new HashMap<>();
        extMappings.put("json", MediaType.APPLICATION_JSON);
        extMappings.put("xml", MediaType.APPLICATION_XML);
        factoryBean.setExtensionMappings(extMappings);
        CrossOriginResourceSharingFilter filter = new CrossOriginResourceSharingFilter();
        filter.setAllowOrigins(Arrays.asList("http://127.0.0.1", "http://localhost", "http://127.0.0.1:8080", "http://localhost:8080"));
        factoryBean.setProviders(Arrays.asList(
//                new StringMessageBodyReader(),
//                new StringMessageBodyWriter(),
                new InvokeFaultExceptionMapper(),
                filter,
                new JacksonJsonProvider(getObjectMapper())));

        return factoryBean.create();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String input = "%7B%22id%22:%225482ff7b975cf001688236e1%22,%22createdOn%22:%222014-12-06T13:07:07.253 0000%22,%22lastUpdatedOn%22:%222014-12-07T01:22:19.645 0000%22,%22name%22:%22%E4%B8%AD%E5%9B%BD%22,%22price%22:%2215%22,%22startStation%22:%22XX%22,%22startStationDesc%22:%22XX%22,%22endStation%22:%22XX%22,%22middleStations%22:%22XX%22,%22timeRanges%22:%5B%7B%22startTime%22:%222014-12-01T00:00:00.000 0000%22,%22endTime%22:%222014-12-03T00:00:00.000 0000%22,%22startPoints%22:%220720%200740%200800%200820%200840%200900%200920%200940%201000%22,%22weekend%22:true,%22vehicles%22:%5B%7B%22name%22:%22%E8%BD%A6%E8%BE%86A%22,%22seatCount%22:14,%22licenseTag%22:%22FS234%22,%22driverName%22:%22XXX%22,%22driverContact%22:%22555555%22,%22company%22:%22ZZZ%22,%22startPoints%22:%5B%2207:20%22,%2208:00%22,%2208:40%22,%2209:20%22%5D%7D,%7B%22name%22:%22%E8%BD%A6%E8%BE%86B%22,%22seatCount%22:15,%22licenseTag%22:%22FF333%22,%22driverName%22:%22BBBB%22,%22driverContact%22:%2244444%22,%22company%22:%22XXXX%22,%22startPoints%22:%5B%2207:40%22,%2208:20%22,%2209:00%22,%2209:40%22,%2210:00%22%5D%7D%5D%7D%5D,%22published%22:false%7D";
        System.out.println(URLDecoder.decode(input, "UTF-8"));
    }
}