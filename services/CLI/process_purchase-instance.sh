#!/bin/bash
PROCESS_ID="process_purchase"
VARIABLES_JSON="{\"traceId\":\"4a9be521-7c28-4bb5-a095-a7df9c63b06e\",\"customer\":{\"id\":1,\"name\":\"Li Lei\",\"address\":\"Beijing, China\"},\"commodity\":{\"code\":\"ISBN-7560013465\",\"name\":\"Longman New Concept English 1\",\"isVirtual\":false,\"price\":21.98},\"commodityCount\":1}"

echo ">zbctl create instance $PROCESS_ID --variables \"$VARIABLES_JSON\" --insecure"
zbctl create instance $PROCESS_ID --variables "$VARIABLES_JSON" --insecure
