@echo off
echo ========================================
echo Checking for Android Device/Emulator
echo ========================================
echo.

echo Running: adb devices
echo.
adb devices
echo.

echo ========================================
echo.
echo If you see a device listed above, you're good to go!
echo If not:
echo   1. Connect your Android device via USB and enable USB Debugging
echo   2. OR start an Android Emulator from Android Studio
echo.
echo Once connected, run:
echo   gradlew.bat installDebug
echo.
pause
