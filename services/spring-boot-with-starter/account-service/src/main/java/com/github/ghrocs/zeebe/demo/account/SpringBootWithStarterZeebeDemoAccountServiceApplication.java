package com.github.ghrocs.zeebe.demo.account;

import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author Rocs Zhang */
@EnableZeebeClient
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoAccountServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoAccountServiceApplication.class, args);
  }
}
