FROM mcr.microsoft.com/mssql/server:2019-CU24-ubuntu-20.04

WORKDIR /src

ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=10122003simp!simp

COPY user.sql ./
COPY product.sql ./
COPY order.sql ./
COPY notification.sql ./
COPY keycloak.sql ./
COPY setup-database.sh ./
COPY entrypoint.sh ./

CMD bash ./entrypoint.sh
