package com.github.ghrocs.zeebe.demo.business.rest.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/** @author Rocs Zhang */
@ApiModel
@Data
public class BizReceiptViaTraceIdReqVO {

  @ApiModelProperty(value = "下单时TraceID", required = true, position = 0)
  @NotBlank(message = "The 'purchaseTraceId' field can't be blank")
  private String purchaseTraceId;

  @ApiModelProperty(value = "签收人", notes = "缺省为本人签收", position = 1)
  private String signer;
}
