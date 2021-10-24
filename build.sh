#!/bin/bash

for s in credit customer product; do
cd $s
mvn clean install
docker build -t $s-img .
cd ..
done