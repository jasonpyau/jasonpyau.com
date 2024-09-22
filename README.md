# jasonpyau.com - Personal Portfolio Website

## Table of Contents
<ol>
  <li><a href="#about">About</a></li>
  <li><a href="#get-started">Get Started</a></li>
</ol>


## About
This is a personal portfolio site made by me using Java Spring Boot. It utilizes the backend so that the site dynamically loads blog posts, projects, skills, etc. stored in the database that can be changed without the need to modify any code. This project is open source so you may use any of the code, credit appreciated. 

**Link:** <a href="https://jasonpyau.com">jasonpyau.com/</a>
<br>
**API Documentation:** <a href="https://app.swaggerhub.com/apis-docs/jasonpyau-com/jasonpyau.com/1.0.0">https://app.swaggerhub.com/apis-docs/jasonpyau-com/jasonpyau.com/1.0.0</a>

This website is hosted on my Raspberry Pi, using Cloudflare's free tunneling services for SSL certificate and DDoS protection and GitHub Actions for continuous deployment after each Git push.


## Get Started
**(Ubuntu)**

**Create secrets.properties (./backend/src/main/resouces/secrets.properties)** -
<a href="https://github.com/jasonpyau/jasonpyau.com/blob/main/backend/src/main/resources/secrets.properties.sample">secrets.properties.sample</a>

```
SPRING_ACTIVE_PROFILE=default

MARIADB_SERVER_URL=YOUR_MARIADB_SERVER_URL_HERE
MARIADB_DATABASE_NAME=YOUR_MARIADB_DATABASE_NAME_HERE
MARIADB_USERNAME=YOUR_MARIADB_USERNAME_HERE
MARIADB_PASSWORD=YOUR_MARIADB_PASSWORD_HERE

EMAIL_NOTIFICATION_SENDER_ADDRESS=YOUR_EMAIL_NOTIFICATION_SENDER_ADDRESS_HERE
EMAIL_NOTIFICATION_SENDER_PASSWORD=YOUR_EMAIL_NOTIFICATION_SENDER_PASSWORD_HERE
EMAIL_NOTIFICATION_RECEIVER_ADDRESS=YOUR_EMAIL_NOTIFICATION_RECEIVER_ADDRESS_HERE

ADMIN_PANEL_PASSWORD=YOUR_ADMIN_PANEL_PASSWORD_HERE
RESUME_LINK=/files/Resume_Template.pdf
```

**Create AdminPanel.properties (./admin/AdminPanel.properties)** -
<a href="https://github.com/jasonpyau/jasonpyau.com/blob/main/admin/AdminPanel.properties.sample">AdminPanel.properties.sample</a>
```
SERVER_URL=YOUR_SERVER_URL_HERE
ADMIN_PANEL_PASSWORD=YOUR_ADMIN_PANEL_PASSWORD_HERE
```

**Run Spring Boot Project**
```
sudo apt update

sudo apt install openjdk-17-jdk

cd ./backend

sudo bash ./mvnw spring-boot:run
```
