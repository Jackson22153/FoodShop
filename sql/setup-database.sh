echo 'Please wait while Sql Server warms up'
sleep 5s
echo 'Initializing database'
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 10122003simp!simp -d master -i keycloak.sql -i user.sql -i payment.sql -i profile.sql -i order.sql -i product.sql -i notification.sql

echo 'Finished initializing the database'