# FALTU - Complete Features List

## 🎯 App Overview

**FALTU** (Fun, AI & Life Together, Unfiltered) is an innovative Android lifestyle app that combines
AI-powered dating assistance with productivity enhancement through humor. The app features two
distinct modules unified under one platform with shared authentication and data management.

---

## 🔐 Authentication & User Management

### Sign Up / Sign In

- **Email/Password Authentication** via Firebase Auth
- Beautiful gradient-themed login screen
- Tab-based navigation between Sign In and Sign Up
- Automatic session management
- Password visibility toggle
- Input validation

### User Profile

- Display name customization
- Email verification support
- Subscription status tracking
- Points system (Confidence & Productivity scores)
- Mission completion counter

---

## 💘 Module 1: DateGPT - AI Dating Wingman

### Chat Tone Analysis

**Purpose**: Help users improve their dating communication confidence

**Features**:

- **Real-time AI Analysis**: Submit any chat message for instant feedback
- **Tone Detection**: AI identifies the tone (Flirty, Awkward, Confident, Boring, Cringe)
- **Confidence Scoring**: Get a confidence level rating (0-100%)
- **Constructive Feedback**: Personalized suggestions for improvement
- **Funny Advice**: Humorous tips to lighten the mood and boost confidence
- **Color-coded Results**: Visual feedback with gradient colors
- **Chat History**: View past analyses in Firestore

**Technology**:

- OpenAI GPT-3.5-turbo API
- Natural Language Processing for tone detection
- Firebase Firestore for data persistence

### Confidence Missions

**Purpose**: Real-world challenges to build social confidence

**Features**:

- **Location-Based Tasks**: Missions triggered by geographic proximity
- **RunAnywhere SDK Integration**: GPS tracking for mission completion
- **Mission Types**:
    - Talk to strangers nearby
    - Compliment someone at a specific location
    - Ask for directions in a crowded area
- **Progress Tracking**: Monitor task completion
- **Reward System**: Earn confidence points
- **Push Notifications**: Reminders and motivation

**Example Missions**:

- "Talk to 2 people near Coffee Shop XYZ"
- "Give a genuine compliment within 100m of your location"
- "Strike up a conversation at the park"

### Pro Subscription (₹99/month)

**Benefits**:

- ✅ Unlimited tone analysis (free tier: 5/day limit)
- ✅ Advanced AI feedback with detailed suggestions
- ✅ Exclusive premium missions
- ✅ Priority customer support
- ✅ Ad-free experience
- ✅ Export chat analysis reports

---

## 🔥 Module 2: LazyLegend - Productivity Through Roasting

### Screen Time Tracking

**Purpose**: Awareness of phone usage patterns

**Features**:

- **Real-Time Usage Stats**: Leverages Android UsageStatsManager API
- **Daily Breakdown**:
    - Total screen time (hours + minutes)
    - Productive time (work/learning apps)
    - Wasted time (social media, games)
- **Top Apps List**: See which apps consume most time
- **App Categorization**: Automatic classification
    - Productive (email, docs, calendar)
    - Social (Facebook, Instagram, Twitter)
    - Entertainment (YouTube, Netflix, games)
    - Other
- **Historical Data**: Week/month view with Firebase sync
- **Visual Charts**: Colorful progress indicators

**Technology**:

- Android UsageStatsManager API
- Custom ScreenTimeManager utility
- Firebase Firestore for cloud backup

### AI Roast Engine

**Purpose**: Motivational roasts to reduce screen time

**Features**:

- **Dynamic Severity Levels**:
    - **Light** (<3 hours): Gentle, playful teasing
    - **Medium** (3-6 hours): Moderate roasting with humor
    - **Savage** (>6 hours): Brutal but hilarious insults
- **Context-Aware**: Roasts based on specific app usage
- **Variety**: Different roasts each time
- **Shareable**: Share roasts with friends
- **Roast History**: View past roasts in timeline

**Sample Roasts**:

- Light: "4 hours? Not bad, but your screen misses you less than you think! 😄"
- Medium: "Spent 5 hours on social media? Even your profile pic is judging you! 🤦"
- Savage: "8 HOURS?! Your phone battery has more of a life than you do! Touch grass! 🌿"

**Technology**:

- OpenAI GPT-3.5-turbo with custom prompts
- Dynamic prompt engineering based on usage data
- Firebase storage for roast history

### Productivity Challenges

**Purpose**: Physical activities to break phone addiction

**Features**:

- **Walk Challenges**: "Walk 500m or get roasted again"
- **Focus Time**: Set phone-free durations
- **Usage Limits**: Cap app usage per day
- **RunAnywhere Integration**: GPS-tracked walking challenges
- **Progress Monitoring**: Real-time distance/time tracking
- **Reward System**: Productivity points for completion
- **Streak Tracking**: Daily/weekly consistency

**Challenge Types**:

- Walk-based (distance or time)
- App usage limits
- Phone-free periods
- Step count goals

### Boss Mode Subscription (₹79/month)

**Benefits**:

- ✅ Celebrity roast voices (Text-to-Speech)
- ✅ Detailed analytics dashboard
- ✅ Custom roast intensity levels
- ✅ Premium challenges with higher rewards
- ✅ Weekly productivity reports
- ✅ Comparison with friends (optional)
- ✅ Ad-free experience

---

## 🏠 Home Screen & Navigation

### Main Home Screen

- **Two Module Cards**: DateGPT & LazyLegend
- **Neon Gradient Design**: Pink, purple, blue theme
- **Emoji Icons**: 💘 for DateGPT, 🔥 for LazyLegend
- **Welcome Message**: Personalized with user's name
- **Quick Access**: Direct tap navigation to modules

### Bottom Navigation Bar

**Four Tabs**:

1. **Home**: Main screen with module cards
2. **Stats**: Combined stats from both modules
3. **Missions**: All active and completed missions
4. **Profile**: User settings and subscriptions

### Beautiful UI/UX

- **Material Design 3**: Latest components and styling
- **Dark Theme**: Background (#121212) with neon accents
- **Smooth Animations**: Card transitions and button effects
- **Gradient Backgrounds**: Pink → Purple → Blue
- **Custom Shapes**: Rounded corners (20dp) throughout
- **Elevation Effects**: 4-8dp shadows for depth
- **Ripple Effects**: Touch feedback on cards/buttons

---

## 📍 RunAnywhere SDK Features

### Location Tracking

- **GPS Integration**: Google Play Services Location API
- **Real-Time Updates**: Location updates every 2-5 seconds
- **Geofencing**: Check if user is within mission radius
- **Distance Calculation**: Haversine formula for accuracy
- **Permission Handling**: Runtime permission requests

### Mission Types

- **Talk Missions**: Social interaction at specific locations
- **Walk Challenges**: Distance-based objectives
- **Location Check-Ins**: Verify presence at coordinates
- **Radius Missions**: Complete tasks within area

### Distance Tracker

- **Step-by-Step Tracking**: Monitor walking progress
- **Total Distance**: Cumulative meters/kilometers
- **Visual Progress**: Progress bars and percentages
- **Background Tracking**: Works even when app is minimized

---

## 🔔 Notifications (Future Feature)

### Funny Notifications

- "Your crush typing... just kidding 😜"
- "Time for your daily roast! 🔥"
- "Mission Complete! You legend! 🎉"
- "You've been on your phone for too long 👀"
- "3 hours of Instagram? Even your grandma is disappointed"

### Notification Types

- Mission reminders
- Roast notifications (scheduled)
- Achievement unlocks
- Subscription renewal alerts
- Friend challenges (future)

---

## 💾 Data & Storage

### Firebase Firestore Collections

**users**

```
{
  uid: string
  email: string
  displayName: string
  isPro: boolean
  isBossMode: boolean
  subscriptionType: string
  createdAt: timestamp
  totalMissionsCompleted: number
  confidenceScore: number
  productivityScore: number
}
```

**chat_analyses**

```
{
  id: string
  userId: string
  chatText: string
  tone: string
  confidenceLevel: number
  feedback: string
  funnyAdvice: string
  timestamp: timestamp
}
```

**roasts**

```
{
  id: string
  userId: string
  roastText: string
  severity: string
  screenTimeMinutes: number
  voiceType: string
  timestamp: timestamp
}
```

**confidence_missions**

```
{
  id: string
  userId: string
  title: string
  description: string
  type: string
  targetCount: number
  currentProgress: number
  latitude: number
  longitude: number
  radiusMeters: number
  isCompleted: boolean
  reward: number
  createdAt: timestamp
  completedAt: timestamp
}
```

**productivity_challenges**

```
{
  id: string
  userId: string
  title: string
  description: string
  type: string
  targetValue: number
  currentProgress: number
  isCompleted: boolean
  reward: number
  createdAt: timestamp
  completedAt: timestamp
}
```

**screen_time**

```
{
  id: string
  userId: string
  date: string (yyyy-MM-dd)
  totalScreenTimeMinutes: number
  productiveTimeMinutes: number
  wastedTimeMinutes: number
  topApps: array
  roastCount: number
  timestamp: timestamp
}
```

---

## 🛡️ Permissions Required

### Mandatory Permissions

- **INTERNET**: For API calls and Firebase
- **ACCESS_FINE_LOCATION**: For RunAnywhere missions
- **PACKAGE_USAGE_STATS**: For screen time tracking

### Optional Permissions

- **POST_NOTIFICATIONS** (Android 13+): For notifications

### Permission Handling

- Runtime permission requests with rationale
- Graceful degradation if denied
- Deep links to system settings
- In-app warning banners for missing permissions

---

## 🎨 Design System

### Color Palette

- **Neon Pink**: #FF1493
- **Neon Purple**: #9D4EDD
- **Neon Blue**: #00D9FF
- **Neon Magenta**: #FF006E
- **Background**: #121212 (dark)
- **Surface**: #1E1E1E
- **Card Background**: #2A2A2A
- **Text Primary**: #FFFFFF
- **Text Secondary**: #B3B3B3
- **Success**: #4CAF50
- **Error**: #F44336
- **Warning**: #FF9800

### Typography

- **Headings**: Bold, 24-28sp
- **Titles**: SemiBold, 18-20sp
- **Body**: Regular, 14-16sp
- **Captions**: Regular, 12sp

### Components

- Material Design 3 buttons
- TextInputLayout with outlined style
- MaterialCardView with elevation
- BottomNavigationView
- AppBarLayout with MaterialToolbar
- NestedScrollView for scrolling

---

## 🚀 Technical Architecture

### Design Pattern

- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** for data access
- **Single Source of Truth** with Firestore

### Networking

- **Retrofit** for REST API calls
- **OkHttp** with logging interceptor
- **Gson** for JSON parsing
- **Coroutines** for async operations

### Dependencies

- Kotlin Coroutines + Flow
- AndroidX Lifecycle (ViewModel, LiveData)
- Firebase Auth + Firestore
- Google Play Services Location
- Retrofit + OkHttp
- Material Components
- ViewPager2 for authentication tabs

### Code Quality

- Kotlin idiomatic code
- Extension functions for utilities
- Sealed classes for state management
- Data classes for models
- Proper error handling with Result<T>

---

## 📊 Analytics & Metrics (Future)

### User Engagement

- Daily active users (DAU)
- Chat analyses per user
- Roasts generated per day
- Mission completion rate
- Average screen time reduction

### Subscription Metrics

- Conversion rate (free → paid)
- Churn rate
- Average revenue per user (ARPU)
- Retention (30/60/90 days)

### Feature Usage

- Most popular tones detected
- Most completed mission types
- Average roast severity
- Peak usage times

---

## 🔮 Future Enhancements

### Social Features

- Friend system
- Leaderboards (confidence/productivity scores)
- Challenge friends to missions
- Share roasts/analyses

### Advanced AI

- Voice analysis for calls
- Image analysis for profile pics
- Predictive text suggestions
- Personalized mission generation

### Gamification

- Achievement badges
- Level system (1-100)
- Unlockable rewards
- Seasonal events

### Integration

- Spotify (control music usage)
- Calendar (schedule focus time)
- Fitness apps (combine with challenges)
- WhatsApp/SMS (analyze real chats)

### Accessibility

- Voice commands
- TalkBack support
- High contrast mode
- Font size adjustments
- Multiple languages

---

## 📝 Summary

FALTU is a comprehensive lifestyle app that uniquely combines:

- **AI-powered dating assistance** for confidence building
- **Screen time awareness** with humorous motivation
- **Location-based challenges** for real-world engagement
- **Gamification** through points and rewards
- **Subscription tiers** for monetization

**Key Differentiators**:

- Humor-driven motivation (roasts!)
- Dual-module approach (dating + productivity)
- RunAnywhere SDK integration
- Beautiful neon gradient UI
- Firebase-powered backend

**Target Audience**:

- Young adults (18-35)
- Dating app users seeking confidence
- People wanting to reduce screen time
- Fitness-conscious users
- Gamification enthusiasts

---

**FALTU - Where AI meets humor, and productivity meets fun! 🎉😜🔥**
