package com.gikoko.recap

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.SwitchCompat
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

/**
 * Cyber 3D Home UI — TRUSTME RECAP AI PRO studio ရဲ့ UI ကို
 * native Android အဖြစ် Cyber 3D animation style နဲ့ ပြန်ဆောက်ထားသည်။
 * (ဒီပိုင်းက UI replica ဖြစ်ပြီး တကယ့် processing က WebView Studio မှာ ဆက်လုပ်မည်။)
 */
class HomeActivity : AppCompatActivity() {

    companion object {
        // Admin Facebook page URL — စစ်မှန်သော link ထည့်ပေးပါ
        const val FB_URL = "https://facebook.com"
    }

    // ---------- colors ----------
    private val cGold = Color.parseColor("#F5B301")
    private val cGoldLight = Color.parseColor("#FFD75E")
    private val cText = Color.parseColor("#E6EDF3")
    private val cTextDim = Color.parseColor("#9AA6B8")
    private val cGreen = Color.parseColor("#3DDC84")
    private val cRed = Color.parseColor("#FF5252")
    private val cDark = Color.parseColor("#05070D")

    private val MATCH = LinearLayout.LayoutParams.MATCH_PARENT
    private val WRAP = LinearLayout.LayoutParams.WRAP_CONTENT

    // ---------- data ----------
    private data class Mode(val emoji: String, val name: String, val desc: String, val vip: Boolean)
    private data class Voice(val emoji: String, val name: String, val style: String)
    private data class Cpu(val name: String, val status: String, val ok: Boolean)

    private val modes = listOf(
        Mode("📝", "Auto Translate", "English Recap video များအတွက်", false),
        Mode("🧠", "Pro Recap", "Video ကိုကြည့်ပြီး Recap အသစ်ရေးမည်", false),
        Mode("⚡", "Smart Highlight", "အရှည်ကြီးထဲမှ အဓိက Highlight ဖြတ်မည်", true),
        Mode("🎙️", "Auto Dubbing", "လူပြောမှ အသံလိုက်ပြောမည့် စနစ်သစ်", true),
        Mode("💬", "Subtitle Only", "မူရင်းအသံဖြင့်သာ စာတန်းထိုးပေးမည်", true),
        Mode("📄", "Transcript Only", "Video ကိုကြည့်ပြီး စာသားသီးသန့်ထုတ်မည်", true),
        Mode("🔊", "TTS Only", "စာသားရိုက်ထည့်ပြီး အသံသီးသန့်ထုတ်မည်", true),
        Mode("🎬", "TTS to Video", "အသံသွင်းပြီးသားကို Video ပေါ်ထပ်တင်မည်", true)
    )

    private val maleVoices = listOf(
        Voice("👦🏻", "Puck", "Upbeat"),
        Voice("🧔🏻‍♂️", "Charon", "Narrator"),
        Voice("👨🏼", "Fenrir", "Dramatic"),
        Voice("👨🏽", "Orus", "Firm"),
        Voice("👴🏻", "Enceladus", "Breathy"),
        Voice("🧑🏻", "Iapetus", "Clear"),
        Voice("👨🏻", "Umbriel", "Casual"),
        Voice("🧔🏽", "Algieba", "Baritone"),
        Voice("🦹🏻‍♂️", "Algenib", "Gritty"),
        Voice("👨🏻‍💼", "Rasalgethi", "Authority"),
        Voice("🧑🏽", "Alnilam", "Deep"),
        Voice("👨🏼‍🦱", "Schedar", "Measured"),
        Voice("🧓🏻", "Gacrux", "Storyteller"),
        Voice("🙋🏻‍♂️", "Achird", "Friendly"),
        Voice("🧑🏼", "Zubenelgenubi", "Relaxed"),
        Voice("🕺🏻", "Sadachbia", "Lively"),
        Voice("🧙🏻‍♂️", "Sadaltager", "Wise")
    )

    private val femaleVoices = listOf(
        Voice("👩🏼", "Zephyr", "Bright"),
        Voice("👧🏻", "Kore", "Firm"),
        Voice("👱🏻‍♀️", "Leda", "Youthful"),
        Voice("👩🏽", "Aoede", "Melodic"),
        Voice("👩🏻‍🦱", "Callirrhoe", "Relaxed"),
        Voice("👩🏻", "Autonoe", "Crisp"),
        Voice("👩🏼‍🦰", "Despina", "Smooth"),
        Voice("👧🏼", "Erinome", "Clear"),
        Voice("💃🏻", "Laomedeia", "Upbeat"),
        Voice("🧕🏻", "Achernar", "Gentle"),
        Voice("💁🏻‍♀️", "Pulcherrima", "Dynamic"),
        Voice("🧘🏻‍♀️", "Vindemiatrix", "Calm"),
        Voice("👩🏻‍💼", "Sulafat", "Warm")
    )

    private val cpus = listOf(
        Cpu("CPU 1", "Ready", true),
        Cpu("CPU 2", "Ready", true),
        Cpu("CPU 3", "Maint", false)
    )

    private val tabs = listOf("Myanmar", "English", "Thai", "Chinese")
    private val aspects = listOf("16:9", "9:16", "3:4", "1:1")

    // ---------- selection state ----------
    private class Sel(val view: View, val bgOff: Int)
    private val selections = HashMap<String, Sel>()

    private fun select(v: View, key: String, bgOff: Int, bgOn: Int, onTextColor: Int? = null, offTextColor: Int? = null) {
        val old = selections[key]
        if (old != null) {
            old.view.setBackgroundResource(old.bgOff)
            if (offTextColor != null && old.view is TextView) old.view.setTextColor(offTextColor)
        }
        v.setBackgroundResource(bgOn)
        if (onTextColor != null && v is TextView) v.setTextColor(onTextColor)
        selections[key] = Sel(v, bgOff)
    }

    // ============================================================
    // helpers
    // ============================================================
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).roundToInt()

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun openStudio() {
        toast("Studio ဆီ ချိတ်ဆက်နေပါသည်...")
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun card(): LinearLayout {
        val p = dp(14)
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_card)
            setPadding(p, p, p, p)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { bottomMargin = dp(14) }
        }
    }

    private fun addTitle(parent: LinearLayout, text: String) {
        parent.addView(TextView(this).apply {
            this.text = text
            setTextColor(cGold)
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setShadowLayer(12f, 0f, 0f, Color.parseColor("#66F5B301"))
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { bottomMargin = dp(8) }
        })
    }

    private fun addDesc(parent: LinearLayout, text: String, color: Int = cTextDim) {
        parent.addView(TextView(this).apply {
            this.text = text
            setTextColor(color)
            textSize = 12f
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { bottomMargin = dp(6) }
        })
    }

    private fun switchRow(parent: LinearLayout, text: String, checked: Boolean) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8); bottomMargin = dp(6) }
        }
        row.addView(TextView(this).apply {
            this.text = text
            setTextColor(cText)
            textSize = 13f
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        row.addView(SwitchCompat(this).apply {
            isChecked = checked
            thumbTintList = ColorStateList.valueOf(cGold)
            trackTintList = ColorStateList.valueOf(Color.parseColor("#55F5B301"))
        })
        parent.addView(row)
    }

    private fun seekRow(parent: LinearLayout, label: String, progress: Int, maxValue: Int = 100): SeekBar {
        parent.addView(TextView(this).apply {
            this.text = label
            setTextColor(cText)
            textSize = 12f
        })
        val seek = SeekBar(this).apply {
            max = maxValue
            this.progress = progress
            progressTintList = ColorStateList.valueOf(cGold)
            thumbTintList = ColorStateList.valueOf(cGoldLight)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP)
        }
        parent.addView(seek)
        return seek
    }

    private fun chipGrid(parent: LinearLayout, columns: Int): GridLayout {
        val grid = GridLayout(this).apply {
            columnCount = columns
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP)
        }
        parent.addView(grid)
        return grid
    }

    private fun addChip(grid: GridLayout, text: String, key: String, selected: Boolean = false) {
        val tv = TextView(this).apply {
            this.text = text
            textSize = 12f
            gravity = Gravity.CENTER
            setPadding(dp(6), dp(9), dp(6), dp(9))
            setTextColor(cText)
            setBackgroundResource(R.drawable.bg_chip)
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                marginStart = dp(3)
                marginEnd = dp(3)
                topMargin = dp(6)
                bottomMargin = dp(6)
            }
        }
        tv.setOnClickListener {
            select(tv, key, R.drawable.bg_chip, R.drawable.bg_chip_selected, cDark, cText)
        }
        grid.addView(tv)
        if (selected) select(tv, key, R.drawable.bg_chip, R.drawable.bg_chip_selected, cDark, cText)
    }

    private fun addVoice(grid: GridLayout, v: Voice, key: String) {
        val voiceCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(6), dp(10), dp(6), dp(10))
            setBackgroundResource(R.drawable.bg_mode_card)
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                marginStart = dp(3)
                marginEnd = dp(3)
                topMargin = dp(6)
                bottomMargin = dp(6)
            }
        }
        voiceCard.addView(TextView(this).apply { text = v.emoji; textSize = 22f; gravity = Gravity.CENTER })
        voiceCard.addView(TextView(this).apply {
            text = v.name; setTextColor(cText); textSize = 12f
            typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
        })
        voiceCard.addView(TextView(this).apply {
            text = v.style; setTextColor(cTextDim); textSize = 10f; gravity = Gravity.CENTER
        })
        voiceCard.setOnClickListener {
            select(voiceCard, key, R.drawable.bg_mode_card, R.drawable.bg_mode_card_selected)
        }
        grid.addView(voiceCard)
    }

    private fun addMode(grid: GridLayout, m: Mode) {
        val modeCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(10), dp(10), dp(10), dp(10))
            setBackgroundResource(R.drawable.bg_mode_card)
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                marginStart = dp(3)
                marginEnd = dp(3)
                topMargin = dp(6)
                bottomMargin = dp(6)
            }
        }
        val topRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        topRow.addView(TextView(this).apply { text = m.emoji; textSize = 20f })
        topRow.addView(TextView(this).apply {
            text = "  " + m.name
            setTextColor(cText); textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        if (m.vip) {
            topRow.addView(TextView(this).apply {
                text = "👑 VVIP"
                setTextColor(cDark); textSize = 9f
                setBackgroundResource(R.drawable.bg_vvip)
                setPadding(dp(5), dp(2), dp(5), dp(2))
            })
        }
        modeCard.addView(topRow)
        modeCard.addView(TextView(this).apply {
            text = m.desc
            setTextColor(cTextDim); textSize = 10f
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) }
        })
        modeCard.setOnClickListener {
            select(modeCard, "mode", R.drawable.bg_mode_card, R.drawable.bg_mode_card_selected)
        }
        grid.addView(modeCard)
    }

    private fun cpuCard(c: Cpu): LinearLayout {
        val cpuView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(12), dp(8), dp(12))
            setBackgroundResource(R.drawable.bg_input)
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f).apply {
                marginStart = dp(4)
                marginEnd = dp(4)
            }
        }
        cpuView.addView(TextView(this).apply {
            text = c.name
            setTextColor(cGold); textSize = 12f
            typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
        })
        cpuView.addView(TextView(this).apply {
            text = (if (c.ok) "🟢 " else "🔴 ") + c.status
            setTextColor(if (c.ok) cGreen else cRed)
            textSize = 11f; gravity = Gravity.CENTER
        })
        return cpuView
    }

    private fun goldButton(text: String): Button {
        return Button(this).apply {
            this.text = text
            setTextColor(cDark)
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setBackgroundResource(R.drawable.bg_gold_btn)
            stateListAnimator = null
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        }
    }

    // ============================================================
    // onCreate
    // ============================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val root = findViewById<LinearLayout>(R.id.rootContainer)

        buildHeader(root)
        buildWorkers(root)
        buildApiKey(root)
        buildModes(root)
        buildVoices(root)
        buildSettings(root)
        buildUpload(root)
        buildMixer(root)
        buildAdvanced(root)
        buildSync(root)
        buildStart(root)
        buildContact(root)
    }

    // ---------- 1. header ----------
    private fun buildHeader(root: LinearLayout) {
        root.addView(ImageView(this).apply {
            setImageResource(R.drawable.ic_monogram)
            layoutParams = LinearLayout.LayoutParams(dp(120), dp(120)).apply { topMargin = dp(6) }
        })
        root.addView(TextView(this).apply {
            text = "TRUSTME RECAP AI PRO"
            setTextColor(cGold)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            letterSpacing = 0.1f
            gravity = Gravity.CENTER
            setShadowLayer(16f, 0f, 0f, Color.parseColor("#80F5B301"))
        })
        root.addView(TextView(this).apply {
            text = "AI Studio & Cloud System • v10.9.1"
            setTextColor(cTextDim); textSize = 11f; gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "အသုံးပြုသူများ အဆင်ပြေချောမွေ့စွာ အသုံးပြုနိုင်ရန် TRUSTME RECAP AI PRO TEAM မှ အစွမ်းကုန် ကြိုးစားဖန်တီးပေးထားပါသည်။"
            setTextColor(cGreen); textSize = 11f; gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8); bottomMargin = dp(4) }
        })
        // user chips
        val hs = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6); bottomMargin = dp(10) }
        }
        val chipsRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("👤 Gi Koko • FREE", "🪙 100 Pts", "🎬 +10 Ads", "👑 VVIP ထွက်မည်").forEach { label ->
            chipsRow.addView(TextView(this).apply {
                text = label
                setTextColor(cGoldLight); textSize = 12f
                setBackgroundResource(R.drawable.bg_chip)
                setPadding(dp(12), dp(8), dp(12), dp(8))
                layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { marginEnd = dp(8) }
            })
        }
        hs.addView(chipsRow)
        root.addView(hs)
    }

    // ---------- 2. render workers ----------
    private fun buildWorkers(root: LinearLayout) {
        val c = card()
        addTitle(c, "⚡ RENDER WORKERS (MULTI-TASKING)")
        addDesc(c, "Parallel Mode — ဆာဗာများ ခွဲ၍ ဗီဒီယို ၂/၃ ပုဒ် တပြိုင်နက်တည်း Render ထုတ်နိုင်ပါသည်")
        val cpuRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(4) }
        }
        cpus.forEach { cpuRow.addView(cpuCard(it)) }
        c.addView(cpuRow)
        root.addView(c)
    }

    // ---------- 3. API key ----------
    private fun buildApiKey(root: LinearLayout) {
        val c = card()
        addTitle(c, "🔑 ကိုယ်ပိုင် Free API Key (OPTIONAL)")
        addDesc(c, "🌐 Server Key အသင့်ရှိသည် — Key မထည့်လည်း ရသည်", cGreen)
        addDesc(c, "📧 Per-Gmail Binding: gikoko611@gmail.com")
        val keyRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        }
        keyRow.addView(EditText(this).apply {
            hint = "AIza... သို့မဟုတ် AQ... Key ထည့်ပါ"
            setTextColor(cText); setHintTextColor(cTextDim); textSize = 13f
            setBackgroundResource(R.drawable.bg_input)
            setPadding(dp(12), dp(10), dp(12), dp(10))
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        keyRow.addView(Button(this).apply {
            text = "📋 Paste"
            setTextColor(cGold); textSize = 12f
            setBackgroundResource(R.drawable.bg_chip)
            stateListAnimator = null
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { marginStart = dp(8) }
        })
        c.addView(keyRow)
        c.addView(goldButton("💾 Key သိမ်းဆည်းပြီး စတင်သုံးမည်"))
        root.addView(c)
    }

    // ---------- 4. AI processing modes ----------
    private fun buildModes(root: LinearLayout) {
        val c = card()
        addTitle(c, "🧠 AI PROCESSING MODE")
        val grid = chipGrid(c, 2)
        modes.forEach { addMode(grid, it) }
        root.addView(c)
    }

    // ---------- 5. voices ----------
    private fun buildVoices(root: LinearLayout) {
        val c = card()
        addTitle(c, "🎙️ SELECT DUBBING VOICES")

        val tabGrid = chipGrid(c, 4)
        tabs.forEachIndexed { i, t -> addChip(tabGrid, t, "lang", i == 0) }

        // pitch
        addDesc(c, "🎚️ Voice Pitch — အသံ ကြီး/သေး (Live Preview)", cText)
        val pitch = seekRow(c, "Pitch", 50, 100)
        val lowHigh = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        lowHigh.addView(TextView(this).apply {
            text = "Low (-50 ယောက်ျားသံဩ)"
            setTextColor(cTextDim); textSize = 10f
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        lowHigh.addView(TextView(this).apply {
            text = "High (+50 မိန်းကလေးသံစူး)"
            setTextColor(cTextDim); textSize = 10f; gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
        })
        c.addView(lowHigh)
        c.addView(Button(this).apply {
            text = "↺ Reset"
            setTextColor(cGold); textSize = 12f
            setBackgroundResource(R.drawable.bg_chip)
            stateListAnimator = null
            setOnClickListener { pitch.progress = 50 }
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(4) }
        })

        // male voices
        addDesc(c, "👨🏻 ယောက်ျားလေး ဇာတ်ကောင်အသံ [Male Role - 17 Male]", cText)
        val maleGrid = chipGrid(c, 3)
        maleVoices.forEach { addVoice(maleGrid, it, "maleVoice") }

        // female voices
        addDesc(c, "👩🏻 မိန်းကလေး ဇာတ်ကောင်အသံ [Female Role - 13 Female]", cText)
        val femaleGrid = chipGrid(c, 3)
        femaleVoices.forEach { addVoice(femaleGrid, it, "femaleVoice") }

        root.addView(c)

        // clone library
        val clone = card()
        addTitle(clone, "🧬 Custom Voice Clone Library (5 Slots)")
        val badgeRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        badgeRow.addView(TextView(this).apply {
            text = "⚠️ MAINTENANCE"
            setTextColor(cRed); textSize = 10f
            setBackgroundResource(R.drawable.bg_danger)
            setPadding(dp(8), dp(4), dp(8), dp(4))
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply { marginEnd = dp(8) }
        })
        badgeRow.addView(TextView(this).apply {
            text = "VoxCPM2 Powered"
            setTextColor(cTextDim); textSize = 10f; gravity = Gravity.CENTER_VERTICAL
        })
        clone.addView(badgeRow)
        addDesc(clone, "မိမိကိုယ်ပိုင်အသံ (သို့) ကြိုက်နှစ်သက်ရာအသံ ၅ ခုအထိ သိမ်းဆည်းနိုင်ပါသည်")
        val slotGrid = chipGrid(clone, 2)
        for (i in 1..5) {
            slotGrid.addView(TextView(this).apply {
                text = "+ Slot " + i + " အသံထည့်ရန်"
                setTextColor(cTextDim); textSize = 12f
                gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_slot)
                setPadding(dp(8), dp(12), dp(8), dp(12))
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    marginStart = dp(3)
                    marginEnd = dp(3)
                    topMargin = dp(6)
                    bottomMargin = dp(6)
                }
            })
        }
        root.addView(clone)
    }

    // ---------- 6. settings ----------
    private fun buildSettings(root: LinearLayout) {
        val c = card()
        addTitle(c, "⚙️ SETTINGS")
        addDesc(c, "🧠 AI Custom Prompt (Optional)", cText)
        c.addView(EditText(this).apply {
            hint = "ဥပမာ - ဇာတ်ကောင်အမည်များကို Mg Mg နှင့် Hla Hla ဟု သုံးပေးပါ။"
            setTextColor(cText); setHintTextColor(cTextDim); textSize = 13f
            setBackgroundResource(R.drawable.bg_input)
            setPadding(dp(12), dp(10), dp(12), dp(10))
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 2
            gravity = Gravity.TOP
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) }
        })
        switchRow(c, "🎭 Advanced Recap Style", false)
        addDesc(c, "📱 Output Aspect Ratio", cText)
        val aspectGrid = chipGrid(c, 4)
        aspects.forEachIndexed { i, a -> addChip(aspectGrid, a, "aspect", i == 0) }
        root.addView(c)
    }

    // ---------- 7. upload ----------
    private fun buildUpload(root: LinearLayout) {
        val c = card()
        addTitle(c, "☁️ VIDEO SOURCE")
        addDesc(c, "Video file upload (သို့) URL paste လုပ်ပါ — 100 MB Limit ကို ကျော်လွန်ခွင့် ပြုထားပါသည်")
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        }
        row.addView(Button(this).apply {
            text = "☁️ Upload File"
            setTextColor(Color.WHITE); textSize = 14f
            setBackgroundResource(R.drawable.bg_start_btn)
            stateListAnimator = null
            setOnClickListener { openStudio() }
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginEnd = dp(6) }
        })
        row.addView(Button(this).apply {
            text = "🔗 Paste URL"
            setTextColor(Color.WHITE); textSize = 14f
            setBackgroundResource(R.drawable.bg_start_btn)
            stateListAnimator = null
            setOnClickListener { openStudio() }
            layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(6) }
        })
        c.addView(row)
        root.addView(c)
    }

    // ---------- 8. audio mixer ----------
    private fun buildMixer(root: LinearLayout) {
        val c = card()
        addTitle(c, "🎛️ AUDIO MIXER")
        seekRow(c, "Voice / Original Audio Volume", 100)
        seekRow(c, "Background Music (BGM)", 5)
        switchRow(c, "©️ Watermark & Logo (ရေစာထည့်ရန်)", true)
        root.addView(c)
    }

    // ---------- 9. advanced video settings ----------
    private fun buildAdvanced(root: LinearLayout) {
        val c = card()
        addTitle(c, "🛠️ ADVANCED VIDEO SETTINGS")
        addDesc(c, "Video Quality (Resolution)", cText)
        val spinner = Spinner(this)
        val opts = listOf("720p (Standard HD)", "1080p (Full HD)", "480p (Standard)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setBackgroundResource(R.drawable.bg_input)
        spinner.layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) }
        c.addView(spinner)

        switchRow(c, "🔲 Auto Blur Subtitles", false)
        addDesc(c, "🔒 Anti-Copyright System (မူပိုင်ခွင့်ရှောင်ရန်)", cText)
        switchRow(c, "Mirror (ဘယ်/ညာလှန်)", false)
        switchRow(c, "Auto Zoom", false)
        switchRow(c, "Color Filter", false)
        switchRow(c, "Audio Pitch Alter", false)
        c.addView(Button(this).apply {
            text = "📸 Freeze + Zoom Bypass (Advanced)"
            setTextColor(Color.WHITE); textSize = 12f
            setBackgroundResource(R.drawable.bg_danger)
            stateListAnimator = null
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        })
        root.addView(c)
    }

    // ---------- 10. smart sync splitting + script review ----------
    private fun buildSync(root: LinearLayout) {
        val c = card()
        addTitle(c, "✂️ SMART SYNC SPLITTING (အပိုင်းခွဲစနစ်)")
        addDesc(c, "ဗီဒီယိုအရှည်များအတွက် ရုပ်နှင့်သံ အတိအကျကိုက်ညီရန် အပိုင်းခွဲ၍ Render လုပ်ပါ။")
        switchRow(c, "Auto Subtitles", true)
        val partsGrid = chipGrid(c, 4)
        for (i in 1..20) addChip(partsGrid, i.toString(), "parts", i == 1)
        switchRow(c, "📝 Interactive Script Review", false)
        c.addView(Button(this).apply {
            text = "📜 CUSTOM SCRIPT"
            setTextColor(cGold); textSize = 13f
            setBackgroundResource(R.drawable.bg_chip)
            stateListAnimator = null
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        })
        root.addView(c)
    }

    // ---------- 11. start dubbing ----------
    private fun buildStart(root: LinearLayout) {
        val startBtn = Button(this).apply {
            text = "🎬 START DUBBING (20 PTS)"
            setTextColor(Color.WHITE)
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            letterSpacing = 0.08f
            setBackgroundResource(R.drawable.bg_start_btn)
            stateListAnimator = null
            setOnClickListener { openStudio() }
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                topMargin = dp(10)
                bottomMargin = dp(6)
            }
        }
        root.addView(startBtn)

        // cyber pulse animation
        val pulseX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.05f)
        val pulseY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.05f)
        ObjectAnimator.ofPropertyValuesHolder(startBtn, pulseX, pulseY).apply {
            duration = 800L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }
    }

    // ---------- 12. contact ----------
    private fun buildContact(root: LinearLayout) {
        root.addView(TextView(this).apply {
            text = "အခက်အခဲ တစ်စုံတစ်ရာ ရှိပါက (သို့) အကောင့် သက်တမ်းတိုးလိုပါက Admin သို့ ဆက်သွယ်နိုင်ပါသည်။"
            setTextColor(cTextDim); textSize = 11f; gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(10) }
        })
        root.addView(Button(this).apply {
            text = "📘 Contact Admin (Facebook)"
            setTextColor(Color.WHITE); textSize = 13f
            setBackgroundResource(R.drawable.bg_fb)
            stateListAnimator = null
            setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(FB_URL)))
                } catch (e: Exception) {
                    toast("ဖွင့်စရာ app မတွေ့ပါ")
                }
            }
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(8) }
        })
        root.addView(TextView(this).apply {
            text = "© GIkoko Video Recap • TRUSTME RECAP AI PRO TEAM"
            setTextColor(cTextDim); textSize = 10f; gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(14) }
        })
    }
}
