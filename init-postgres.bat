call kubectl create configmap db-init --from-file=.\postgres\db-init.sh
call kubectl apply -f postgres.yml
pause