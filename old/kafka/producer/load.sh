#!/usr/bin/env bash

while true; do
  curl http://localhost:8080/view
  curl http://localhost:8080/view0?fail=true
  for i in  1 2; do
    curl http://localhost:8080/view${i}
  done
  sleep 0.5
done
