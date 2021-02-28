package com.github.ghrocs.zeebe.demo.business.rest.vo;

import com.github.ghrocs.zeebe.demo.base.domain.PurchaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/** @author Rocs Zhang */
@ApiModel
@Data
@Builder
public class BizPurchaseRespVO {

  @ApiModelProperty(value = "Timestamp", required = true, position = 0)
  private Timestamp timestamp;
  // Once the non-blocking startup
  @ApiModelProperty(value = "Trace ID", required = true, position = 1)
  private String traceId;
  // Once the blocking startup
  @ApiModelProperty(value = "Bpmn:process id-Version-Instance key", required = true, position = 2)
  private String wfInstance;
  // Once the blocking completion
  @ApiModelProperty(value = "End-to-end Result", required = true, position = 3)
  private PurchaseDTO e2eResult;
}
