package com.github.ghrocs.zeebe.demo.inventory.service.impl;

import com.github.ghrocs.zeebe.demo.inventory.service.IInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author Rocs Zhang */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

  @Override
  public void deduct(String commodityCode, int count) {
    log.info("编号为{}的商品扣减掉{}件库存", commodityCode, count);
    // TODO: 扣减库存
  }
}
