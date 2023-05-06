docker pull phpmyadmin/phpmyadmin:latest
docker run --network=database --name my-own-phpmyadmin -d --link some-mysql:db -p 8081:80 phpmyadmin/phpmyadmin