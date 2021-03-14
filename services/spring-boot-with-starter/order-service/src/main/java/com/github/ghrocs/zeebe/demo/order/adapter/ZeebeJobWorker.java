package com.github.ghrocs.zeebe.demo.order.adapter;

import com.github.ghrocs.zeebe.demo.common.constant.PurchaseConst;
import com.github.ghrocs.zeebe.demo.order.service.IOrderService;
import com.github.ghrocs.zeebe.demo.order.service.bo.OrderBO;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class ZeebeJobWorker {

  @Autowired private IOrderService orderService;

  @ZeebeWorker(
      type = "create-order",
      name = "【Spring Boot With Starter Create Order】",
      timeout = 60000L,
      maxJobsActive = 2)
  public void handleCreateOrder(final JobClient client, final ActivatedJob job) {
    Map<String, Object> variablesMap = job.getVariablesAsMap();
    Integer customerId = (Integer) variablesMap.get("customerId");
    String commodityCode = (String) variablesMap.get("commodityCode");
    Integer count = (Integer) variablesMap.get("count");
    // call logic
    OrderBO orderBO = orderService.create(customerId.longValue(), commodityCode, count);
    log.debug("业务原始单号:{}", orderBO.getOrderNo());
    // prefix OrderNo
    String numberPrefix =
        job.getCustomHeaders().getOrDefault(PurchaseConst.HEADER_ORDER_NO_PREFIX, "");
    orderBO.setOrderNo(numberPrefix + orderBO.getOrderNo());
    log.debug("流程运用单号:{}", orderBO.getOrderNo());
    Map<String, Object> outputVariablesMap =
        new HashMap() {
          {
            put("order", orderBO);
          }
        };
    client.newCompleteCommand(job.getKey()).variables(outputVariablesMap).send().join();
    log.info(
        "Completed the create-order job#{} with variables:{}", job.getKey(), outputVariablesMap);
  }
}
