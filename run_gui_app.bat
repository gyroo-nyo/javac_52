@echo off
title JAVAC Healthcare Management System - GUI Application
echo =========================================================================
echo    JAVAC HEALTHCARE MANAGEMENT SYSTEM (GUI + JAVA WEB SERVER)
echo =========================================================================
echo Compiling latest Java source files...
javac -cp "HealthcareManagementSystem\lib\*" -d "HealthcareManagementSystem\target\classes" HealthcareManagementSystem\src\main\java\com\healthcare\*.java HealthcareManagementSystem\src\main\java\com\healthcare\model\*.java HealthcareManagementSystem\src\main\java\com\healthcare\dao\*.java HealthcareManagementSystem\src\main\java\com\healthcare\interfaces\*.java HealthcareManagementSystem\src\main\java\com\healthcare\exception\*.java HealthcareManagementSystem\src\main\java\com\healthcare\service\*.java HealthcareManagementSystem\src\main\java\com\healthcare\thread\*.java HealthcareManagementSystem\src\main\java\com\healthcare\util\*.java HealthcareManagementSystem\src\main\java\com\healthcare\web\*.java HealthcareManagementSystem\src\main\java\com\healthcare\servlet\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Starting application...
java -cp "HealthcareManagementSystem\target\classes;HealthcareManagementSystem\lib\*" com.healthcare.Main
pause
