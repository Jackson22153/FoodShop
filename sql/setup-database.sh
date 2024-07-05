echo 'Please wait while Sql Server warms up'
sleep 5s
echo 'Initializing database'
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 10122003simp!simp -d master -i user.sql -i keycloak.sql -i profile.sql -i order.sql -i product.sql -i notification.sql

echo 'Finished initializing the database'