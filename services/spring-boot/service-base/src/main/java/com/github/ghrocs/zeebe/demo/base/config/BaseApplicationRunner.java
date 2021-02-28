package com.github.ghrocs.zeebe.demo.base.config;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** @author Rocs Zhang */
@Slf4j
@Component
public class BaseApplicationRunner implements ApplicationRunner {

  // 在应用程序启动过程中执行部署流程资源文件的开关
  @Value("${enableDeploymentRunner:false}")
  private Boolean enableDeploymentRunner;

  // 流程资源文件所在的Classpath
  @Value("${classPathResourceDeployment:processes/purchase.bpmn}")
  private String classPathResourceDeployment;

  @Autowired private ZeebeClient zeebeClient;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (enableDeploymentRunner) {
      DeploymentEvent deploymentEvent =
          zeebeClient
              .newDeployCommand()
              .addResourceFromClasspath(classPathResourceDeployment)
              .send()
              .join();
      log.info("Deployed the Workflows:{} to your zeebe cluster", deploymentEvent.getWorkflows());
    }
  }
}
