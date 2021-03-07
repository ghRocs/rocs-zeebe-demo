#!/bin/bash
TYPE="ship-commodity"

echo ">zbctl create worker $TYPE --handler \"echo\" --insecure"
zbctl create worker $TYPE --handler "echo" --insecure