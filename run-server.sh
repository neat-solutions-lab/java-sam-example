#!/usr/bin/env bash

SMS_ENV_USER_1="environment-demo-user:{noop}environment-demo-password USER" \
SMS_ENV_TOKEN_1="TOKEN_READ_FROM_ENVIRONMENT environment-demo-user USER" \
java -jar target/java-sam-example-0.0.1-SNAPSHOT.jar
