package com.bus.services.config;

import com.bus.services.config.cxf.InvokeFaultExceptionMapper;
import com.bus.services.services.AuthenticationService;
import com.bus.services.services.ReservationService;
import com.bus.services.services.RoutesService;
import com.bus.services.services.WeixinService;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import java.util.*;

/**
 * CxfConfig
 * @author <a href="mailto:tangjinshui@gmail.com">Tom Tang</a>
 */
@Configuration
public class CxfConfig {
    @Resource
    private RoutesService routesService;
    @Resource
    private ReservationService reservationService;
    @Resource
    private WeixinService weixinService;
    @Resource
    private AuthenticationService authenticationService;

    //Beans for metamore-ui
    public static class BusObjectMapper extends ObjectMapper {
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
    public Server restServer() throws JAXBException {
        List<Object> serviceBeans = new ArrayList<>();
        serviceBeans.add(routesService);
        serviceBeans.add(reservationService);
        serviceBeans.add(weixinService);
        serviceBeans.add(authenticationService);
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
                new InvokeFaultExceptionMapper(),
                filter,
                new JacksonJsonProvider(getObjectMapper())));

        return factoryBean.create();
    }
}