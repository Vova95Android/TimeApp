package com.example.timeapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class TimeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var heightClock = 0
    private var widthClock = 0
    private var paddingClock = 0
    private var fontSizeClock = 0
    private var handTruncationClock = 0
    private var hourHandTruncationClock = 0
    private var radiusClock = 0
    private var paint = Paint()
    private var isInit = false
    private var numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private var rect = Rect()
    private var hour = 0
    private var min = 0
    private var sec = 0
    private var xCenter=0f
    private var yCenter=0f


    fun initClock() {
        widthClock = width
        heightClock = height
        xCenter=(widthClock/2).toFloat()
        yCenter=(heightClock/2).toFloat()
        paddingClock = 50
        fontSizeClock = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics
        ).toInt()
        val min = Math.min(heightClock, widthClock)
        radiusClock = min / 2 - paddingClock-5
        handTruncationClock = min / 20
        hourHandTruncationClock = min / 7
        isInit = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInit) initClock()
        canvas?.drawColor(Color.WHITE)
        drawCircel(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawSecond(canvas)
        drawHands(canvas)

        //postInvalidateDelayed(500)
        //invalidate()
    }

    private fun drawSecond(canvas: Canvas?) {
        for (i in 0..60) {
            val angle = Math.PI * i / 30 - Math.PI / 2
            val x = xCenter + Math.cos(angle) * (radiusClock+30)
            val y = yCenter + Math.sin(angle) * (radiusClock+30)
            canvas?.drawLine(
                x.toFloat(),
                y.toFloat(),
                (xCenter + Math.cos(angle) * (radiusClock+50)).toFloat(),
                (yCenter + Math.sin(angle) * (radiusClock+50)).toFloat(),
                paint
            )
        }
    }

    fun setTime(hour: Int, min: Int, sec: Int) {
        this.hour = hour
        this.min = min
        this.sec = sec
        invalidate()
    }

    private fun drawHands(canvas: Canvas?) {
        //val calendar=Calendar.getInstance()
        //var hour=calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, (hour + min / 60) * 5f, true)
        drawHand(canvas, min.toFloat(), false)
        drawHand(canvas, sec.toFloat(), false)
    }

    private fun drawHand(canvas: Canvas?, loc: Float, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour) (radiusClock - handTruncationClock - hourHandTruncationClock)
            else (radiusClock - handTruncationClock)
        canvas?.drawLine(
            xCenter, yCenter,
            (xCenter + Math.cos(angle) * handRadius).toFloat(),
            (yCenter + Math.sin(angle) * handRadius).toFloat(), paint
        )
    }

    private fun drawNumeral(canvas: Canvas?) {
        paint.textSize = fontSizeClock.toFloat()
        for (i in numbers) {
            val tmp = i.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (i - 3)
            val x = xCenter + (Math.cos(angle) * radiusClock).toFloat()
            val y = yCenter + (Math.sin(angle) * radiusClock).toFloat()
            canvas?.drawText(tmp, x-15, y+10, paint)
        }
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        canvas?.drawCircle((widthClock / 2).toFloat(), (heightClock / 2).toFloat(), 12f, paint)
    }

    private fun drawCircel(canvas: Canvas?) {
        paint.reset()
        paint.setColor(Color.BLACK)
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas?.drawCircle(
            xCenter,
            yCenter,
            (radiusClock + paddingClock).toFloat(),
            paint
        )
    }
}