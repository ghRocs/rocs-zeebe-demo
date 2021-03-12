package com.github.ghrocs.zeebe.demo.account.adapter;

import com.github.ghrocs.zeebe.demo.account.service.IAccountService;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class ZeebeJobWorker {

  @Autowired private IAccountService accountService;

  @ZeebeWorker(
      type = "debit-account",
      name = "【Spring Boot With Starter Debit Account】",
      timeout = 60000L)
  public void handleDebitAccount(final JobClient client, final ActivatedJob job) {
    Map<String, Object> variablesMap = job.getVariablesAsMap();
    Integer userId = (Integer) variablesMap.get("userId");
    Double amount = (Double) variablesMap.get("amount");
    // call logic
    accountService.debit(userId.longValue(), BigDecimal.valueOf(amount));
    client.newCompleteCommand(job.getKey()).send().join();
    log.info("Completed the debit-account Job#{}", job.getKey());
  }
}
