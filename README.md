# Conestoga Computer Science Club Sign-In API
This is the Conestoga Computer Science Club's sign-in and attendance tracking web service.

# Schema
To track attendance, users make sign-in requests to specific sign-in sessions. 
These requests are made to an endpoint that depends on the session's id and a code for the current time, and are tied to a specific user, specified by id.

During the creation of a sign-in session, a refresh rate for the secondary code may be specified, defaulting to 60 seconds.
Whether or not a request succeeds or fails depends on if the timecode specified in the request matches the current active one.

Each sign-in session is specific to a meeting, and a meeting may have multiple sessions.

## Requirements
- JDK 8 or above
- Maven
- A MySQL database

## Testing

### Setting Up
In `application.yml`, replace the values under `spring.datasource` to reflect your own MySQL server:
```yaml
spring:
  datasource:
    url: jdbc:mysql://{HOST}:{PORT}/{SCHEMA}    
    username: # username to access server
    password: # password
```

The schema must already exist, but the application will either generate SQL scripts to create and drop the tables, or create them directly itself.
By default, it will only generate SQL scripts and not create the tables directly.

To instead just generate the tables each time on initialization, comment-out this section of `application.yml`:
```yaml
jpa:
  properties:
    hibernate:
      dialect: org.hibernate.dialect.MySQL8Dialect
#   javax:
#     persistence:
#       schema-generation:
#         create-source: metadata
#         scripts:
#           action: create
#           create-target: create.sql
  generate-ddl: true
  hibernate:
    ddl-auto: create
```

For development, the application must run on SSL.

In `src/main/resources`, run the following to generate a self-signed `keystore.jks`:
```
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650
```

Follow the prompts and specify a password.
Alternatively, use an existing keystore.

Then, in `application.yml`, set the properties:
```yaml
key-store: classpath:keystore.jks # or relative path to your keystore.jks from src/main/resources
key-store-password: # your password here
```

By default, no users are created on initialization, meaning you cannot access the admin-protected endpoints of the service.
To easily generate a user with admin authorities on start, create the following class somewhere in the same or deeper package as `Application`:

```java
import net.edt.persistence.domain.Role;
import net.edt.persistence.domain.User;
import net.edt.persistence.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class OnInitialize implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        UserService userService = context.getBean(UserService.class);

        User admin = new User();
        admin.setName(/* name */);
        admin.setGrade(/* number between 8 and 13 */);
        admin.setEmail(/* email */);
        admin.setPassword(/* password */);
        admin.getRoles().add(Role.ADMIN);

        userService.create(admin);
    }

}
```
Unless this readme is updated, the imports may not be accurate.

Now, to run the application, run the `Application` class directly from your IDE, or in the project root, run: 
```
mvn clean install spring-boot:run
```
By default the service runs on port 4201.

A client running in the browser may not be able to make requests to service if the keystore is self-signed. 
Navigate directly to the application root endpoint in the browser and add an exception for it.

### Requesting
No OpenAPI v3 specification has been created for the API yet, so view the source code of the controllers at `src/main/java/net/edt/web/controller` to view the existing endpoints.

Only the endpoint '`/user/auth/register`' is open to use without authentication. 

The endpoint '`/user/auth/token`' is protected with basic authentication. The username is the user's email, and the password is the user's password.

All other endpoints are protected with basic authentication where the username is again the user's email, but the password is the token fetched from `/user/auth/token`.

All '`/admin/**`' endpoints are of course only accessible to users with the `ADMIN` role.