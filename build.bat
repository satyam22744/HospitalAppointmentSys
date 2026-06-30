@echo off
echo ==============================================
echo  Hospital Appointment System - Build Script
echo ==============================================

:: Create directories
if not exist lib mkdir lib
if not exist bin mkdir bin
if not exist src mkdir src

:: Check and download libraries
echo Checking dependencies...

if not exist lib\flatlaf-3.4.1.jar echo Downloading FlatLaf (UI Theme)...
if not exist lib\flatlaf-3.4.1.jar powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar' -OutFile 'lib/flatlaf-3.4.1.jar'"

if not exist lib\sqlite-jdbc-3.45.3.0.jar echo Downloading SQLite JDBC Driver...
if not exist lib\sqlite-jdbc-3.45.3.0.jar powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar' -OutFile 'lib/sqlite-jdbc-3.45.3.0.jar'"

if not exist lib\slf4j-api-2.0.9.jar echo Downloading SLF4J API...
if not exist lib\slf4j-api-2.0.9.jar powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar' -OutFile 'lib/slf4j-api-2.0.9.jar'"

if not exist lib\slf4j-simple-2.0.9.jar echo Downloading SLF4J Simple Logger...
if not exist lib\slf4j-simple-2.0.9.jar powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar' -OutFile 'lib/slf4j-simple-2.0.9.jar'"

echo Dependencies verified!

:: Clean previous bin directory contents
echo Cleaning old build output...
if exist bin del /s /q bin\* >nul 2>&1

:: Compile sources
echo Compiling source files...
if exist sources.txt del sources.txt
dir /s /b src\*.java > sources.txt 2>nul

if not exist sources.txt echo No Java source files found. Create your code files under src/ first.
if not exist sources.txt exit /b 1

javac -d bin -cp "lib/*" @sources.txt
if %ERRORLEVEL% neq 0 (
    echo Compilation FAILED!
    if exist sources.txt del sources.txt
    exit /b %ERRORLEVEL%
)

if exist sources.txt del sources.txt
echo Compilation successful! Output stored in bin\
echo You can run the app using run.bat
exit /b 0
