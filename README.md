# Table of Contents
1. [About](#About)
2. [Dependencies](#Dependencies)
3. [Get Started](#GetStarted)


## About <a name="About"></a>
This is a personal portfolio site made by me using Java Springboot. It utilizes the backend so that the site dynamically loads projects, skills, etc. stored in the database that can be changed without the need to modify any code. This project is open source so you may use any of the code, credit appreciated. 

## Dependencies <a name="Dependencies"></a>
```
1. Spring Web
2. Spring Data JPA - Database
3. Thymeleaf - Load webpages through SpringBoot
4. MariaDB Driver - Database
5. Java Mail Sender - Send email notifications to myself
6. Bucket4J - Rate Limitter
7. Google Guava - Cache to Rate Limit
```

## Get Started (Ubuntu) <a name="GetStarted"></a>

**Create application.properties (./backend/src/main/resouces/application.properties)**

https://github.com/jasonpyau/jasonpyau.com/blob/main/backend/src/main/resources/application.properties.sample

**Create Constants.java (./admin/Constants.java)**

https://github.com/jasonpyau/jasonpyau.com/blob/main/admin/ConstantsSample.java

**Run Spring Boot Project**
```
sudo apt update

sudo apt upgrade

sudo apt install openjdk-17-jdk

sudo apt install maven

sudo mvn spring-boot:run
```
