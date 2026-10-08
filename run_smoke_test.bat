@echo off
title JAVAC Healthcare Management System - Rubric Verification Test
echo =========================================================================
echo    RUNNING RUBRIC TEST SUITE (OOP, GENERICS, THREADING, DAOS, SERVLETS)
echo =========================================================================
java -cp "HealthcareManagementSystem\target\classes;HealthcareManagementSystem\lib\*" com.healthcare.SmokeTest
pause
