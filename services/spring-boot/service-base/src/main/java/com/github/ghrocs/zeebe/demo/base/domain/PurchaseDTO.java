package com.github.ghrocs.zeebe.demo.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** @author Rocs Zhang */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PurchaseDTO extends TraceableDTO {

  private CustomerDO customer;
  private CommodityDO commodity;
  private Integer commodityCount;

  private BigDecimal totalAmount;
  private String orderNo;
  private String signer;

  public PurchaseDTO(CustomerDO customer, CommodityDO commodity, Integer commodityCount) {
    this.customer = customer;
    this.commodity = commodity;
    this.commodityCount = commodityCount;
  }
}
