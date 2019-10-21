# idea
To be continued

---

#### Datasource
An in-memory database is available for development purposes. However, an additional data source
has been setup in the `application.properties` file in-order to revert to a production ready database.   

#### Session management   
Session management is normally handled by a servlet container, such as Tomcat or Jetty. However, this is not sufficient for distributed systems; thus a replacement session store/container has been setup in the `application.properties` file in-order to revert to a centralized production ready session store. Details see [Spring documentation.](https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot-redis.html)

#### WebSocket
Simple text oriented message protocol (STOMP) has been setup. Currently an in-memory version is running, however it will need to be replaced by a full-fledged message broker, such as RabbitMQ, in-order to scale.

#### Docker environment variables
A `.env` file is required by `docker-compose.yml` in order to properly setup environment
variables. See `application.properties` to identify what variables the application requires.

#### Pitfall
There may be instances where there is not enough entropy in a system to quickly generate a psuedorandom number. This can cause
issues. To resolve this, install `haveged`. For additional details visit [this tutorial](https://www.digitalocean.com/community/tutorials/how-to-setup-additional-entropy-for-cloud-servers-using-haveged) from Digital Ocean.
