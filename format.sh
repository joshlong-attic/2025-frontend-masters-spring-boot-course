#!/usr/bin/env bash
find . -iname pom.xml | while read l ; do
  mvn -f $l io.spring.javaformat:spring-javaformat-maven-plugin:0.0.43:apply
done