#!/bin/bash

services=("auth-service" "employee-service" "department-service" "api-gateway")

for service in "${services[@]}"; do
  echo "Building $service..."
  cd $service
  docker build -t $service:latest .
  cd ..
done
