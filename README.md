# jasonpyau.com - Personal Portfolio Website

## Table of Contents
<ol>
  <li><a href="#about">About</a></li>
  <li><a href="#dependencies">Dependencies</a></li>
  <li><a href="#get-started">Get Started</a></li>
</ol>


## About
This is a personal portfolio site made by me using Java Spring Boot. It utilizes the backend so that the site dynamically loads blog posts, projects, skills, etc. stored in the database that can be changed without the need to modify any code. This project is open source so you may use any of the code, credit appreciated. 

**Link:** <a href="https://jasonpyau.com">jasonpyau.com/</a>
<br>
**API Documentation:** <a href="https://app.swaggerhub.com/apis-docs/JASONYAU/jasonpyau.com/1.0.0#/">swaggerhub.com/apis-docs/JASONYAU/jasonpyau.com/1.0.0#/</a>

This website is hosted on my Raspberry Pi, using Cloudflare's free tunneling services for SSL certificate and DDoS protection and GitHub Actions for continuous deployment after each Git push.

## Dependencies
```
1. Spring Boot Web
2. Spring Boot Data JPA - Database
3. Spring Boot Thymeleaf - Load webpages through Spring Boot
4. MariaDB Driver - Database
5. Java Mail Sender - Send email notifications to myself
6. Bucket4J - Rate Limitter
7. Google Guava - Cache to Rate Limit
8. Lombok - Reduce repetitive code (Getter/Setters)
9. Spring Boot Validation - Validate user input
10. Spring Boot Test - Unit testing
11. Hamcrest - Unit testing
```

## Get Started
**(Ubuntu)**

**Create application.properties (./backend/src/main/resouces/application.properties)**

https://github.com/jasonpyau/jasonpyau.com/blob/fb316fd5f09d5b62cd46015f1fa1c2d0ffbc3e88/backend/src/main/resources/application.properties.sample#L1-L35

**Create Constants.java (./admin/Constants.java)**

https://github.com/jasonpyau/jasonpyau.com/blob/ac427271c88fe2a8cbfa2917f3cfd3e36ceeebf4/admin/ConstantsSample.java#L1-L8

**Run Spring Boot Project**
```
sudo apt update

sudo apt install openjdk-17-jdk

sudo apt install maven

sudo mvn spring-boot:run
```
