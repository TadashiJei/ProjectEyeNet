#!/bin/bash

# Ensure MongoDB is running
brew services start mongodb-community

# Wait for MongoDB to start
sleep 2

# Run the application with test profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
