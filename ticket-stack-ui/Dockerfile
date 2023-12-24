FROM node:19.8.1-buster-slim

WORKDIR /app

#Add '/app/node_modules/.bin' tp $PATH
ENV PATH /app/node_modules/.bin:$PATH

#Install dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm install
RUN npm install react-scripts@5.0.1 --global

#Add app
COPY . ./

#Start app
CMD ["npm", "start"]

