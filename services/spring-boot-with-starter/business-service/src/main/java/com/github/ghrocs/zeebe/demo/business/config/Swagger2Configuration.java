package com.github.ghrocs.zeebe.demo.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author Rocs Zhang */
@Configuration
public class Swagger2Configuration {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.github.ghrocs.zeebe.demo.business.rest"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Demo Business APIs")
        .description("Spring Boot With Starter Zeebe Demo Business APIs")
        .contact(new Contact("Rocs Zhang", null, "askrocs" + "@gmail" + ".com"))
        .version("0.1-SNAPSHOT")
        .build();
  }
}
