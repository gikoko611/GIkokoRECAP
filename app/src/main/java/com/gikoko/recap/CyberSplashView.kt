package com.gikoko.recap

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin
import kotlin.random.Random

/**
 * Cyber 3D Animation background —
 * perspective grid floor, gold horizon glow, floating gold particles.
 */
class CyberSplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var animator: ValueAnimator? = null
    private var t = 0f

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#05070D")
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isDither = true
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#38F5B301")
        strokeWidth = 1.5f
        style = Paint.Style.STROKE
    }

    private val horizonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#80F5B301")
        strokeWidth = 2f
        style = Paint.Style.STROKE
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F5B301")
        maskFilter = BlurMaskFilter(3f, BlurMaskFilter.Blur.NORMAL)
    }

    private val gridPath = Path()

    private class Particle(
        val x: Float,
        val y0: Float,
        val speed: Float,
        val r: Float,
        val phase: Float
    )

    private val particles = List(34) {
        Particle(
            x = Random.nextFloat(),
            y0 = Random.nextFloat(),
            speed = 0.02f + Random.nextFloat() * 0.05f,
            r = 1.5f + Random.nextFloat() * 2.5f,
            phase = Random.nextFloat() * 6.283f
        )
    }

    init {
        // BlurMaskFilter အတွက် software layer လိုအပ်သည်
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        glowPaint.shader = RadialGradient(
            w / 2f, h * 0.44f, w * 0.6f,
            intArrayOf(
                Color.parseColor("#26F5B301"),
                Color.parseColor("#0DF5B301"),
                Color.TRANSPARENT
            ),
            floatArrayOf(0f, 0.45f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // background
        canvas.drawRect(0f, 0f, w, h, bgPaint)

        // center gold glow
        canvas.drawCircle(w / 2f, h * 0.44f, w * 0.6f, glowPaint)

        // ---- cyber 3D perspective floor ----
        val horizonY = h * 0.60f
        val vanishX = w / 2f
        gridPath.reset()

        // vertical lines — vanishing point မှ အောက်သို့
        val spread = w * 1.7f
        val vCount = 12
        for (i in -vCount..vCount) {
            val xBottom = vanishX + (i.toFloat() / vCount) * spread
            gridPath.moveTo(vanishX, horizonY)
            gridPath.lineTo(xBottom, h)
        }

        // horizontal lines — viewer ဆီ တဖြည်းဖြည်း ရွေ့လျား
        val hCount = 14
        val scroll = (t * 0.35f) % 1f
        for (i in 0 until hCount) {
            var f = (i.toFloat() + scroll) / hCount
            if (f <= 0f) f += 1f
            if (f > 1f) continue
            val y = horizonY + (h - horizonY) * (f * f)
            gridPath.moveTo(0f, y)
            gridPath.lineTo(w, y)
        }
        canvas.drawPath(gridPath, gridPaint)

        // glowing horizon line
        canvas.drawLine(0f, horizonY, w, horizonY, horizonPaint)

        // ---- floating gold particles ----
        for (p in particles) {
            var yy = p.y0 - t * p.speed
            yy = ((yy % 1f) + 1f) % 1f
            val alpha = 0.30f + 0.30f * sin(t * 2.5f + p.phase)
            particlePaint.alpha = (alpha * 255).toInt()
            canvas.drawCircle(p.x * w, yy * h, p.r, particlePaint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                t += 0.016f
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator = null
        super.onDetachedFromWindow()
    }
}
