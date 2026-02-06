Steps to run -DockerFile
1. Build Jar
mvn clean package

2. Build Docker image
docker build -t user-service .

3. Run Docker Container
docker run -p 8080:8080 user-service

Server starts at:
http://localhost:8080

API Endpoints

1. GET /users/:id
   http://localhost:8080/users/1

2. POST /users

3. DELETE /users/:id

4. PUT /users/:id/email

