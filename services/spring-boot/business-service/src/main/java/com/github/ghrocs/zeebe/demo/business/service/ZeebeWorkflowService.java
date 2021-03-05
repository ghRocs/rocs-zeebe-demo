package com.github.ghrocs.zeebe.demo.business.service;

import com.github.ghrocs.zeebe.demo.base.exception.DefaultException;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.command.PublishMessageCommandStep1;
import io.zeebe.client.api.response.PublishMessageResponse;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.response.WorkflowInstanceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;

/** @author Rocs Zhang */
@Slf4j
@Service
public class ZeebeWorkflowService<I, O> {

  @Autowired private ZeebeClient client;

  public void createInstance(String bpmnProcessId, I variables, StringBuilder wfKeyResultBuilder) {
    try {
      final WorkflowInstanceEvent workflowInstanceEvent =
          client
              .newCreateInstanceCommand()
              .bpmnProcessId(bpmnProcessId)
              .latestVersion()
              .variables(variables)
              .send()
              .join();
      log.info(
          "Created the Workflow Instance:{} for the Process#{}",
          workflowInstanceEvent,
          workflowInstanceEvent.getBpmnProcessId());
      wfKeyResultBuilder
          .append(workflowInstanceEvent.getBpmnProcessId())
          .append("-")
          .append(workflowInstanceEvent.getVersion())
          .append("-")
          .append(workflowInstanceEvent.getWorkflowInstanceKey());
    } catch (Exception e) {
      throw new DefaultException("WF-instance-creating has an error：" + e.getMessage(), e);
    }
  }

  public void createInstanceWithResult(
      String bpmnProcessId, I variables, StringBuilder wfKeyResultBuilder, O... variablesResult) {
    try {
      final WorkflowInstanceResult workflowInstanceResult =
          client
              .newCreateInstanceCommand()
              .bpmnProcessId(bpmnProcessId)
              .latestVersion()
              .variables(variables)
              .withResult()
              .send()
              .join();
      log.info(
          "Completed the Workflow instance with Result:{} for the Process#{}",
          workflowInstanceResult,
          workflowInstanceResult.getBpmnProcessId());
      wfKeyResultBuilder
          .append(workflowInstanceResult.getBpmnProcessId())
          .append("-")
          .append(workflowInstanceResult.getVersion())
          .append("-")
          .append(workflowInstanceResult.getWorkflowInstanceKey());
      variablesResult[0] =
          (O) workflowInstanceResult.getVariablesAsType(variablesResult[0].getClass());
    } catch (Exception e) {
      throw new DefaultException(
          "WF-instance-creating-completing has an error：" + e.getMessage(), e);
    }
  }

  public void publishMessage(
      String messageName,
      String correlationKey,
      String messageId,
      Map<String, Object> variablesMap,
      Long ttlSeconds) {
    try {
      PublishMessageCommandStep1.PublishMessageCommandStep3 step3 =
          client
              .newPublishMessageCommand()
              .messageName(messageName)
              .correlationKey(correlationKey)
              .variables(variablesMap);
      if (StringUtils.hasLength(messageId)) {
        step3.messageId(messageId);
      }
      if (ttlSeconds != null) {
        step3.timeToLive(Duration.ofSeconds(ttlSeconds));
      }
      PublishMessageResponse publishMessageResponse = step3.send().join();
      log.info(
          "Published the {} message with the correlation key#{} and the message key#{} ",
          messageName,
          correlationKey,
          publishMessageResponse == null ? null : publishMessageResponse.getMessageKey());
    } catch (Exception e) {
      throw new DefaultException("WF-instance-messaging has an error：" + e.getMessage(), e);
    }
  }
}
