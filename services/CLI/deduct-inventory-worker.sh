#!/bin/bash
TYPE="deduct-inventory"

echo ">zbctl create worker $TYPE --handler \"echo\" --insecure"
zbctl create worker $TYPE --handler "echo" --insecure