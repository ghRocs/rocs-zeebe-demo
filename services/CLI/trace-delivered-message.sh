#!/bin/bash
MESSAGE_NAME="Trace delivered"
CORRELATION_KEY="4a9be521-7c28-4bb5-a095-a7df9c63b06e"
MESSAGE_ID="$CORRELATION_KEY-trace-delivered"
VARIABLES_JSON="{\"signer\":\"Jim Green\"}"
TTL="1800s"

echo ">zbctl publish message \"$MESSAGE_NAME\" --correlationKey \"$CORRELATION_KEY\" --messageId \"$MESSAGE_ID\" --variables \"$VARIABLES_JSON\" --ttl $TTL --insecure"
zbctl publish message "$MESSAGE_NAME" --correlationKey "$CORRELATION_KEY" --messageId "$MESSAGE_ID" --variables "$VARIABLES_JSON" --ttl $TTL --insecure