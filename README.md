# GIKOKO VIDEO RECAP — Android App (Cyber 3D)

trustmeaipro.org website ကို Cyber 3D Animation style splash screen နဲ့ ဖွင့်ပေးမည့် Android WebView Wrapper app project ပါ။

## ✨ Features
- Cyber 3D animated splash — perspective grid floor + gold glow + floating particles
- GIkoko monogram app icon (adaptive icon, vector — G + Play button design)
- Video file upload / download အပြည့်အစုံ support
- အင်တာနက်ပြတ်ရင် မြန်မာ error page
- Swipe-to-refresh, back button navigation
- Facebook / WhatsApp / YouTube link တွေ app အပြင်မှာဖွင့်

## 📁 Project Structure

TrustMeRecapAI/
├── settings.gradle
├── build.gradle
├── gradle.properties
└── app/
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        ├── assets/error.html
        ├── java/com/gikoko/recap/
        │   ├── SplashActivity.kt      (Cyber 3D splash → WebView)
        │   ├── CyberSplashView.kt    (3D grid + particles animation)
        │   └── MainActivity.kt        (WebView + upload/download)
        └── res/
            ├── layout/activity_splash.xml
            ├── layout/activity_main.xml
            ├── values/ (strings, colors, themes)
            ├── drawable/ic_monogram.xml
            └── mipmap/ (app icons)

## 🛠 Build လုပ်နည်း
1. git clone https://github.com/gikoko611/GIkokoRECAP.git
2. Android Studio နဲ့ project ဖွင့်ပါ (Gradle sync အလိုအလျောက်လုပ်ပါမယ်)
3. Run ▶ နဲ့ ဖုန်းမှာ စမ်းသပ်ပါ
4. APK ထုတ်ရန် — Build > Build App Bundle(s)/APK(s) > Build APK(s)
   → app/build/outputs/apk/debug/app-debug.apk
5. Release APK — Build > Generate Signed APK (keystore ဖန်တီးပါ၊ password မမေ့ပါနဲ့)

## 🖼 ကိုယ်ပိုင် JPEG ပုံတွေ ထည့်နည်း

### App Icon — download (1).jpeg (G.I ပန်းကနုတ် design)
1. Android Studio မှာ res folder ပေါ် right-click > New > Image Asset
2. Asset Type: Image, Path: download (1).jpeg ရွေးပါ
3. Finish နှိပ်ရုံ — ic_launcher အားလုံး auto ထုတ်ပေးပါမယ် (vector monogram နေရာမှာ အစားထိုးသွားမယ်)

### Splash Home Screen Logo — download (4).jpeg (G + Play Video Recap)
1. download (4).jpeg ကို app/src/main/res/drawable/ ထဲကို img_splash_logo.png နာမည်နဲ့ ကူးထည့်ပါ
2. res/layout/activity_splash.xml မှာ @drawable/ic_monogram ကို @drawable/img_splash_logo အဖြစ် ပြောင်းပါ
3. (မပြောင်းပဲ vector monogram နဲ့ပဲ သုံးလို့လည်း ရပါတယ်)

## ⚙ Customize
- Website URL — MainActivity.kt ထဲက HOME_URL
- Splash ကြာချိန် — SplashActivity.kt မှာ 2600L (milliseconds)
- အရောင်တွေ — res/values/colors.xml
