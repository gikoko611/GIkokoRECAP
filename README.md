# GIKOKO VIDEO RECAP — Android App (Cyber 3D)

trustmeaipro.org website UI ကို Cyber 3D Animation style native Android UI အဖြစ် ပြန်ဆောက်ထားတဲ့ app project ပါ။

## 🔄 App Flow
1. SplashActivity — Cyber 3D animated splash (perspective grid + gold particles + glow) — ၂.၆ စက်
2. HomeActivity — TRUSTME RECAP STUDIO UI အပြည့် (native Cyber 3D style)
   - Home screen logo (download (4).jpeg — G + Play Video Recap)
   - Render Workers (CPU 1/2/3 status + Parallel Mode)
   - Free API Key section
   - AI Processing Mode (mode ၈ ခု + VVIP badges)
   - Select Dubbing Voices (tabs + Pitch slider + ယောက်ျားသံ ၁၇ + မိန်းကလေးသံ ၁၃)
   - Custom Voice Clone Library (5 slots)
   - Settings (Custom Prompt + Aspect Ratio)
   - Upload File / Paste URL
   - Audio Mixer (Voice 100% / BGM 5%)
   - Advanced Video Settings (Quality + Anti-Copyright toggles)
   - Smart Sync Splitting (Parts 1-20 + Auto Subtitles + Script Review)
   - START DUBBING (pulsing animation)
   - Contact Admin (Facebook)
3. MainActivity — WebView studio (trustmeaipro.org) — တကယ့် processing များ ဒီမှာ ဆက်လုပ်မည်

## ⚠️ မဖြစ်မနေ လုပ်ရမည် — Home Screen Logo ထည့်ခြင်း

Home screen အပေါ်ဆုံးမှာ ပေးထားတဲ့ logo ပုံ (download (4).jpeg — G + Play "Video Recap" / POWERED BY I KO KO) ပေါ်ရန်အတွက် ဒီအဆင့်ကို မဖြစ်မနေ လုပ်ပေးပါ (မထည့်ဘဲ build ရင် error တက်ပါမည်):

1. download (4).jpeg ကို computer ထဲ ရယူပါ
2. Android Studio မှာ app/src/main/res/drawable/ folder ပေါ် right-click > Show in Explorer (Mac: Reveal in Finder)
3. ပုံကို အဲ့ဒီ folder ထဲကို ကူးထည့်ပြီး နာမည်ကို img_splash_logo ဟု ပေးပါ
   (.jpeg အတိုင်းဆိုရင် img_splash_logo.jpeg — စာလုံးအသေး၊ space မထည့်ပါနဲ့)
4. Build လုပ်ပါ — Home screen အပေါ်ဆုံးမှာ ပုံ အလိုအလျောက် ပေါ်လာမည်
   (Title စာသားတွေကတော့ ဖျက်ထားပြီးသား — logo ပုံပဲ ပေါ်မည်)

### App Icon — download (1).jpeg (G.I ပန်းကနုတ် design)
1. Android Studio မှာ res folder ပေါ် right-click > New > Image Asset
2. Asset Type: Image, Path: download (1).jpeg ရွေးပါ
3. Finish နှိပ်ရုံ — ic_launcher အားလုံး auto ထုတ်ပေးပါမယ်

## 🛠 Build လုပ်နည်း
1. git clone https://github.com/gikoko611/GIkokoRECAP.git
2. Android Studio နဲ့ project ဖွင့်ပါ (Gradle sync အလိုအလျောက်လုပ်ပါမယ်)
3. အထက်ပါ logo ထည့်ခြင်းအဆင့်ကို လုပ်ပါ
4. Run ▶ နဲ့ ဖုန်းမှာ စမ်းသပ်ပါ
5. APK ထုတ်ရန် — Build > Build App Bundle(s)/APK(s) > Build APK(s)
6. Release APK — Build > Generate Signed APK (keystore ဖန်တီးပါ၊ password မမေ့ပါနဲ့)

## ⚙ Customize
- Website URL — MainActivity.kt ထဲက HOME_URL
- Admin Facebook link — HomeActivity.kt ထဲက FB_URL (ကိုယ်ပိုင် page link ထည့်ပါ)
- Splash ကြာချိန် — SplashActivity.kt မှာ 2600L (milliseconds)
- အရောင်တွေ — res/values/colors.xml + HomeActivity.kt ထဲက cGold အစရှိသည်တို့

## 📱 Notes
- Home UI က website ရဲ့ UI replica ဖြစ်ပြီး၊ အလုပ်တကယ်လုပ်ချင်ရင် (upload, render, dubbing) WebView studio ဆီ ဆက်သွယ်ပြီး လုပ်ပါမယ်
- Cyber 3D background animation က Home screen မှာလည်း ဆက်လက် လှုပ်ရှားနေပါမယ်
