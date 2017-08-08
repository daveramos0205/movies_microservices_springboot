# movies_microservices_springboot
This project implements various spring boot based microservices displaying movietimes, 
theaters and user accounts from a MySQL database. 
Spring Cloud is used to configure the services based on different profiles. 
Netflix's Eureka framework provides service discovery functionality.
This was final project for Spring Boot with Microservices graduate course offered at UCSC Extension in Santa Clara, CA.

Steps to start:
1. Create a local git repository containing davidango-properties. Make sure the repo is initialized and all files are
committed.

2. Point davidango-config-service's application.properties's spring.cloud.config.server.git.uri to the location 
of davidango-properties.

3. Start davidango-eureka-service as a Spring Boot app. You can confirm it is up at localhost:8761, per the current config.

4. Start davidango-config-service as a Spring Boot app. It will register itself with Eureka and the url above can be used 
to confirm it is available.

5. Start remaining services: Movies_Theaters_Service on 8080 and Users_Accounts_Service on 8090. By changing these services' 
profiles in davidango-properties using the service's bootstrap.properties (select prod profile instead of default) and committing
these changes to the local git repo for davidango-properties, these services can then be restarted using ports 8081 and 8091, 
respectively.


