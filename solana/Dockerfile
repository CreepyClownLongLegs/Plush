FROM node:20

WORKDIR /app

COPY package*.json ./

RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

EXPOSE 3000

CMD ["npm", "run", "start"]