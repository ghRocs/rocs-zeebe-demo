package com.github.ghrocs.zeebe.demo.business.rest;

import com.github.ghrocs.zeebe.demo.base.domain.CommodityDO;
import com.github.ghrocs.zeebe.demo.base.domain.CustomerDO;
import com.github.ghrocs.zeebe.demo.base.domain.PurchaseDTO;
import com.github.ghrocs.zeebe.demo.base.exception.DefaultException;
import com.github.ghrocs.zeebe.demo.base.repository.CommodityRepository;
import com.github.ghrocs.zeebe.demo.base.repository.CustomerRepository;
import com.github.ghrocs.zeebe.demo.business.rest.vo.BizPurchaseReqVO;
import com.github.ghrocs.zeebe.demo.business.rest.vo.BizPurchaseRespVO;
import com.github.ghrocs.zeebe.demo.business.service.ZeebeWorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/** @author Rocs Zhang */
@Api(tags = "Business", description = "End-points")
@Slf4j
@RestController
@RequestMapping("/api/business")
public class BusinessController {

  // 对应流程定义Bpmn:process的id内容
  private static final String BPMN_PROCESS_ID = "process_purchase";

  // 在创建流程实例时是否等待执行结果返回的开关
  @Value("${enableCreateWorkflowInstanceWithExecutionResult:false}")
  private Boolean enableCreateWorkflowInstanceWithExecutionResult;

  @Autowired private ZeebeWorkflowService<PurchaseDTO, PurchaseDTO> workflowService;

  @ApiOperation(value = "顾客购买商品", notes = "通过执行相应流程" + BPMN_PROCESS_ID + "语义内容进行模拟顾客购买商品下单业务逻辑流程")
  @PostMapping("/purchase")
  public ResponseEntity<BizPurchaseRespVO> purchase(
      @Validated @ModelAttribute BizPurchaseReqVO bizPurchaseReqVO) {
    log.info("Requested {} to start a purchase process instance", bizPurchaseReqVO);
    BizPurchaseRespVO.BizPurchaseRespVOBuilder bizPurchaseRespVOBuilder =
        BizPurchaseRespVO.builder();
    PurchaseDTO purchaseDTO = doBackgroundVerificationForPurchaseDTO(bizPurchaseReqVO);
    StringBuilder wfKeyResultBuilder = new StringBuilder();
    PurchaseDTO[] purchaseResult = {new PurchaseDTO()};
    bizPurchaseRespVOBuilder.timestamp(Timestamp.from(Instant.now()));
    log.info(
        "Create a Workflow instance with Variables:{} for the latest Process#{}",
        purchaseDTO,
        BPMN_PROCESS_ID);
    if (enableCreateWorkflowInstanceWithExecutionResult) {
      workflowService.createInstanceWithResult(
          BPMN_PROCESS_ID, purchaseDTO, wfKeyResultBuilder, purchaseResult);
    } else {
      workflowService.createInstance(BPMN_PROCESS_ID, purchaseDTO, wfKeyResultBuilder);
    }
    BizPurchaseRespVO bizPurchaseRespVO =
        bizPurchaseRespVOBuilder
            .traceId(purchaseDTO.getTraceId())
            .wfInstance(wfKeyResultBuilder.toString())
            .e2eResult(purchaseResult[0])
            .build();
    return ResponseEntity.ok(bizPurchaseRespVO);
  }

  @ApiOperation(
      value = "顾客试购商品",
      notes = "仅完成对顾客购买商品进行价格试算的逻辑并返回结果，不会创建相应流程" + BPMN_PROCESS_ID + "的实例")
  @RequestMapping(
      value = "/purchase/trial",
      method = {RequestMethod.GET, RequestMethod.POST})
  public ResponseEntity<BizPurchaseRespVO> trialPurchase(
      @Validated @ModelAttribute BizPurchaseReqVO bizPurchaseReqVO) {
    log.info("Requested {} for a trial purchase process", bizPurchaseReqVO);
    BizPurchaseRespVO.BizPurchaseRespVOBuilder bizPurchaseRespVOBuilder =
        BizPurchaseRespVO.builder();
    PurchaseDTO purchaseDTO = doBackgroundVerificationForPurchaseDTO(bizPurchaseReqVO);
    bizPurchaseRespVOBuilder.timestamp(Timestamp.from(Instant.now()));
    BigDecimal price =
        CommodityRepository.getByCode(bizPurchaseReqVO.getCommodityCode()).getPrice();
    BigDecimal totalAmount = new BigDecimal(bizPurchaseReqVO.getCommodityCount()).multiply(price);
    purchaseDTO.setTotalAmount(totalAmount);
    purchaseDTO.setOrderNo("{Headers['NO_PREFIX']}{UUID}");
    BizPurchaseRespVO bizPurchaseRespVO =
        bizPurchaseRespVOBuilder
            .traceId(purchaseDTO.getTraceId())
            .wfInstance(BPMN_PROCESS_ID + "-{INT}-{INT}")
            .e2eResult(purchaseDTO)
            .build();
    return ResponseEntity.ok(bizPurchaseRespVO);
  }

  private PurchaseDTO doBackgroundVerificationForPurchaseDTO(BizPurchaseReqVO bizPurchaseReqVO) {
    CustomerDO customerDO = CustomerRepository.getById(bizPurchaseReqVO.getCustomerId());
    if (customerDO == null) {
      throw new DefaultException("The Customer can't be found");
    }
    CommodityDO commodityDO = CommodityRepository.getByCode(bizPurchaseReqVO.getCommodityCode());
    if (commodityDO == null) {
      throw new DefaultException("The Commodity can't be found");
    }
    PurchaseDTO purchaseDTO =
        new PurchaseDTO(customerDO, commodityDO, bizPurchaseReqVO.getCommodityCount());
    purchaseDTO.setTraceId(UUID.randomUUID().toString());
    return purchaseDTO;
  }
}
