@echo off
echo Starting PC Component Store in Development Mode...
echo.

set SPRING_PROFILES_ACTIVE=dev
set DB_URL=jdbc:mysql://localhost:3306/pc_component_store_dev?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
set DB_USERNAME=root
set DB_PASSWORD=root

echo Environment Variables Set:
echo SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE%
echo DB_URL=%DB_URL%
echo DB_USERNAME=%DB_USERNAME%
echo.

java -jar target\pc-component-store-1.0.0.jar

pause 