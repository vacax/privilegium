#!/bin/bash
docker build -f Dockerfile -t web-pam .
docker-compose down && docker-compose --env-file env-demo.env up -d
