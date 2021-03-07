package com.github.ghrocs.zeebe.demo.base.config;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.ZeebeClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author Rocs Zhang */
@Slf4j
@Configuration
public class ZeebeClientAutoConfiguration {

  @Value("${zeebe.gateway.address:#{null}}")
  private String gatewayAddress;

  @Value("${zeebe.client.numExecutionThreads:1}")
  private int numJobWorkerExecutionThreads;

  @Bean
  public ZeebeClient zeebeClient() {
    ZeebeClientBuilder clientBuilder = ZeebeClient.newClientBuilder();
    if (gatewayAddress != null) {
      clientBuilder.gatewayAddress(gatewayAddress);
    }
    // use an unsecured connection
    clientBuilder.usePlaintext().numJobWorkerExecutionThreads(numJobWorkerExecutionThreads);
    ZeebeClient client = clientBuilder.build();
    log.info("Connected to the zeebe cluster:{}", client.newTopologyRequest().send().join());
    return client;
  }
}
