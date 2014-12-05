package com.bus.services.config;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * CxfConfig
 * @author <a href="mailto:tangjinshui@gmail.com">Tom Tang</a>
 */
@Configuration
public class CxfConfig {
    @Resource
    private RoutesService routesService;
    @Value("${testing:false}")
    private boolean testing;

    //Beans for metamore-ui
    private static class MetaMoreObjectMapper extends ObjectMapper {
        public MetaMoreObjectMapper() {
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
//        factoryBean.setDataBinding(MetaMoreUtil.METAMORE_JAXB_DATA_BINDING);

        Map<Object, Object> extMappings = new HashMap<>();
        extMappings.put("json", MediaType.APPLICATION_JSON);
        extMappings.put("xml", MediaType.APPLICATION_XML);
        factoryBean.setExtensionMappings(extMappings);
        factoryBean.setProviders(Arrays.asList(
//                new StringMessageBodyReader(),
//                new StringMessageBodyWriter(),
//                new ConstraintViolationExceptionMapper(),
//                new DuplicateEntityExceptionMapper(),
                new JacksonJsonProvider(new MetaMoreObjectMapper())));

        return factoryBean.create();
    }
}