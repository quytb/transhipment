package com.havaz.transport.api.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

    private Predicate<String> postPaths() {
        return Predicates.not(PathSelectors.regex("/error.*"));
    }

    private ApiInfo apiInfo() {
        try {
            File resource = new ClassPathResource("data/api_version.txt").getFile();
            String version = new String(Files.readAllBytes(resource.toPath()));
            return new ApiInfoBuilder().title("Havaz API")
                    .description("Havaz API reference for developers")
                    .termsOfServiceUrl("http://havaz.vn")
                    .license("Havaz License")
                    .licenseUrl("havaz@gmail.com").version(version).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ApiInfoBuilder().title("Havaz API")
                .description("Havaz API reference for developers")
                .termsOfServiceUrl("http://havaz.vn")
                .license("Havaz License")
                .licenseUrl("havaz@gmail.com").build();
    }

}
