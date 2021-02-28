package com.github.ghrocs.zeebe.demo.account.adapter;

import com.github.ghrocs.zeebe.demo.account.service.IAccountService;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class DebitAccountZeebeJobWorker implements JobHandler {

  @Autowired private IAccountService accountService;

  @Autowired private ZeebeClient client;

  private JobWorker worker;

  @PostConstruct
  public void register() {
    worker =
        client
            .newWorker()
            .jobType("debit-account")
            .handler(this)
            .name("【Spring Boot Debit Account】")
            .timeout(Duration.ofMinutes(1L))
            .open();
    log.info("Job worker opened and receiving jobs");
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {
    log.info("Job handler called by ActivatedJob:{}", job);
    Map<String, Object> variablesMap = job.getVariablesAsMap();
    Integer userId = (Integer) variablesMap.get("userId");
    Double amount = (Double) variablesMap.get("amount");
    // call logic
    accountService.debit(userId.longValue(), BigDecimal.valueOf(amount));
    client.newCompleteCommand(job.getKey()).send().join();
    log.info("Job handler completed the Job#{}", job.getKey());
  }

  @PreDestroy
  public void unregister() {
    if (!worker.isClosed()) {
      worker.close();
      log.info("Job worker closed");
    }
  }
}
