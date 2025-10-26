Contact Form Messaging System - Installation Guide
Prerequisites
Before you begin, ensure you have the following installed on your machine:

Java 21 or higher
Maven 3.6+
Docker Desktop (if running with Docker)
Git (to clone the repository)


Installation & Setup
Step 1: Clone the Repository
bashgit clone <your-repository-url>
cd dd.messaging-system
Step 2: Configure Email Settings
Edit src/main/resources/application.properties and add your email configuration:
properties# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Receiver Email Address
receiver.mail.address=contact@yourdomain.com

# Server Port
server.port=8080
Note for Gmail Users:

Use App Password instead of your regular password
Enable 2-Step Verification in your Google Account
Generate App Password: Google Account → Security → 2-Step Verification → App Passwords


Running the Application
Option 1: Run Locally (Without Docker)

Build the project:

bash   mvn clean install

Run the application:

bash   mvn spring-boot:run

Access the API:

   http://localhost:8080/api/contact/send

Option 2: Run with Docker

Build the project:

bash   mvn clean install

Build Docker image:

bash   docker build -t messaging-system:latest .

Run the container:

bash   docker run -d -p 8080:8080 --name messaging-system messaging-system:latest

Access the API:

   http://localhost:8080/api/contact/send

Testing the API
Using cURL:
bashcurl -X POST http://localhost:8080/api/contact/send \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "subject": "Program Inquiry",
    "message": "I would like to know more about your educational programs."
  }'
Using Postman:

Create a new POST request
URL: http://localhost:8080/api/contact/send
Headers: Content-Type: application/json
Body (raw JSON):

json   {
     "fullName": "John Doe",
     "email": "john@example.com",
     "subject": "Program Inquiry",
     "message": "I would like to know more about your educational programs."
   }

Docker Commands Reference
View running containers:
bashdocker ps
View all containers (including stopped):
bashdocker ps -a
Stop the container:
bashdocker stop messaging-system
Start the container:
bashdocker start messaging-system
Remove the container:
bashdocker rm messaging-system
View container logs:
bashdocker logs messaging-system
View live logs:
bashdocker logs -f messaging-system
Remove Docker image:
bashdocker rmi messaging-system:latest

Troubleshooting
Application won't start:

Check if port 8080 is already in use
Verify Java version: java -version
Check Maven installation: mvn -version

Email not sending:

Verify email credentials in application.properties
Check if App Password is used for Gmail
Ensure SMTP settings are correct

Docker container not accessible:

Ensure port mapping is correct: -p 8080:8080
Check if container is running: docker ps
View container logs: docker logs messaging-system

Port already in use:
Run on a different port:
bashdocker run -d -p 8081:8080 --name messaging-system messaging-system:latest
Then access: http://localhost:8081/api/contact/send

API Endpoint
POST /api/contact/send
Request Body:
json{
  "fullName": "string (required, 2-100 chars)",
  "email": "string (required, valid email)",
  "subject": "string (required, 3-200 chars)",
  "message": "string (required, 10-2000 chars)"
}
Success Response (200 OK):
json{
  "success": true,
  "message": "Message sent successfully! We'll get back to you within 24 hours.",
  "data": {
    "fullName": "John Doe",
    "email": "john@example.com",
    "subject": "Program Inquiry",
    "message": "I would like to know more about your educational programs.",
    "createdAt": "2025-10-26T16:30:00"
  }
}
Error Response (400 Bad Request):
json{
  "success": false,
  "message": "Validation failed. Please check the form fields.",
  "errors": {
    "email": "Please provide a valid email address",
    "message": "Message must be between 10 and 2000 characters"
  }
}

Quick Start Summary
bash# 1. Navigate to project directory
cd dd.messaging-system

# 2. Configure email in application.properties

# 3. Build the project
mvn clean install

# 4. Run with Docker
docker build -t messaging-system:latest .
docker run -d -p 8080:8080 --name messaging-system messaging-system:latest

# 5. Test the API
curl -X POST http://localhost:8080/api/contact/send \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","subject":"Test","message":"This is a test message"}'

