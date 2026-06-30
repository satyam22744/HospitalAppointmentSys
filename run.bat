@echo off
echo ==============================================
echo  Hospital Appointment System - Launcher
echo ==============================================

if not exist bin\com\hospital\Main.class (
    echo Error: Application has not been compiled yet.
    echo Please run build.bat first to compile and prepare dependencies.
    pause
    exit /b 1
)

echo Starting application...
java -Dorg.slf4j.simpleLogger.defaultLogLevel=warn -cp "bin;lib/*" com.hospital.Main
if %ERRORLEVEL% neq 0 (
    echo Application exited with error code: %ERRORLEVEL%
    pause
)
exit /b 0
