# 🚀 FALTU App - Quick Start Guide

## Prerequisites

- Android device or emulator (API 24+)
- Internet connection
- Firebase project configured (already done ✅)

## Installation

### Option 1: Run from Android Studio

1. Open Android Studio
2. Open the project: `C:/Users/laksh/AndroidStudioProjects/FALTU`
3. Wait for Gradle sync
4. Click the "Run" button (▶️)
5. Select your device/emulator

### Option 2: Install APK

1. Build the APK:
   ```powershell
   .\gradlew.bat assembleDebug
   ```
2. APK location: `app/build/outputs/apk/debug/app-debug.apk`
3. Transfer to device and install

## 🎯 Testing Flow

### 1. First Launch - Sign Up ✅

**What to expect:**

- App opens to Authentication screen
- Two tabs: "Sign In" | "Sign Up"

**Test Sign Up:**

1. Tap "Sign Up" tab
2. Enter:
    - Display Name: `Test User`
    - Email: `test@example.com`
    - Password: `password123`
3. Tap "Sign Up" button
4. ✅ Success: Redirected to Main Dashboard with welcome message

**Expected Result:**

```
Welcome, Test User!
```

### 2. Main Dashboard 🏠

**What to see:**

- Welcome message with your name
- Two feature cards:
    - **DateGPT 💘** - AI Wingman for Dating
    - **LazyLegend 🔥** - Get Roasted, Get Productive
- Bottom navigation (Home/Stats/Missions/Profile)

**Test Navigation:**

- Bottom nav items show "Coming Soon" toasts ✅
- Home is already selected ✅

### 3. Testing DateGPT 💘

**Step 1: Open DateGPT**

- Tap the "DateGPT" card
- Screen shows input field and "Analyze" button

**Step 2: Test Chat Analysis**

1. Type a message in the input field:
   ```
   Hey! You looked amazing today. Would you like to grab coffee sometime? ☕
   ```
2. Tap "Analyze Tone" button
3. Wait for loading (shows progress bar)

**Expected API Response:**

```json
{
  "tone": "flirty",
  "confidenceLevel": 85,
  "feedback": "Great opener! You're confident and direct.",
  "funnyAdvice": "Add a wink emoji and you're golden! 😉"
}
```

**Result Display:**

- ✅ Tone: FLIRTY (in neon pink)
- ✅ Confidence Level: 85%
- ✅ Feedback text
- ✅ Funny advice text

**Test Different Messages:**

**Awkward Message:**

```
Um... hi... maybe we could... if you want... coffee?
```

Expected: Tone = "awkward", Confidence = 30-40%

**Confident Message:**

```
Let's grab dinner Friday. I know a great place. You free at 7?
```

Expected: Tone = "confident", Confidence = 90%+

**Step 3: Test Missions**

1. Tap "Create Mission" button
2. Should request location permission
3. Grant permission
4. Toast: "Creating confidence mission... 💪"

**Step 4: Test Pro Upgrade**

1. Tap "Upgrade to Pro" button
2. Dialog shows:
    - Title: "Upgrade to Pro 💎"
    - Price: ₹99/month
    - Features list
3. Tap "Upgrade" → Shows "Payment integration coming soon!"

### 4. Testing LazyLegend 🔥

**Step 1: Open LazyLegend**

- Go back to home (back button)
- Tap "LazyLegend" card
- Screen shows screen time stats

**Step 2: Grant Usage Permission**

1. Warning appears: "⚠️ Grant Usage Access to track screen time"
2. Tap the warning message
3. Redirected to system settings
4. Find "FALTU" in the list
5. Toggle ON "Permit usage access"
6. Press back to return to app
7. Stats should load automatically

**Expected Stats Display:**

```
Screen Time Today
Total Time: 3h 45m
Productive: 45 min
Wasted: 180 min

Top Apps:
📱 YouTube: 120 min
📱 Instagram: 90 min
📱 Chrome: 45 min
📱 WhatsApp: 30 min
📱 Gmail: 20 min
```

**Step 3: Test Roast Generation**

1. Tap "Get Roasted 🔥" button
2. Loading appears
3. AI generates roast based on screen time

**Expected Roast Examples:**

**Light (< 3 hours):**

```
"Not bad! You're using your phone like a normal human. Keep it up! 👍"
```

**Medium (3-6 hours):**

```
"3 hours and 45 minutes? Your phone must think you're in a committed relationship! 
Time to see the outside world, champ! 😅"
```

**Savage (> 6 hours):**

```
"6+ hours?! Your phone's battery has more social life than you do! 
Put it down and touch some grass! 🔥"
```

**Step 4: Test Boss Mode**

1. Tap "Upgrade to Boss Mode" button
2. Dialog shows:
    - Title: "Upgrade to Boss Mode 👑"
    - Price: ₹79/month
    - Features list
3. Tap "Upgrade" → Shows "Payment integration coming soon!"

### 5. Sign Out & Sign In ✅

**Test Sign Out (Manual):**
Currently, you need to clear app data to test sign-in flow again:

1. Settings → Apps → FALTU
2. Clear Data
3. Reopen app

**Test Sign In:**

1. App opens to Auth screen
2. Stay on "Sign In" tab (default)
3. Enter:
    - Email: `test@example.com`
    - Password: `password123`
4. Tap "Sign In"
5. ✅ Success: Redirected to Main Dashboard

## 🧪 API Testing

### Test Gemini API Directly

**Endpoint:**

```
POST https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBAF1EcjEit1qEz5JAcLkoICJdwApOq3Gk
```

**Sample Request:**

```json
{
  "contents": [{
    "parts": [{
      "text": "Say hello in a fun way!"
    }]
  }]
}
```

**Expected Response:**

```json
{
  "candidates": [{
    "content": {
      "parts": [{
        "text": "Hey there, superstar! 🌟"
      }]
    }
  }]
}
```

### Check API Status

Run in terminal:

```bash
curl -X POST "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBAF1EcjEit1qEz5JAcLkoICJdwApOq3Gk" \
-H "Content-Type: application/json" \
-d '{"contents":[{"parts":[{"text":"test"}]}]}'
```

## 🐛 Troubleshooting

### Issue: App crashes on launch

**Solution:** Clean and rebuild

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

### Issue: "API 404 Error"

**Status:** ✅ FIXED
**Check:** API key is correct in `RetrofitClient.kt`

### Issue: "Sign In failed"

**Possible causes:**

1. No internet connection
2. Wrong credentials
3. Firebase not initialized

**Solution:** Check logcat for Firebase errors

### Issue: Screen time shows "No data"

**Cause:** Usage permission not granted
**Solution:**

1. Go to Settings → Apps → Special Access → Usage Access
2. Enable for FALTU

### Issue: DateGPT analysis takes too long

**Cause:** Slow internet or API rate limit
**Wait time:** Usually 2-5 seconds
**Solution:** Check internet connection

## 📱 Test Scenarios

### Scenario 1: New User Journey

1. ✅ Sign Up → Main Dashboard
2. ✅ Open DateGPT → Analyze message
3. ✅ Back → Open LazyLegend
4. ✅ Grant permissions → View stats
5. ✅ Get roasted

### Scenario 2: Returning User

1. ✅ Open app → Auto sign in
2. ✅ Welcome message appears
3. ✅ Navigate to features

### Scenario 3: Error Handling

1. ✅ Wrong password → Error toast
2. ✅ Empty fields → "Fill all fields"
3. ✅ No internet → "Network error"

## ✅ Success Criteria

### Authentication

- [ ] Sign up creates account
- [ ] Sign in works with correct credentials
- [ ] Error messages appear for wrong input
- [ ] Session persists across app restarts

### DateGPT

- [ ] Analysis completes successfully
- [ ] Tone is displayed correctly
- [ ] Confidence level shows percentage
- [ ] Feedback and advice appear
- [ ] No 404 errors

### LazyLegend

- [ ] Permission request appears
- [ ] Stats load after granting permission
- [ ] Top apps list displays
- [ ] Roast generation works
- [ ] Different severity levels

### UI/UX

- [ ] No crashes
- [ ] Smooth navigation
- [ ] Buttons respond to taps
- [ ] Loading indicators appear
- [ ] Toasts show feedback

## 🎉 You're All Set!

If all tests pass, the app is working perfectly!

**Key Points:**

- ✅ Sign In/Sign Up implemented
- ✅ Gemini API integrated (no 404 errors)
- ✅ DateGPT fully functional
- ✅ LazyLegend fully functional
- ✅ Firebase connected
- ✅ Build successful

## 📞 Need Help?

Check the logs:

```bash
adb logcat | findstr "FALTU\|DateGPT\|LazyLegend\|Gemini"
```

Happy Testing! 🚀
