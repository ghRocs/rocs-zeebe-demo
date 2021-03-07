#!/bin/bash
TYPE="create-order"
VARIABLES_JSON="{\"order\":{\"orderNo\":\"demo-520ddcb56b16412182e0f472d9f5bea3\",\"customerId\":1,\"commodityCode\":\"ISBN-7560013465\",\"commodityCount\":1,\"totalAmount\":21.98}}"

echo "zbctl create worker $TYPE --handler \"echo $VARIABLES_JSON\" --insecure"
zbctl create worker $TYPE --handler "echo $VARIABLES_JSON" --insecure