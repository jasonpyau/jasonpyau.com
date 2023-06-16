# Table of Contents
<li><a href="#about">About</a></li>
<li><a href="#dependencies">Dependencies</a></li>
<li><a href="#get-started">Get Started</a></li>


## About
This is a personal portfolio site made by me using Java Spring Boot. It utilizes the backend so that the site dynamically loads projects, skills, etc. stored in the database that can be changed without the need to modify any code. This project is open source so you may use any of the code, credit appreciated. 

**Link:** <a href="https://jasonpyau.com" target="_blank">jasonpyau.com/</a>
<br>
**API Documentation:** <a href="https://app.swaggerhub.com/apis/JASONYAU/jasonpyau.com/1.0.0#/" target="_blank">swaggerhub.com/apis/JASONYAU/jasonpyau.com/1.0.0#/</a>

This website is hosted on my Raspberry Pi, using Cloudflare's free tunneling services for SSL certification and DDoS protection.

## Dependencies
```
1. Spring Web
2. Spring Data JPA - Database
3. Thymeleaf - Load webpages through SpringBoot
4. MariaDB Driver - Database
5. Java Mail Sender - Send email notifications to myself
6. Bucket4J - Rate Limitter
7. Google Guava - Cache to Rate Limit
```

## Get Started
**(Ubuntu)**

**Create application.properties (./backend/src/main/resouces/application.properties)**

https://github.com/jasonpyau/jasonpyau.com/blob/ac427271c88fe2a8cbfa2917f3cfd3e36ceeebf4/backend/src/main/resources/application.properties.sample#L1-L28

**Create Constants.java (./admin/Constants.java)**

https://github.com/jasonpyau/jasonpyau.com/blob/ac427271c88fe2a8cbfa2917f3cfd3e36ceeebf4/admin/ConstantsSample.java#L1-L8

**Run Spring Boot Project**
```
sudo apt update

sudo apt upgrade

sudo apt install openjdk-17-jdk

sudo apt install maven

sudo mvn spring-boot:run
```
