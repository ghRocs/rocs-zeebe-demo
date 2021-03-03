package com.github.ghrocs.zeebe.demo.order.adapter;

import com.github.ghrocs.zeebe.demo.base.constant.PurchaseConst;
import com.github.ghrocs.zeebe.demo.order.service.IOrderService;
import com.github.ghrocs.zeebe.demo.order.service.bo.OrderBO;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import io.zeebe.client.api.worker.JobWorkerBuilderStep1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class CreateOrderZeebeJobWorker implements JobHandler {

  // Job能被激活的最大数目
  @Value("${zeebe.client.maxJobsActive:#{null}}")
  private Integer maxJobsActive;

  @Autowired private IOrderService orderService;

  @Autowired private ZeebeClient client;

  private JobWorker worker;

  @PostConstruct
  public void register() {
    JobWorkerBuilderStep1.JobWorkerBuilderStep3 step3 =
        client
            .newWorker()
            .jobType("create-order")
            .handler(this)
            .name("【Spring Boot Create Order】")
            .timeout(Duration.ofMinutes(1L));
    if (maxJobsActive != null) {
      step3.maxJobsActive(maxJobsActive);
    }
    worker = step3.open();
    log.info("Job worker opened and receiving jobs");
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {
    log.info("Job handler called by ActivatedJob:{}", job);
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
