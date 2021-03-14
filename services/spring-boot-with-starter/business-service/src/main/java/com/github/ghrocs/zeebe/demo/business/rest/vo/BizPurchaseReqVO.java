package com.github.ghrocs.zeebe.demo.business.rest.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** @author Rocs Zhang */
@ApiModel
@Data
public class BizPurchaseReqVO {

  @ApiModelProperty(
      value = "顾客ID",
      notes = "1:Li Lei, 2:Han Meimei",
      example = "1",
      allowableValues = "1,2",
      required = true,
      position = 0)
  @NotNull(message = "The 'customerId' field can't be null")
  private Long customerId;

  @ApiModelProperty(
      value = "商品编码",
      notes =
          "ISBN-7560013465:Longman New Concept English 1, ASIN-B012JIOKM4:World of Warcraft 60 Day Game Time",
      example = "ISBN-7560013465",
      allowableValues = "ASIN-B012JIOKM4,ISBN-7560013465,ISBN-7560013473",
      required = true,
      position = 1)
  @NotBlank(message = "The 'commodityCode' field can't be blank")
  private String commodityCode;

  @ApiModelProperty(value = "购买数量", example = "1", required = true, position = 2)
  @Min(value = 1, message = "The 'commodityCount' field can't be less than the integer 1")
  private Integer commodityCount;
}
