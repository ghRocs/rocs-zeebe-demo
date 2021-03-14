package com.github.ghrocs.zeebe.demo.order.service.impl;

import com.github.ghrocs.zeebe.demo.common.repository.CommodityRepository;
import com.github.ghrocs.zeebe.demo.order.service.IOrderService;
import com.github.ghrocs.zeebe.demo.order.service.bo.OrderBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/** @author Rocs Zhang */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

  @Override
  public OrderBO create(long customerId, String commodityCode, int count) {
    log.info("为顾客{}购买{}个商品{}进行创建订单", customerId, count, commodityCode);
    // 计算金额
    BigDecimal price = CommodityRepository.getByCode(commodityCode).getPrice();
    BigDecimal totalAmount = new BigDecimal(count).multiply(price);
    // 生成订单
    OrderBO orderBO = new OrderBO();
    // 生成订单编号
    orderBO.setOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));
    orderBO.setCustomerId(customerId);
    orderBO.setCommodityCode(commodityCode);
    orderBO.setCommodityCount(count);
    orderBO.setTotalAmount(totalAmount);
    // TODO: 保存订单数据
    return orderBO;
  }
}
