#!/bin/bash
ZEEBE_ADDRESS="127.0.0.1:26500"

export ZEEBE_ADDRESS=$ZEEBE_ADDRESS
echo "Connection setting:--address $ZEEBE_ADDRESS"

echo ">zbctl status --insecure"
zbctl status --insecure
