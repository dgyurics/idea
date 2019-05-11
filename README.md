# idea
To be continued

---

#### Datasource
An in-memory database is available for development purposes. However, an additional data source
has been setup in the `application.properties` file in-order to revert to a production ready database.   

#### Session management   
Session management is normally handled by a servlet container, such as Tomcat or Jetty. However, this is not sufficient for distributed systems; thus a replacement session store/container has been setup in the `application.properties` file in-order to revert to a centralized production ready session store. Details see [Spring documentation.](https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-redis.html)

#### Docker environment variables
A `.env` file is required by `docker-compose.yml` in order to properly setup environment
variables. See `application.properties` to identify what variables the application requires.
