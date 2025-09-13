#!/usr/bin/env bash

if [ -f "/run/secrets/admin_password" ]; then
	ADMIN_PASSWORD=$(cat /run/secrets/admin_password)
	export ADMIN_PASSWORD
fi
if [ -f "/run/secrets/db_user_password" ]; then
	DB_PASSWORD=$(cat /run/secrets/db_user_password)
	export DB_PASSWORD
fi

./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.devtools.restart.enabled=true"
