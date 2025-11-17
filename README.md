# Microservices ZTNA (hexagonal skeleton)

Generated components:
- gateway-service
- identity-profile-service
- idea-service
- party-service
- voting-service
- moderation-service
- audit-service
- notification-service
- file-storage-service
- common-lib

Dev infra (docker-compose.dev.yml):
- Keycloak (http://localhost:8080) - admin/admin
- Postgres (5432)
- MinIO (9000)
- Kafka + Zookeeper (9092/2181)

Notes:
- This skeleton purposely avoids Eureka and Spring Cloud Config.
- Use environment variables or k8s ConfigMaps + Vault for config/secrets.
- Each service follows hexagonal layout.
- moderation-service includes a sample "moderator request" flow (in-memory).
- idea-service includes a sample "submit idea" flow (in-memory).
