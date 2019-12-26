package com.havaz.transport.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:message.properties", encoding = "UTF-8")
public class MessageConfig {

    @Autowired
    private Environment env;

    public String getMessage(String errProperties){
        return  env.getProperty(errProperties);

    }

}
