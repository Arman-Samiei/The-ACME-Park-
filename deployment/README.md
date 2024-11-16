## Deployment

This serves as a single quick reference for starting the entire app. It will launch the
necessary services:
- RabbitMQ (in [infrastructure/message-broker](../infrastructure/message-broker))
- Eureka Registry (in [infrastructure/service-registry](../infrastructure/service-registry))
- Parking Controller (in [services/p-controller](../services/p-controller))

And it will also start the client, but it has to be manually started using a profile.

###  Starting Server and Client
1. In the root folder run the following command:
    ```bash
    mvn clean package
    ```
2. The rest of the commands are in the `deployment` folder. Run the following commands:
    ```bash
    docker compose --profile manual build --no-cache
    docker compose up -d
    ```
3. To run the client:
    ```bash
    docker compose --profile manual up -d
    ```
4. To stop the services:
    ```bash
    docker compose --profile manual down
    ```
View the logs in the respective images.