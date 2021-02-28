package com.github.ghrocs.zeebe.demo.base;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.Topology;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** @author Rocs Zhang */
@Slf4j
@SpringBootTest
class ZeebeEnvTests {

  @Autowired private ZeebeClient client;

  @Test
  void topologyRequest() {
    Topology topology = client.newTopologyRequest().send().join();
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

  @Test
  void deployCommand() {
    DeploymentEvent deploymentEvent =
        client.newDeployCommand().addResourceFromClasspath("processes/purchase.bpmn").send().join();
    log.info("DeploymentEvent:{}", deploymentEvent);
    deploymentEvent
        .getWorkflows()
        .forEach(
            wf -> {
              log.info(
                  "Workflow#{}-{}-version:{}",
                  wf.getWorkflowKey(),
                  wf.getBpmnProcessId(),
                  wf.getVersion());
              log.info("Workflow-resourceName:【{}】", wf.getResourceName());
            });
  }
}
