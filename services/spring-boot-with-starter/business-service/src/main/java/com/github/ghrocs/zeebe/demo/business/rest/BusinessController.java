package com.github.ghrocs.zeebe.demo.business.rest;

import com.github.ghrocs.zeebe.demo.business.rest.vo.BizPurchaseReqVO;
import com.github.ghrocs.zeebe.demo.business.rest.vo.BizPurchaseRespVO;
import com.github.ghrocs.zeebe.demo.business.rest.vo.BizReceiptViaTraceIdReqVO;
import com.github.ghrocs.zeebe.demo.business.service.ZeebeWorkflowService;
import com.github.ghrocs.zeebe.demo.common.constant.PurchaseConst;
import com.github.ghrocs.zeebe.demo.common.domain.CommodityDO;
import com.github.ghrocs.zeebe.demo.common.domain.CustomerDO;
import com.github.ghrocs.zeebe.demo.common.domain.PurchaseDTO;
import com.github.ghrocs.zeebe.demo.common.exception.DefaultException;
import com.github.ghrocs.zeebe.demo.common.repository.CommodityRepository;
import com.github.ghrocs.zeebe.demo.common.repository.CustomerRepository;
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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/** @author Rocs Zhang */
@Api(tags = "Business", description = "End-points")
@Slf4j
@RestController
@RequestMapping("/api/business")
public class BusinessController {

  // 在创建流程实例时是否等待执行结果返回的开关
  @Value("${enableCreateWorkflowInstanceWithExecutionResult:false}")
  private Boolean enableCreateWorkflowInstanceWithExecutionResult;

  // 消息的生存时间(TTL)
  @Value("${zeebe.message.buffering.ttlSeconds:#{null}}")
  private Long ttlSeconds;

  @Autowired private ZeebeWorkflowService<PurchaseDTO, PurchaseDTO> workflowService;

  @ApiOperation(
      value = "顾客购买商品",
      notes = "通过执行相应流程" + PurchaseConst.BPMN_PROCESS_ID + "语义内容进行模拟顾客购买商品下单业务逻辑流程")
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
        PurchaseConst.BPMN_PROCESS_ID);
    if (enableCreateWorkflowInstanceWithExecutionResult) {
      workflowService.createInstanceWithResult(
          PurchaseConst.BPMN_PROCESS_ID, purchaseDTO, wfKeyResultBuilder, purchaseResult);
    } else {
      workflowService.createInstance(
          PurchaseConst.BPMN_PROCESS_ID, purchaseDTO, wfKeyResultBuilder);
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
      notes = "仅完成对顾客购买商品进行价格试算的逻辑并返回结果，不会创建相应流程" + PurchaseConst.BPMN_PROCESS_ID + "的实例")
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
            .wfInstance(PurchaseConst.BPMN_PROCESS_ID + "-{INT}-{INT}")
            .e2eResult(purchaseDTO)
            .build();
    return ResponseEntity.ok(bizPurchaseRespVO);
  }

  @ApiOperation(
      value = "顾客(签)收",
      notes = "通过发布-订阅相应" + PurchaseConst.MESSAGE_NAME_TRACE_DELIVERED + "消息进行模拟顾客(签)收业务逻辑")
  @PostMapping("/receipt")
  public ResponseEntity receipt(
      @Validated @ModelAttribute BizReceiptViaTraceIdReqVO bizReceiptViaTraceIdReqVO) {
    log.info("Requested {} to await a message upon receipt of delivery", bizReceiptViaTraceIdReqVO);
    Map<String, Object> variablesMap =
        Collections.singletonMap("signer", bizReceiptViaTraceIdReqVO.getSigner());
    log.info(
        "Send one {} message with Variables:{} to the workflow instances with the correlation key#{}",
        PurchaseConst.MESSAGE_NAME_TRACE_DELIVERED,
        variablesMap,
        bizReceiptViaTraceIdReqVO.getPurchaseTraceId());
    workflowService.publishMessage(
        PurchaseConst.MESSAGE_NAME_TRACE_DELIVERED,
        bizReceiptViaTraceIdReqVO.getPurchaseTraceId(),
        customizeMessageIdentifier(
            bizReceiptViaTraceIdReqVO.getPurchaseTraceId(),
            PurchaseConst.MESSAGE_NAME_TRACE_DELIVERED),
        variablesMap,
        ttlSeconds);
    return ResponseEntity.ok().build();
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

  private static String customizeMessageIdentifier(String uid, String name) {
    return uid.concat("-").concat(name.toLowerCase().trim().replaceAll("\\s+", "-"));
  }
}
