@echo off
echo ========================================
echo FALTU App - Setup Verification Script
echo ========================================
echo.

echo [1/5] Checking for google-services.json...
if exist "app\google-services.json" (
    echo ✓ google-services.json found
) else (
    echo ✗ google-services.json NOT found - Please add it to app/ directory
)
echo.

echo [2/5] Checking Gradle files...
if exist "gradlew.bat" (
    echo ✓ gradlew.bat found
) else (
    echo ✗ gradlew.bat NOT found
)
if exist "build.gradle.kts" (
    echo ✓ build.gradle.kts found
) else (
    echo ✗ build.gradle.kts NOT found
)
echo.

echo [3/5] Checking AndroidManifest.xml...
if exist "app\src\main\AndroidManifest.xml" (
    echo ✓ AndroidManifest.xml found
    findstr /C:"FaltuApplication" "app\src\main\AndroidManifest.xml" >nul
    if %errorlevel% equ 0 (
        echo ✓ FaltuApplication declared
    ) else (
        echo ✗ FaltuApplication NOT declared in manifest
    )
) else (
    echo ✗ AndroidManifest.xml NOT found
)
echo.

echo [4/5] Checking key source files...
if exist "app\src\main\java\com\example\faltu\MainActivity.kt" (
    echo ✓ MainActivity.kt found
) else (
    echo ✗ MainActivity.kt NOT found
)
if exist "app\src\main\java\com\example\faltu\ui\auth\AuthActivity.kt" (
    echo ✓ AuthActivity.kt found
) else (
    echo ✗ AuthActivity.kt NOT found
)
if exist "app\src\main\java\com\example\faltu\FaltuApplication.kt" (
    echo ✓ FaltuApplication.kt found
) else (
    echo ✗ FaltuApplication.kt NOT found
)
echo.

echo [5/5] Attempting to build...
echo Running: gradlew.bat clean assembleDebug
echo This may take a minute...
echo.
call gradlew.bat clean assembleDebug --quiet
if %errorlevel% equ 0 (
    echo ✓ Build SUCCESSFUL!
    echo.
    echo APK Location: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo ✗ Build FAILED
    echo Run 'gradlew.bat assembleDebug --stacktrace' for details
)
echo.

echo ========================================
echo Verification Complete
echo ========================================
echo.
echo Next steps:
echo 1. Install the app: gradlew.bat installDebug
echo 2. Check logs: adb logcat ^| findstr /i "MainActivity AuthActivity FaltuApp"
echo 3. Clear app data if needed: adb shell pm clear com.example.faltu
echo.
pause
