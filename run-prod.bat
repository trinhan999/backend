@echo off
echo Starting PC Component Store in Production Mode...
echo.

set SPRING_PROFILES_ACTIVE=prod

echo Environment Variables Set:
echo SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE%
echo.
echo IMPORTANT: Make sure to set the following environment variables:
echo - DB_URL
echo - DB_USERNAME  
echo - DB_PASSWORD
echo - APP_JWT_SECRET
echo.

java -jar target\pc-component-store-1.0.0.jar

pause 