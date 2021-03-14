package com.github.ghrocs.zeebe.demo.order.service.bo;

import lombok.Data;

import java.math.BigDecimal;

/** @author Rocs Zhang */
@Data
public class OrderBO {

  private String orderNo;

  private Long customerId;

  private String commodityCode;

  private Integer commodityCount;

  private BigDecimal totalAmount;
}
