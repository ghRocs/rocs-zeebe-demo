package com.github.ghrocs.zeebe.demo.business;

import io.zeebe.client.api.response.Topology;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeDeployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** @author Rocs Zhang */
@Slf4j
@EnableSwagger2
@EnableZeebeClient
@ZeebeDeployment(classPathResources = "processes/purchase_zh_CN.bpmn")
@SpringBootApplication
public class SpringBootWithStarterZeebeDemoBusinessServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWithStarterZeebeDemoBusinessServiceApplication.class, args);
  }

  @Component
  class Runner implements ApplicationRunner {

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
