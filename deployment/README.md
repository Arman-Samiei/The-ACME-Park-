## Deployment

This serves as a single quick reference for starting the entire app. It will launch the
necessary services:

- RabbitMQ (in [infrastructure/message-broker](../infrastructure/message-broker))
- Eureka Registry (in [infrastructure/service-registry](../infrastructure/service-registry))
- All services (in [services](../services))

### Two Stage Build

We are using two stage build. In the first stage, the compilation and build will be done. So, there is no need to run
the ```mvn clean package``` for services manually.

### Starting Server and Client

1. Create shared network for services
   ```bash
   docker network create shared-network
   ```
2. The rest of the commands are in the `deployment` folder. Run the following commands:
    ```bash
    docker compose build
    docker compose up -d
    ```
3. To stop the services:
    ```bash
    docker compose down
    ```

View the logs in the respective images.