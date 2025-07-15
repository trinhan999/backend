@echo off
echo ========================================
echo PC Component Store Backend Setup
echo ========================================
echo.

echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven wrapper...
if not exist "mvnw.cmd" (
    echo ERROR: Maven wrapper not found
    echo Please ensure mvnw.cmd exists in the project root
    pause
    exit /b 1
)

echo.
echo Downloading Maven wrapper dependencies...
call mvnw.cmd --version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to download Maven wrapper
    pause
    exit /b 1
)

echo.
echo Building project...
call mvnw.cmd clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo ========================================
echo Setup completed successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Create MySQL database: pc_component_store
echo 2. Update database credentials in application.yml
echo 3. Run: mvnw.cmd spring-boot:run
echo.
pause 