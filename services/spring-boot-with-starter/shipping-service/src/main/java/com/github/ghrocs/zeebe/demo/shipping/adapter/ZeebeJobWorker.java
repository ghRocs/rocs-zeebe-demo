package com.github.ghrocs.zeebe.demo.shipping.adapter;

import com.github.ghrocs.zeebe.demo.common.exception.DefaultException;
import com.github.ghrocs.zeebe.demo.shipping.service.IShippingService;
import com.github.ghrocs.zeebe.demo.shipping.spi.WMSApi;
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

  @Autowired private WMSApi wmsApi;

  @Autowired private IShippingService shippingService;

  @ZeebeWorker(
      type = "ship-commodity",
      name = "【Spring Boot With Starter Ship Commodity】",
      timeout = 60000L)
  public void handleShipCommodity(final JobClient client, final ActivatedJob job) {
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
      log.info("Completed the ship-commodity Job#{}", job.getKey());
    } catch (DefaultException defaultException) {
      client
          .newFailCommand(job.getKey())
          .retries(0) // the number of remaining retries
          .errorMessage(defaultException.getMessage())
          .send()
          .join();
      log.error(
          "Gone wrong with the ship-commodity Job#{}, due to: {}", job.getKey(), defaultException.getMessage());
    }
  }
}
