package com.havaz.transport.core.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.havaz.transport.core.utils.ApplicationContextProvider;
import com.havaz.transport.core.utils.SpeedSMSAPI;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    private static final String SPEED_SMS_API_ACCESS_TOKEN = "zLOtSQMD7a1siFmae-dltIvf_iskN7p6";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public ApplicationContextAware applicationContextAware() {
        return new ApplicationContextProvider();
    }

    @Bean
    public SpeedSMSAPI speedSmsApi() {
        return new SpeedSMSAPI(SPEED_SMS_API_ACCESS_TOKEN);
    }
}
