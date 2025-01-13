# jasonpyau.com - Personal Portfolio Website

## Table of Contents
<ol>
  <li><a href="#about">About</a></li>
  <li><a href="#demo">Demo</a></li>
  <li><a href="#get-started-ubuntu">Get Started</a></li>
</ol>


## About
This is a personal portfolio website made with Spring Boot (Java). It utilizes the backend so that the site dynamically loads blog posts, experiences, projects, and skills stored in the database that can be changed without the need to modify any code. All components of the website are **fully customizable** via the Admin Panel.

**Link:** <a href="https://jasonpyau.com">jasonpyau.com</a>
<br/>
**API Documentation:** <a href="https://app.swaggerhub.com/apis-docs/jasonpyau-com/jasonpyau.com/1.0.0">https://app.swaggerhub.com/apis-docs/jasonpyau-com/jasonpyau.com/1.0.0</a>

This website is hosted on my Raspberry Pi, using Cloudflare for protection and GitHub Actions to synchronize the main branch and the server.

## Demo

https://github.com/user-attachments/assets/9a5ac48d-69b7-4716-8486-bb61766c8925

## Get Started (Ubuntu)

**Prerequisites**
- MariaDB is installed and a database has been created.

<br/>

**Create `secrets.properties` `(./backend/src/main/resources/secrets.properties)`** -
<a href="https://github.com/jasonpyau/jasonpyau.com/blob/main/backend/src/main/resources/secrets.properties.sample">secrets.properties.sample</a>

```
# EX: default
SPRING_ACTIVE_PROFILE=YOUR_SPRING_ACTIVE_PROFILE_HERE

# EX: localhost:3306
MARIADB_SERVER_URL=YOUR_MARIADB_SERVER_URL_HERE
# EX: portfolio
MARIADB_DATABASE_NAME=YOUR_MARIADB_DATABASE_NAME_HERE
# EX: root
MARIADB_USERNAME=YOUR_MARIADB_USERNAME_HERE
# EX: password
MARIADB_PASSWORD=YOUR_MARIADB_PASSWORD_HERE

# EX: SENDER@gmail.com
EMAIL_NOTIFICATION_SENDER_ADDRESS=YOUR_EMAIL_NOTIFICATION_SENDER_ADDRESS_HERE
# SEE: https://support.google.com/mail/answer/185833?hl=en 
# EX: xxxxxxxxxxxxxxxx
EMAIL_NOTIFICATION_SENDER_PASSWORD=YOUR_EMAIL_NOTIFICATION_SENDER_PASSWORD_HERE
# EX: RECEIVER@gmail.com
EMAIL_NOTIFICATION_RECEIVER_ADDRESS=YOUR_EMAIL_NOTIFICATION_RECEIVER_ADDRESS_HERE

# EX: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
ADMIN_PANEL_PASSWORD=YOUR_ADMIN_PANEL_PASSWORD_HERE
```

<br/>

**Create `AdminPanel.properties` `(./admin/AdminPanel.properties)`** -
<a href="https://github.com/jasonpyau/jasonpyau.com/blob/main/admin/AdminPanel.properties.sample">AdminPanel.properties.sample</a>
```
# EX: http://localhost:9000
SERVER_URL=YOUR_SERVER_URL_HERE
# EX: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
ADMIN_PANEL_PASSWORD=YOUR_ADMIN_PANEL_PASSWORD_HERE
```

<br/>

**Optional**
- Modify default settings in `application.properties` `(./backend/src/main/resources/application.properties)` - <a href="https://github.com/jasonpyau/jasonpyau.com/blob/main/backend/src/main/resources/application.properties">application.properties</a>
- For the `Contact Me` section to work, create a Gmail account with an app password to send email notifications to your main email.
<br/>

**Run Spring Boot (runs both frontend and backend)**
```
# Install Java JDK >= 17
sudo apt update
sudo apt install openjdk-17-jdk

# Change directory to the backend directory
cd ./backend

# Run the Spring Boot Project via the included Maven Wrapper
sudo bash ./mvnw spring-boot:run
```

<br/>

**Run Admin Panel**
```
# Change directory to the Admin Panel directory
cd ./admin

# Compile and run AdminPanel.java
sudo bash ./runAdmin.sh # OR: sudo javac AdminPanel.java && sudo java AdminPanel && sudo rm -f *.class
```
