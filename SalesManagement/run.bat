@echo off
cd /d "%~dp0"
title Sales Management System
if not exist "%~dp0SalesManagement_v1.6.jar" (
    echo Khong tim thay file SalesManagement_v1.6.jar.
    pause
    exit /b 1
)
where javaw >nul 2>&1
if errorlevel 1 (
    echo Khong tim thay Java. Vui long cai dat JDK 8 tro len va them Java vao PATH.
    pause
    exit /b 1
)
start "Sales Management System v1.6" /D "%~dp0" javaw -jar "%~dp0SalesManagement_v1.6.jar"
if errorlevel 1 (
    echo.
    echo Chuong trinh khoi dong that bai. Hay kiem tra Java va file SalesManagement_v1.6.jar.
    pause
)
