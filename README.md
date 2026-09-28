# ecommerce-marketplace
e-commerce marketplace built with Java, Spring Boot, Spring Security, JPA/Hibernate, and MySQL.

## Redis

Redis is required for JWT access-token revocation. Start the local Redis service before running the application:

```bash
docker compose up -d redis
```

The application connects to `localhost:6379` by default. Override this with the `REDIS_HOST` and `REDIS_PORT` environment variables when Redis runs elsewhere.
