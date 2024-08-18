FROM node:22-alpine
WORKDIR /app/react-app 
COPY ./dist/ /app/react-app/dist/
COPY package.json .
COPY vite.config.ts .

RUN npm install typescript

EXPOSE 5173
CMD [ "npm", "run", "preview" ]