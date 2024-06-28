echo 'Waiting 20s for sql server to initialize keycloak database'
sleep 20s 
echo 'Start keycloak'
/opt/keycloak/bin/kc.sh start-dev