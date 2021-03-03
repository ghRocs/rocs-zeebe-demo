package com.github.ghrocs.zeebe.demo.business.rest.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/** @author Rocs Zhang */
@ApiModel
@Data
public class BizReceiptReqVO {

  @ApiModelProperty(value = "订单编号", required = true, position = 0)
  @NotBlank(message = "The 'orderNo' field can't be blank")
  private String orderNo;

  @ApiModelProperty(value = "签收人", notes = "缺省为本人签收", position = 1)
  private String signer;
}
