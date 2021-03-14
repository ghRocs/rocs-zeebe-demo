package com.github.ghrocs.zeebe.demo.inventory;

import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author Rocs Zhang */
@EnableZeebeClient
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoInventoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoInventoryServiceApplication.class, args);
  }
}
