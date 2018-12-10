#!/usr/bin/env bash
set -x

curl -s http://demo-user:demo-password@localhost:8080/file-user-info

curl -s -H "Authorization: Bearer TOKEN_STORED_IN_FILE" http://localhost:8080/file-user-info

curl -s http://hardcoded-demo-user:hardcoded-demo-password@localhost:8080/hardcoded-user-info

curl -s -H "Authorization: Bearer TOKEN_HARDCODED_IN_ANNOTATION" http://localhost:8080/hardcoded-user-info

curl -s http://environment-demo-user:environment-demo-password@localhost:8080/environment-user-info

curl -s -H "Authorization: Bearer TOKEN_READ_FROM_ENVIRONMENT" http://localhost:8080/environment-user-info
