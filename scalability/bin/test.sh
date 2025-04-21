#!/usr/bin/env bash
echo  "the script port is ${1}"
hey -n 40 -c 20 http://localhost:${1}/delay