#!/bin/bash
WORKFLOW_PATH="../spring-boot/service-base/src/main/resources/processes/purchase_zh_CN.bpmn"

echo ">zbctl deploy $WORKFLOW_PATH --insecure"
zbctl deploy $WORKFLOW_PATH --insecure