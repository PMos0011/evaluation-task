call kubectl delete deployment customer
call kubectl delete deployment credit
call kubectl delete deployment product
call kubectl delete deployment postgres

call kubectl delete service customer-service
call kubectl delete service product-service
call kubectl delete service credit-service
call kubectl delete service postgres-service

call kubectl delete configmap customer-config
call kubectl delete configmap product-config
call kubectl delete configmap postgres-config
call kubectl delete configmap db-init

call docker rmi credit-img product-img customer-img
pause