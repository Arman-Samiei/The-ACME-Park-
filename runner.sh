#!/bin/bash

set -e

compose_files=(
    "./deployment/docker-compose.yml"
    "./external-servers/external-bank/docker-compose.yml"
    "./external-servers/external-payslip/docker-compose.yml"
)

# Define the shared Docker network name
SHARED_NETWORK="shared-network"

# Check if the Docker network exists; if not, create it
if ! docker network inspect $SHARED_NETWORK >/dev/null 2>&1; then
    echo "Creating Docker network: $SHARED_NETWORK..."
    docker network create $SHARED_NETWORK
else
    echo "Docker network $SHARED_NETWORK already exists. Continuing..."
fi

for compose_file in "${compose_files[@]}"; do
    compose_dir=$(dirname "$compose_file")

    echo "Navigating to directory: $compose_dir"
    cd "$compose_dir"

    echo "Running 'docker compose down' for $compose_file..."
    docker compose down

    echo "Running 'docker compose build' for $compose_file..."
    docker compose build

    echo "Running 'docker compose up -d' for $compose_file..."
    docker compose up -d

    cd - > /dev/null
done

echo "All Docker Compose operations completed successfully!"