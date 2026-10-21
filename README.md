# GIKOKO VIDEO RECAP — Android App (Cyber 3D)

![Build & Release](https://github.com/gikoko611/GIkokoRECAP/actions/workflows/android-build.yml/badge.svg)

trustmeaipro.org website UI ကို Cyber 3D Animation style native Android UI အဖြစ် ပြန်ဆောက်ထားတဲ့ app project ပါ။

## 🤖 GitHub Actions — Auto Build + Release

- main branch ကို push တိုင်း **Auto Build** ဖြစ်ပြီး APK ကို **GitHub Release** ထဲ တင်ပါမယ်
- Build status — https://github.com/gikoko611/GIkokoRECAP/actions
- APK download — https://github.com/gikoko611/GIkokoRECAP/releases
- Release ထဲက GIkokoVideoRecap-debug.apk ကို ဖုန်းထဲ တန်း download / install လို့ရပါတယ်
- လက်ဖြင့် build ချင်ရင် — Actions tab > Android Build & Release > Run workflow
- Build fail ရင် Actions run ရဲ့ log ထဲမှာ error ကို ဖွင့်ကြည့်ပါ

## 🔄 App Flow
1. SplashActivity — Cyber 3D animated splash — ၂.၆ စက်
2. HomeActivity — TRUSTME RECAP STUDIO UI အပြည့် (Home logo + workers, API key, modes, voices, settings, mixer, advanced, sync splitting, START DUBBING)
3. MainActivity — WebView studio (trustmeaipro.org) — တကယ့် processing များ

## 🖼 ကိုယ်ပိုင် JPEG ပုံတွေ ထည့်နည်း

### Home Screen Logo — download (4).jpeg (G + Play Video Recap)
repo ထဲမှာ build အောင်မင်းရန် **placeholder** vector (img_splash_logo.xml) ထည့်ထားပါတယ်။
တကယ့်ပုံနဲ့ အစားထိုးရန်:
1. app/src/main/res/drawable/ ထဲက **img_splash_logo.xml ကို ဖျက်ပါ**
2. download (4).jpeg ကို အဲ့ဒီ folder ထဲ ကူးထည့်ပြီး နာမည်ကို **img_splash_logo.jpeg** ဟုပေးပါ (စာလုံးအသေး၊ space မထည့်ပါနဲ့)
3. Commit + push လုပ်ပါ — Actions က auto build လုပ်ပြီး Release အသစ်ထုတ်ပေးမည်

### App Icon — download (1).jpeg (G.I ပန်းကနုတ် design)
1. Android Studio မှာ res folder ပေါ် right-click > New > Image Asset
2. Asset Type: Image, Path: download (1).jpeg ရွေးပါ
3. Finish — ic_launcher အားလုံး auto ထုတ်ပေးပါမယ်

## 🛠 Build လုပ်နည်း (လက်ဖြင့်)
1. git clone https://github.com/gikoko611/GIkokoRECAP.git
2. Android Studio နဲ့ project ဖွင့်ပါ (Gradle sync auto)
3. Run ▶ နဲ့ စမ်းသပ်ပါ
4. APK — Build > Build App Bundle(s)/APK(s) > Build APK(s)
5. Release APK — Build > Generate Signed APK (keystore ဖန်တီးပါ၊ password မမေ့ပါနဲ့)

## ⚙ Customize
- Website URL — MainActivity.kt ထဲက HOME_URL
- Admin Facebook link — HomeActivity.kt ထဲက FB_URL
- Splash ကြာချိန် — SplashActivity.kt မှာ 2600L
- အရောင်တွေ — res/values/colors.xml + HomeActivity.kt ထဲက cGold အစရှိသည်တို့

## 📱 Notes
- Home UI က website ရဲ့ UI replica — အလုပ်တကယ်လုပ်ရင် WebView studio ဆီ ချိတ်ဆက်ပြီးလုပ်ပါမယ်
- Cyber 3D background animation က Home screen မှာလည်း လှုပ်ရှားနေမည်
