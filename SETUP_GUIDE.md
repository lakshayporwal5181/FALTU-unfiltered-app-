# FALTU App - Complete Setup Guide

## 📋 Quick Start Checklist

Before running the app, complete these steps:

- [ ] Configure Firebase
- [ ] Add OpenAI API Key
- [ ] Sync Gradle dependencies
- [ ] Build and run

---

## 1️⃣ Firebase Configuration (MANDATORY)

### Step 1: Create Firebase Project

1. Go to https://console.firebase.google.com/
2. Click "Add project" or select existing project
3. Enter project name: **FALTU** (or your choice)
4. Follow the wizard to create the project

### Step 2: Add Android App

1. In Firebase Console, click the **Android icon** to add an Android app
2. Enter the package name: `com.example.faltu`
3. Enter app nickname (optional): FALTU Android
4. Leave SHA-1 blank for now (required later for production)
5. Click "Register app"

### Step 3: Download google-services.json

1. Firebase will prompt you to download `google-services.json`
2. **IMPORTANT**: Place this file in the `app/` directory of your project
3. Path should be: `app/google-services.json`
4. Replace the placeholder file that's already there

### Step 4: Enable Authentication

1. In Firebase Console, go to **Build → Authentication**
2. Click "Get started"
3. Go to "Sign-in method" tab
4. Enable **Email/Password** provider
5. Click "Save"

### Step 5: Enable Firestore

1. In Firebase Console, go to **Build → Firestore Database**
2. Click "Create database"
3. Select **Start in test mode** (for development)
4. Choose a location (preferably closest to you)
5. Click "Enable"

**⚠️ Security Note**: Test mode allows open access. For production, configure proper security
rules (see README.md)

---

## 2️⃣ OpenAI API Configuration

### Option A: OpenAI (Recommended)

1. Go to https://platform.openai.com/api-keys
2. Sign in or create an account
3. Click "Create new secret key"
4. Copy the API key (starts with `sk-...`)
5. Open `app/src/main/java/com/example/faltu/data/api/RetrofitClient.kt`
6. Replace this line:
   ```kotlin
   private const val OPENAI_API_KEY = "YOUR_OPENAI_API_KEY_HERE"
   ```
   With:
   ```kotlin
   private const val OPENAI_API_KEY = "sk-your-actual-key-here"
   ```

**⚠️ Security Warning**: Never commit API keys to version control. For production, use:

- Environment variables
- BuildConfig secrets
- Secure key management service

### Option B: HuggingFace (Alternative)

If you prefer HuggingFace over OpenAI:

1. Get API key from https://huggingface.co/settings/tokens
2. Modify `RetrofitClient.kt`:
   ```kotlin
   private const val OPENAI_BASE_URL = "https://api-inference.huggingface.co/"
   private const val OPENAI_API_KEY = "hf_your_key_here"
   ```
3. Update the request format in repositories to match HuggingFace API spec

### Pricing Note

- **OpenAI GPT-3.5-turbo**: ~$0.002 per 1K tokens
- **HuggingFace**: Free tier available, then paid
- Implement caching and rate limiting to control costs

---

## 3️⃣ Gradle Sync & Build

### Step 1: Open Project in Android Studio

1. Open Android Studio
2. Click "Open" and select the `FALTU` project folder
3. Wait for Gradle to index

### Step 2: Sync Dependencies

1. Android Studio should automatically prompt you to sync
2. If not, click **File → Sync Project with Gradle Files**
3. Wait for all dependencies to download (may take 5-10 minutes first time)

### Step 3: Resolve Any Issues

**If you see errors:**

- **google-services.json missing**: Make sure you added it to `app/` directory
- **SDK not found**: Install required SDK versions via SDK Manager
- **Dependency resolution failed**: Check internet connection and try again

### Step 4: Build the Project

1. Click **Build → Rebuild Project**
2. Wait for build to complete
3. Check "Build" tab for any errors

---

## 4️⃣ Run the App

### Prerequisites

- Android device with API 24+ (Android 7.0+) OR
- Android Emulator with API 24+

### Step 1: Connect Device / Start Emulator

**Physical Device:**

1. Enable Developer Options on your device
2. Enable USB Debugging
3. Connect via USB
4. Accept debugging permission

**Emulator:**

1. Click **Tools → Device Manager**
2. Create a new device if needed (Pixel 5, API 34 recommended)
3. Start the emulator

### Step 2: Run the App

1. Select your device from the dropdown in toolbar
2. Click the **Run** button (green play icon) or press `Shift + F10`
3. Wait for the app to install and launch

### Step 3: First Launch

1. You should see the **FALTU** sign-in/sign-up screen
2. Create a new account:
    - Enter display name
    - Enter email
    - Enter password (minimum 6 characters)
    - Click "Sign Up"
3. You'll be taken to the home screen with two cards

---

## 5️⃣ Permissions Setup

### Location Permission (for DateGPT missions)

1. When you click "Create Mission" in DateGPT
2. The app will request location permission
3. Grant "While using the app" or "Always" permission

### Usage Stats Permission (for LazyLegend)

1. When you open LazyLegend for the first time
2. You'll see a warning about Usage Access
3. Click the warning to open system settings
4. Find **FALTU** in the list
5. Toggle on "Permit usage access"
6. Return to the app and refresh

---

## 6️⃣ Testing the Features

### DateGPT - Chat Analysis

1. Tap the **DateGPT** card on home screen
2. Enter a sample message: "Hey, wanna grab coffee sometime? 😊"
3. Tap "Analyze Chat"
4. Wait for AI response (requires OpenAI API key)
5. You'll see:
    - Tone (Flirty, Confident, etc.)
    - Confidence level (0-100%)
    - Feedback
    - Funny advice

### LazyLegend - Screen Time & Roast

1. Tap the **LazyLegend** card on home screen
2. Grant usage stats permission (see above)
3. Screen time data will load automatically
4. View your total, productive, and wasted time
5. Tap "Get Roasted 🔥" for an AI-generated roast
6. The roast severity depends on your screen time:
    - Light: < 3 hours
    - Medium: 3-6 hours
    - Savage: > 6 hours

### RunAnywhere SDK (Location-Based Missions)

The custom RunAnywhere SDK implementation is integrated throughout:

- **DateGPT**: "Confidence Missions" use location tracking
- **LazyLegend**: "Walk 500m" challenges track distance

Test by walking around with the app open and GPS enabled.

---

## 7️⃣ Troubleshooting

### Firebase Authentication Errors

**Error**: "Email already in use"

- **Solution**: Use a different email or sign in instead

**Error**: "The supplied auth credential is incorrect"

- **Solution**: Check your password, or reset it via Firebase Console

**Error**: "Network error"

- **Solution**: Check internet connection and Firebase project status

### OpenAI API Errors

**Error**: "Invalid API key"

- **Solution**: Verify your API key in `RetrofitClient.kt`

**Error**: "Rate limit exceeded"

- **Solution**: Wait a minute or upgrade your OpenAI plan

**Error**: "Insufficient quota"

- **Solution**: Add credits to your OpenAI account

### Location Not Working

- Ensure location permission is granted
- Enable GPS/Location Services on device
- Check if Google Play Services is updated
- Try outdoors for better GPS signal

### Usage Stats Not Available

- Manually grant permission in system settings
- Path: Settings → Apps → Special Access → Usage Access → FALTU
- Some devices may have different menu structures

### App Crashes on Launch

1. Check Logcat for error details
2. Verify `google-services.json` is present
3. Clean and rebuild project: **Build → Clean Project** → **Rebuild Project**
4. Invalidate caches: **File → Invalidate Caches / Restart**

---

## 8️⃣ Development Tips

### Viewing Firestore Data

1. Go to Firebase Console
2. Navigate to **Firestore Database**
3. You'll see collections: `users`, `chat_analyses`, `roasts`, etc.
4. Click to view documents and data

### Testing Without API Costs

To avoid API charges during development:

1. Use mock responses in repositories
2. Comment out actual API calls
3. Return hardcoded `ChatAnalysis` or `Roast` objects

Example:

```kotlin
suspend fun analyzeChatTone(chatText: String): Result<ChatAnalysis> {
    // Bypass API for testing
    return Result.success(ChatAnalysis(
        id = "test",
        userId = "test_user",
        chatText = chatText,
        tone = "flirty",
        confidenceLevel = 85,
        feedback = "Great message! Very confident.",
        funnyAdvice = "Maybe add a joke? 😄"
    ))
}
```

### Debugging Location

Enable developer settings in Android Studio:

- **Tools → Layout Inspector**: View UI hierarchy
- **Logcat**: Filter by "FALTU" or package name
- **Database Inspector**: View Room database (if used)

---

## 9️⃣ Building APK for Distribution

### Debug APK (for testing)

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for production)

1. Generate signing key:
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. In Android Studio: **Build → Generate Signed Bundle / APK**
3. Select **APK**
4. Choose your keystore (or create new)
5. Select **release** build variant
6. Wait for build to complete

Output: `app/build/outputs/apk/release/app-release.apk`

### Installing APK

**On Device:**

1. Transfer APK to device
2. Open file manager and tap APK
3. Allow "Install from unknown sources" if prompted
4. Install and open

**Via ADB:**

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔟 Next Steps

### For Production

1. **Configure Firebase Security Rules** (see README.md)
2. **Secure API Keys** (use BuildConfig or backend proxy)
3. **Implement Payment Integration** (Razorpay, Google Play Billing)
4. **Add Analytics** (Firebase Analytics, Mixpanel)
5. **Add Crash Reporting** (Firebase Crashlytics)
6. **Optimize Performance** (ProGuard, R8)
7. **Test on Multiple Devices** (different screen sizes, Android versions)
8. **Implement Rate Limiting** (prevent API abuse)

### Additional Features to Add

- Push notifications for missions/roasts
- Social features (leaderboards, friend challenges)
- More roast voices (text-to-speech)
- Export reports (PDF, share)
- Dark/light theme toggle
- Multiple language support
- Offline mode with sync

---

## 📞 Support

If you encounter issues:

1. Check Logcat for detailed error messages
2. Review Firebase Console for authentication/database errors
3. Verify all configuration steps were completed
4. Check the troubleshooting section above

For common issues, refer to:

- Firebase Documentation: https://firebase.google.com/docs
- OpenAI API Docs: https://platform.openai.com/docs
- Android Developer Docs: https://developer.android.com

---

**🎉 Congratulations! Your FALTU app is now ready to run!**

**Remember**: This is a demonstration project. For production deployment, implement proper security,
error handling, and testing.

---

**FALTU - Making life fun with AI, one roast at a time! 🔥😜**
