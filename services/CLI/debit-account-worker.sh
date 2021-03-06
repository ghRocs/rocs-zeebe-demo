#!/bin/bash
TYPE="debit-account"

echo ">zbctl create worker $TYPE --handler \"echo\" --insecure"
zbctl create worker $TYPE --handler "echo" --insecure