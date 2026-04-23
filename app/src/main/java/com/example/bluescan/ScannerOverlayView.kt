package com.example.bluescan

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class ScannerOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val boxPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val linePaint = Paint().apply {
        color = Color.parseColor("#3366cc")
        strokeWidth = 5f
    }

    private val boxRect = RectF()
    private var scanLineY = 0f
    private var animator: ValueAnimator? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Smaller reticle (e.g., 60% width)
        val boxWidth = w * 0.6f
        val boxHeight = boxWidth // Square
        val left = (w - boxWidth) / 2
        val top = (h - boxHeight) / 2
        boxRect.set(left, top, left + boxWidth, top + boxHeight)

        startAnimation()
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(boxRect.top, boxRect.bottom).apply {
            duration = 2000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                scanLineY = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Reticle
        canvas.drawRect(boxRect, boxPaint)
        // Scan Line
        canvas.drawLine(boxRect.left, scanLineY, boxRect.right, scanLineY, linePaint)
    }
}
