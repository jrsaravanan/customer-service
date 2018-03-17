Customer Service
--

Customer Service : Get customer details


## Build
To generate ASCII doc

```sh
mvn package 
```

## To Run
```sh
mvn spring-boot:run

debug
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8008"

Run with profile

java -jar -Dspring.profiles.active=dev  target/customer-service-0.0.1-SNAPSHOT.jar

```



## Run Container in local
```sh

$ mvn dockerfile:build

$ export CUSTOMER_SVR_HOST_NAME=$(hostname  -I | cut -f1 -d " ")

$ docker run -d --name mysql-server -e MYSQL_DATABASE="customers" -e MYSQL_USER="appuser" -e MYSQL_PASSWORD="appuser"  -e MYSQL_ROOT_PASSWORD="appuser" -e MYSQL_ROOT_HOST=$CUSTOMER_SVR_HOST_NAME -p 3306:3306 mysql:latest

$ docker run -e CUSTOMER_APP_USER='appuser' -e CUSTOMER_APP_PASSWORD='appuser'  -e CUSTOMER_DB_URI=$CUSTOMER_SVR_HOST_NAME   -t jrsaravanan/customer-service

```