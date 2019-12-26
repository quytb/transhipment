package com.havaz.transport.batch;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.havaz.transport",
                       exclude = {RabbitAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.havaz.transport.dao.repository")
@EntityScan(basePackages = "com.havaz.transport.dao.entity")
@EnableTransactionManagement
@EnableJpaAuditing
public class BatchApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Locale.setDefault(Locale.US);
        new SpringApplicationBuilder().sources(BatchApplication.class)
                .web(WebApplicationType.NONE).build().run(args);
    }

}
