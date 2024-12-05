[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/j07qAgWR)

## Group 2 Final Project
Provides an implementation of a Parking Service.
- Register permits
- Enter and Exit lots with a permit, with a voucher, or as a visitor
- Payment services to external bank and also external payslip system
- Officers can issue fines as well


To run the services and client for the Final Project, there are individual instructions in the in the [deployment](deployment/README.md) folder.

In the root folder we can run the `./runner.sh` script to run all the services and the external servers. The main services are
in the same container and then the external services are in their own containers. Assuming that Docker is installed and running,
the script just enters the [deployment](deployment) and [external-services](external-services) folders and runs:
1. `docker network create shared-network` if the network doesn't exist
2. `docker compose down`
3. `docker compose build`
4. `docker compose up -d`