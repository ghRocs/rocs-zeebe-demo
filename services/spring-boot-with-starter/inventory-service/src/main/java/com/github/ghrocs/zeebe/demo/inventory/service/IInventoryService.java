package com.github.ghrocs.zeebe.demo.inventory.service;

/** @author Rocs Zhang */
public interface IInventoryService {

  /**
   * 扣减库存
   *
   * @param commodityCode 商品编码
   * @param count 扣减数量
   */
  void deduct(String commodityCode, int count);
}
