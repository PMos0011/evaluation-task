#!/bin/bash

kubectl delete deployment customer
kubectl delete deployment credit
kubectl delete deployment product
kubectl delete deployment postgres

kubectl delete service customer-service
kubectl delete service product-service
kubectl delete service credit-service
kubectl delete service postgres-service

kubectl delete configmap customer-config
kubectl delete configmap product-config
kubectl delete configmap postgres-config
kubectl delete configmap db-init

docker rmi credit-img product-img customer-img