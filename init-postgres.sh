#!/bin/bash

kubectl create configmap db-init --from-file=./postgres/db-init.sh
kubectl apply -f postgres.yml