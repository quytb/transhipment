package com.havaz.transport.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.havaz.transport",
                       exclude = {RabbitAutoConfiguration.class})
@ServletComponentScan
@EnableJpaRepositories(basePackages = "com.havaz.transport.dao.repository")
@EntityScan(basePackages = "com.havaz.transport.dao.entity")
@EnableTransactionManagement
@EnableJpaAuditing
public class TransportApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Locale.setDefault(Locale.US);
        new TransportApplication()
                .configure(new SpringApplicationBuilder(TransportApplication.class)).run(args);
    }

}
