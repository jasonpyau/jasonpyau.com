# Get Started (Ubuntu)

# Create application.properties (./backend/src/main/resouces/application.properties)
```
# MariaDB Spring Boot Connection
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mariadb://<SERVER_URL>/<DATABASE_NAME> #example jdbc:mariadb://192.168.1.1:3306/portfolio
spring.datasource.username=YOUR_USERNAME_HERE #example root
spring.datasource.password=YOUR_PASSWORD_HERE #example password
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
# Keep Connection Alive
spring.datasource.testWhileIdle=true
spring.datasource.validationQuery=SELECT 1
# Print for Each SQL Query
spring.jpa.show-sql=false

# Email Service
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_USERNAME_HERE #example jasonemailsender@gmail.com
spring.mail.password=YOUR_PASSWORD_HERE #example password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Backend Configuration
server.port=9000
spring.profiles.active=default

# Admin Panel password
com.jasonpyau.appPassword=YOUR_APP_PASSWORD_HERE #example password
# Email to send contact notifications to
com.jasonpyau.email=YOUR_EMAIL_HERE #example jason@gmail.com

```
# Create Constants.java (./admin/Constants.java)
```
// Rename file & class to "Constants"
public class Constants {
    private Constants() {};

    public static final String SERVER_URL = "YOUR_SERVER_URL_HERE"; // example 192.168.1.1:9000
    public static final String APP_PASSWORD = "YOUR_APP_PASSWORD_HERE"; // example password
}

```
# Run Spring Boot Project
```
sudo apt update

sudo apt upgrade

sudo apt install openjdk-17-jdk

sudo apt install maven

sudo mvn spring-boot:run
```
