package com.github.ghrocs.zeebe.demo.shipping.adapter;

import com.github.ghrocs.zeebe.demo.base.exception.DefaultException;
import com.github.ghrocs.zeebe.demo.shipping.service.IShippingService;
import com.github.ghrocs.zeebe.demo.shipping.spi.WMSApi;
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
import java.time.Duration;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Component
public class ShipCommodityZeebeJobWorker implements JobHandler {

  @Autowired private WMSApi wmsApi;

  @Autowired private IShippingService shippingService;

  @Autowired private ZeebeClient client;

  private JobWorker worker;

  @PostConstruct
  public void register() {
    worker =
        client
            .newWorker()
            .jobType("ship-commodity")
            .handler(this)
            .name("【Spring Boot Ship Commodity】")
            .timeout(Duration.ofMinutes(1L))
            .open();
    log.info("Job worker opened and receiving jobs");
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {
    log.info("Job handler called by ActivatedJob:{}", job);
    Map<String, Object> variablesMap = job.getVariablesAsMap();
    String address = (String) variablesMap.get("address");
    String commodityCode = (String) variablesMap.get("commodityCode");
    Integer count = (Integer) variablesMap.get("count");
    try {
      // Fetch Commodities
      wmsApi.fetchCommodities(commodityCode, count);
      // Ship Parcel
      shippingService.shipParcel(address);
      client.newCompleteCommand(job.getKey()).send().join();
      log.info("Job handler completed the Job#{}", job.getKey());
    } catch (DefaultException defaultException) {
      client
          .newFailCommand(job.getKey())
          .retries(0) // the number of remaining retries
          .errorMessage(defaultException.getMessage())
          .send()
          .join();
      log.info(
          "Job handler gone wrong with the Job#{}, due to: {}",
          job.getKey(),
          defaultException.getMessage());
    }
  }

  @PreDestroy
  public void unregister() {
    if (!worker.isClosed()) {
      worker.close();
      log.info("Job worker closed");
    }
  }
}
