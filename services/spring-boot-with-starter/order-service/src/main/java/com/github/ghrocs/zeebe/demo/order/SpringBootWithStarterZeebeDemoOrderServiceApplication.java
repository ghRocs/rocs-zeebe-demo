package com.github.ghrocs.zeebe.demo.order;

import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author Rocs Zhang */
@EnableZeebeClient
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoOrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoOrderServiceApplication.class, args);
  }
}
