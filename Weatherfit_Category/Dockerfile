FROM node:18.16.0
WORKDIR /usr/src/app
COPY package*.json .
RUN npm install
RUN npm i pm2 -g
COPY . .
EXPOSE 8080
CMD ["pm2-runtime","app.js"]