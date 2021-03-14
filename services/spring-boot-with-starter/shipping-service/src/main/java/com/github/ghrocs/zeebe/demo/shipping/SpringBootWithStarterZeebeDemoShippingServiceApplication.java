package com.github.ghrocs.zeebe.demo.shipping;

import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author Rocs Zhang */
@EnableZeebeClient
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoShippingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoShippingServiceApplication.class, args);
  }
}
