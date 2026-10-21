# GIKOKO VIDEO RECAP — Android App (Cyber 3D)

trustmeaipro.org website UI ကို Cyber 3D Animation style native Android UI အဖြစ် ပြန်ဆောက်ထားတဲ့ app project ပါ။

## 🔄 App Flow
1. SplashActivity — Cyber 3D animated splash (perspective grid + gold particles + glow) — ၂.၆ စက်
2. HomeActivity — TRUSTME RECAP STUDIO UI အပြည့် (native Cyber 3D style)
   - Render Workers (CPU 1/2/3 status + Parallel Mode)
   - Free API Key section
   - AI Processing Mode (mode ၈ ခု + VVIP badges)
   - Select Dubbing Voices (Myanmar/English/Thai/Chinese tabs + Pitch slider + ယောက်ျားသံ ၁၇ + မိန်းကလေးသံ ၁၃)
   - Custom Voice Clone Library (5 slots)
   - Settings (Custom Prompt + Aspect Ratio)
   - Upload File / Paste URL
   - Audio Mixer (Voice 100% / BGM 5%)
   - Advanced Video Settings (Quality + Anti-Copyright toggles)
   - Smart Sync Splitting (Parts 1-20 + Auto Subtitles + Script Review)
   - START DUBBING (pulsing animation)
   - Contact Admin (Facebook)
3. MainActivity — WebView studio (trustmeaipro.org) — တကယ့် processing များ ဒီမှာ ဆက်လုပ်မည်

## 🛠 Build လုပ်နည်း
1. git clone https://github.com/gikoko611/GIkokoRECAP.git
2. Android Studio နဲ့ project ဖွင့်ပါ (Gradle sync အလိုအလျောက်လုပ်ပါမယ်)
3. Run ▶ နဲ့ ဖုန်းမှာ စမ်းသပ်ပါ
4. APK ထုတ်ရန် — Build > Build App Bundle(s)/APK(s) > Build APK(s)
5. Release APK — Build > Generate Signed APK (keystore ဖန်တီးပါ၊ password မမေ့ပါနဲ့)

## 🖼 ကိုယ်ပိုင် JPEG ပုံတွေ ထည့်နည်း

### App Icon — download (1).jpeg (G.I ပန်းကနုတ် design)
1. Android Studio မှာ res folder ပေါ် right-click > New > Image Asset
2. Asset Type: Image, Path: download (1).jpeg ရွေးပါ
3. Finish နှိပ်ရုံ — ic_launcher အားလုံး auto ထုတ်ပေးပါမယ်

### Splash / Home Logo — download (4).jpeg (G + Play Video Recap)
1. download (4).jpeg ကို app/src/main/res/drawable/ ထဲကို img_splash_logo.png နာမည်နဲ့ ကူးထည့်ပါ
2. res/layout/activity_splash.xml မှာ @drawable/ic_monogram ကို @drawable/img_splash_logo အဖြစ် ပြောင်းပါ

## ⚙ Customize
- Website URL — MainActivity.kt ထဲက HOME_URL
- Admin Facebook link — HomeActivity.kt ထဲက FB_URL (ကိုယ်ပိုင် page link ထည့်ပါ)
- Splash ကြာချိန် — SplashActivity.kt မှာ 2600L (milliseconds)
- အရောင်တွေ — res/values/colors.xml + HomeActivity.kt ထဲက cGold အစရှိသည်တို့

## 📱 Notes
- Home UI က website ရဲ့ UI replica ဖြစ်ပြီး၊ အလုပ်တကယ်လုပ်ချင်ရင် (upload, render, dubbing) WebView studio ဆီ ဆက်သွယ်ပြီး လုပ်ပါမယ်
- Cyber 3D background animation က Home screen မှာလည်း ဆက်လက် လှုပ်ရှားနေပါမယ်
