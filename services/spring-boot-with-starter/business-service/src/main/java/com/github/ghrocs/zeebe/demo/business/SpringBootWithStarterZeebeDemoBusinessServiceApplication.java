package com.github.ghrocs.zeebe.demo.business;

import io.zeebe.client.api.response.Topology;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeDeployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/** @author Rocs Zhang */
@Slf4j
@EnableZeebeClient
@ZeebeDeployment(classPathResources = "processes/purchase_zh_CN.bpmn")
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoBusinessServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoBusinessServiceApplication.class, args);
  }

  @Component
  static class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Autowired private ZeebeClientLifecycle zeebeClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
      Topology topology = zeebeClient.newTopologyRequest().send().join();
      log.info("Topology:{}", topology);
      topology
              .getBrokers()
              .forEach(
                      b -> {
                        log.info("Broker-address:【{}】", b.getAddress());
                        b.getPartitions()
                                .forEach(
                                        p -> {
                                          log.info(
                                                  "Partition#{}-{}-health:{}",
                                                  p.getPartitionId(),
                                                  p.getRole(),
                                                  p.getHealth());
                                        });
                      });
    }
  }
}
