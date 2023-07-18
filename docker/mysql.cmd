docker network create database
docker run --network=database --name some-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql