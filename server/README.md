# Getting started with server

## System requirements
- Java version: 17 or higher
- Apache Maven version: 3.9.9 or higher
- Running postgresSQL database
- docker desktop

## postgresSQL Database
Using docker, the postgresSQL server is started by running `cd server && docker compose up`

## env variables
The env file should contain.

### DATABASE_URL
This is the url to the database.

Format example: `jdbc:postgresql://localhost:3306/Table`.

### DATABASE_USERNAME
This is the username you use to log in to the database.

### DATABASE_PASSWORD
This is the password for the username you want to log in with.

## DATABASE_NAME
The name of the database.

### SERVER_PORT
The port the server is running on.

### API_BASE_URL
The base url to the client BlueCTRL.

### USE_SSL
This is a boolean value. Above are all the variables you need in the env if USE_SSL is set to false. 

If this variable is set to true, the rest are required aswell.

### CERT_PASS
The SSL certificate private key. Add the path to the certificate private key.

### KEY_NAME
The location of the chain you want to use the password on.

## Get started
To start the server there are two different ways.

### Run `main.java`
Run the main.java file located inside src/java/no/ntnu folder.

### Run `mvn spring-boot:run` or `sudo mvn spring-boot:run`
To run the server from console you can run the command `mvn spring-boot:run`. If that fails it could be because of the SSL certification files is located inside a locked folder. In that case run `mvn spring-boot:run`.