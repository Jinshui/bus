package com.bus.services.config;

import com.bus.services.services.AccessTokenService;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * SchedulerConfig
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);
    @Resource
    private AccessTokenService accessTokenService;
    @Value("${wx.token.refresh.interval:7000}")
    private int ACCESS_TOKEN_REFRESH_INTERVAL;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler());
        log.info("Add Task: name={}, interval={} seconds", accessTokenService.getTaskName(), ACCESS_TOKEN_REFRESH_INTERVAL);
        taskRegistrar.addFixedRateTask(accessTokenService, ACCESS_TOKEN_REFRESH_INTERVAL * 1000);
    }

    /**
     * <p>taskScheduler.</p>
     *
     * @return a {@link java.util.concurrent.Executor} object.
     */
    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(10);
    }

}
