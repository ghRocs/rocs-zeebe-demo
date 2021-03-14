package com.github.ghrocs.zeebe.demo.shipping.service.impl;

import com.github.ghrocs.zeebe.demo.shipping.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author Rocs Zhang */
@Slf4j
@Service
public class ShippingServiceImpl implements IShippingService {

  @Override
  public void shipParcel(String address) {
    log.info("将商品包裹运往{}", address);
    // TODO: 运送包裹
  }
}
