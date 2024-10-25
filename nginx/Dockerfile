FROM nginx:1.24.0

COPY nginx.conf ./etc/nginx/nginx.conf.template

CMD /bin/bash -c "envsubst '\$OAUTH2_PROXY \$SHOP_URL \$PAYMENT_URL \$AUTHORIZATION_URL \$ACCOUNT_URL \$NOTIFICATION_URL \$ORDER_URL \$REDIRECT_UI \$REDIRECT_LOGOUT_URI \$KEYCLOAK_USER_URL \$KEYCLOAK_ADMIN_URL \$ALLOWED_ORIGIN' < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf && exec nginx -g 'daemon off;'"