package com.github.ghrocs.zeebe.demo.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** @author Rocs Zhang */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDO {

  private String code;

  private String name;

  private Boolean isVirtual;

  private BigDecimal price;
}
