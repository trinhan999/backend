# Configuration Guide

This document explains how to configure the PC Component Store backend application using environment variables and Spring profiles.

## Configuration Files

- `application.yml` - Base configuration with environment variable placeholders
- `application-dev.yml` - Development profile configuration
- `application-prod.yml` - Production profile configuration
- `env.example` - Example environment variables file

## Environment Variables

### Database Configuration
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password

### JPA Configuration
- `JPA_DDL_AUTO` - Hibernate DDL auto mode (validate, update, create, etc.)
- `JPA_SHOW_SQL` - Enable/disable SQL logging (true/false)
- `HIBERNATE_FORMAT_SQL` - Enable/disable SQL formatting (true/false)

### Flyway Configuration
- `FLYWAY_ENABLED` - Enable/disable Flyway migrations (true/false)

### JWT Configuration
- `JWT_SECRET` - JWT secret key for Spring Security
- `JWT_EXPIRATION` - JWT token expiration time in milliseconds
- `APP_JWT_SECRET` - Application JWT secret key
- `APP_JWT_EXPIRATION` - Application JWT expiration time in milliseconds
- `APP_JWT_REFRESH_EXPIRATION` - JWT refresh token expiration time in milliseconds

### Server Configuration
- `SERVER_PORT` - Server port number
- `SERVER_CONTEXT_PATH` - Application context path

### Logging Configuration
- `LOG_LEVEL_COM_ONLINESHOP` - Log level for application packages
- `LOG_LEVEL_SPRING_SECURITY` - Log level for Spring Security

## Profiles

### Development Profile (`dev`)
- Uses local database with development settings
- Enables SQL logging and formatting
- Uses `update` DDL auto mode for schema changes
- Extended JWT expiration for development convenience
- Detailed logging for debugging

### Production Profile (`prod`)
- Uses environment variables for database configuration
- Disables SQL logging and formatting
- Uses `validate` DDL auto mode for security
- Standard JWT expiration times
- Reduced logging levels for performance

## Running the Application

### Development Mode
```bash
# Set environment variables
export SPRING_PROFILES_ACTIVE=dev

# Run the application
./mvnw spring-boot:run
```

### Production Mode
```bash
# Set required environment variables
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://your-production-db:3306/pc_component_store
export DB_USERNAME=your_production_user
export DB_PASSWORD=your_production_password
export APP_JWT_SECRET=your-secure-production-secret

# Run the application
./mvnw spring-boot:run
```

### Using Environment File
1. Copy `env.example` to `.env`
2. Modify the values in `.env` file
3. Source the environment file:
   ```bash
   source .env
   ./mvnw spring-boot:run
   ```

## Security Considerations

### Production Deployment
1. **Never commit sensitive data** like passwords or JWT secrets to version control
2. Use strong, unique JWT secrets in production
3. Use environment variables or secure configuration management systems
4. Consider using a secrets management service for production deployments
5. Use SSL/TLS for database connections in production

### Environment Variables Best Practices
1. Use different databases for development and production
2. Use different JWT secrets for each environment
3. Set appropriate log levels for each environment
4. Use connection pooling configuration for production databases

## Example Docker Run Command

```bash
docker run -d \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://db:3306/pc_component_store \
  -e DB_USERNAME=app_user \
  -e DB_PASSWORD=secure_password \
  -e APP_JWT_SECRET=your-secure-production-secret \
  -p 8080:8080 \
  pc-component-store:latest
``` 