#!/usr/bin/env bash
ls -la  pom.xml &&  mvn spring-javaformat:apply
git add * 
git commit -am polish && git  push
