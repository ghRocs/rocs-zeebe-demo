package com.github.ghrocs.zeebe.demo.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author Rocs Zhang */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDO {

  private Long id;

  private String name;

  private String address;
}
