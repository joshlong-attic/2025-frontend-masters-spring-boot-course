#!/usr/bin/env bash
ls -la target && rm -rf target || echo "no target directory to delete."
mvn -DskipTests -Pnative spring-javaformat:apply clean package
