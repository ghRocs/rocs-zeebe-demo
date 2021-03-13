package com.github.ghrocs.zeebe.demo.inventory.adapter;

import com.github.ghrocs.zeebe.demo.inventory.service.IInventoryService;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class ZeebeJobWorker {

  @Autowired private IInventoryService inventoryService;

  @ZeebeWorker
  public void handleDeductInventory(final JobClient client, final ActivatedJob job) {
    log.info("Job handler called by ActivatedJob:{}", job);
    Map<String, Object> variablesMap = job.getVariablesAsMap();
    String commodityCode = (String) variablesMap.get("commodityCode");
    Integer count = (Integer) variablesMap.get("count");
    // call logic
    inventoryService.deduct(commodityCode, count);
    client.newCompleteCommand(job.getKey()).send().join();
    log.info("Completed the deduct-inventory job#{}", job.getKey());
  }
}
