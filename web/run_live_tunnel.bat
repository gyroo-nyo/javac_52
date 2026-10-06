@echo off
title JAVACHealth HMS - Live Server & Cloudflare Tunnel
echo ========================================================
echo   Starting JAVACHealth HMS Live Web Server & Tunnel
echo ========================================================
start /b "" "C:\Program Files\Java\jdk-27\bin\jwebserver.exe" -b 127.0.0.1 -p 8080 -d "%~dp0."
timeout /t 2 /nobreak >nul
echo Web server running on http://127.0.0.1:8080
echo Establishing public live tunnel...
"%~dp0cloudflared.exe" tunnel --url http://127.0.0.1:8080
pause
