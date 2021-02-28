package com.github.ghrocs.zeebe.demo.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** @author Rocs Zhang */
@EnableSwagger2
@SpringBootApplication
public class SpringBootZeebeDemoBusinessServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootZeebeDemoBusinessServiceApplication.class, args);
  }
}
