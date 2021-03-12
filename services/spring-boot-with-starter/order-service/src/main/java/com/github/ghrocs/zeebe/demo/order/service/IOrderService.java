package com.github.ghrocs.zeebe.demo.order.service;

import com.github.ghrocs.zeebe.demo.order.service.bo.OrderBO;

/** @author Rocs Zhang */
public interface IOrderService {

  /**
   * 创建订单
   *
   * @param customerId 顾客ID
   * @param commodityCode 商品编码
   * @param count 下单数量
   */
  OrderBO create(long customerId, String commodityCode, int count);
}
