# idea
It all starts with an idea

---

#### Datasource
An in-memory database is available for development purposes. However, an additional data source
has been setup in the `application-dev.properties` file in-order to revert to a production ready database.

#### Session management   
There are no sessions. Authorization and authentication is accomplished using JSON Web Tokens (JWT).

#### Cache management
By default, `simple` cache management has been setup which utilizes a `ConcurrentHashMap`. In `application-dev.properties`
the cache manager is set to utilize `redis` in-order to revert to a production ready cache manager.

#### WebSocket
Simple text oriented message protocol (STOMP) has been setup. Currently an in-memory version is running, however it will need to be replaced by a full-fledged message broker, such as RabbitMQ, in-order to scale.

#### Docker environment variables
A `.env` file is required by `docker-compose.yml` and `Dockerfile` in order to properly setup environment
variables. See `application-prod.properties` to identify what variables the application requires.

#### Simulating a distributed environment
Running `docker-compose build` and `docker-compose up -d` will bring up external datasources. After this you can compile the project and utilize the external datasources using the following command, `mvn spring-boot:run -Dspring.profiles.active=dev`

#### Default admin account
`CommandLineRunner` class uses admin credentials passed in from the command line, see `Dockerfile`, to create 
an administrative account at startup. These credentials should be changed in production systems.

#### Pitfalls
There may be instances where there is not enough entropy in a system to quickly generate a psuedorandom number. This can cause
issues. To resolve this, install `haveged`. For additional details visit [this tutorial](https://www.digitalocean.com/community/tutorials/how-to-setup-additional-entropy-for-cloud-servers-using-haveged) from Digital Ocean.
