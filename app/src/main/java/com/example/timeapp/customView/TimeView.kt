package com.example.timeapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class TimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

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
    private var hour = 6
    private var min = 15
    private var sec = 45
    private var xCenter = 0f
    private var yCenter = 0f
    private var touchActive = 0
    private val HOUR = 1
    private val MIN = 2
    private val SEC = 3
    private val DEACTIVE = 0
    private var actualI = 0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInit) initClock()
        canvas?.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumbers(canvas)
        drawSecond(canvas)
        drawHands(canvas)

        //postInvalidateDelayed(500)
        //invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            val xTemp = event.x
            val yTemp = event.y
            if ((event.action == MotionEvent.ACTION_DOWN) || (event.action == MotionEvent.ACTION_POINTER_DOWN)) {
                for (i in 0..60) {
                    val angle = Math.PI * i / 30 - Math.PI / 2
                    val xEnd = (xCenter + cos(angle) * (radiusClock + 50)).toFloat()
                    val yEnd = (yCenter + sin(angle) * (radiusClock + 50)).toFloat()
                    var a = 0f
                    if (xCenter != xEnd) a = (yCenter - yEnd) / (xCenter - xEnd)
                    val b = ((yCenter + yEnd) - a * (xCenter + xEnd)) / 2
                    if (xCenter < xEnd) {
                        if ((abs(yTemp - (a * xTemp + b)) < 60) && (xTemp > xCenter) && (xTemp < xEnd)) {
                            if(setTouchActive(i)) return true
                        }

                    } else if (xCenter >= xEnd) {
                        if ((abs(yTemp - (a * xTemp + b)) < 60) && (xTemp <= xCenter) && (xTemp >= xEnd)) {
                            if(setTouchActive(i)) return true
                        }

                    }
                    if((xCenter==xEnd)&&(abs(xTemp-xCenter)<5)&&(yCenter>yTemp)){
                        if(setTouchActive(i)) return true
                    }else if((xCenter==xEnd)&&(abs(xTemp-xCenter)<5)&&(yCenter<=yTemp)){
                        if(setTouchActive(i)) return true
                    }
                }
            } else if ((event.action == MotionEvent.ACTION_MOVE) || (event.action == MotionEvent.ACTION_HOVER_MOVE)) {
                for (i in 0..60) {
                    val angle = Math.PI * i / 30 - Math.PI / 2
                    val xEnd = (xCenter + cos(angle) * (radiusClock + 50)).toFloat()
                    val yEnd = (yCenter + sin(angle) * (radiusClock + 50)).toFloat()
                    var a = 0f
                    if (xCenter != xEnd) a = (yCenter - yEnd) / (xCenter - xEnd)
                    val b = ((yCenter + yEnd) - a * (xCenter + xEnd)) / 2
                    if (xCenter < xEnd) {
                        if ((abs(yTemp - (a * xTemp + b)) < 30) && (xTemp >= xCenter) && (xTemp <= xEnd)) {
                            if(movHand(i)) return true
                        }

                    } else if (xCenter >= xEnd) {
                        if ((abs(yTemp - (a * xTemp + b)) < 30) && (xTemp <= xCenter) && (xTemp >= xEnd)) {
                            if(movHand(i)) return true
                        }

                    }

                    if((xCenter==xEnd)&&(abs(xTemp-xCenter)<5)&&(yCenter>yTemp)&&(i==0)){
                        if(movHand(i)) return true
                    }else if((xCenter==xEnd)&&(abs(xTemp-xCenter)<5)&&(yCenter<=yTemp)&&(i==30)){
                        if(movHand(i)) return true
                    }
                }
            } else if ((event.action == MotionEvent.ACTION_UP) ||
                (event.action == MotionEvent.ACTION_POINTER_UP) ||
                (event.action == MotionEvent.ACTION_CANCEL)
            )
                touchActive = DEACTIVE
        }
        return true
    }

    fun getMin()=min
    fun getHour()=hour
    fun getSec()=sec

    private fun movHand(i: Int): Boolean{
        when (touchActive) {
            HOUR -> {
                hour = i / 5
                invalidate()
                actualI = i
                return true
            }
            MIN -> {
                min = i
                invalidate()
                actualI = i
                return true
            }
            SEC -> {
                sec = i
                invalidate()
                actualI = i
                return true
            }
        }
        return false
    }

    private fun setTouchActive(i: Int): Boolean{
        if (abs((hour - i / 5).toDouble()) < 2) {
            touchActive = HOUR
            invalidate()
            actualI = i
            return true
        } else if (abs((min - i).toFloat()) < 4) {
            touchActive = MIN
            invalidate()
            actualI = i
            return true
        } else if (abs((sec - i).toFloat()) < 4) {
            touchActive = SEC
            invalidate()
            actualI = i
            return true
        }
        return false
    }


    private fun initClock() {
        widthClock = width
        heightClock = height
        xCenter = (widthClock / 2).toFloat()
        yCenter = (heightClock / 2).toFloat()
        paddingClock = 50
        fontSizeClock = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics
        ).toInt()
        val min = heightClock.coerceAtMost(widthClock)
        radiusClock = min / 2 - paddingClock - 5
        handTruncationClock = min / 20
        hourHandTruncationClock = min / 7
        isInit = true
    }

    fun setTime(hour: Int, min: Int, sec: Int) {
        this.hour = hour
        this.min = min
        this.sec = sec
        invalidate()
    }

    private fun drawSecond(canvas: Canvas?) {
        for (i in 0..60) {
            val angle = Math.PI * i / 30 - Math.PI / 2
            val x = xCenter + cos(angle) * (radiusClock + 30)
            val y = yCenter + sin(angle) * (radiusClock + 30)
            canvas?.drawLine(
                x.toFloat(),
                y.toFloat(),
                (xCenter + cos(angle) * (radiusClock + 50)).toFloat(),
                (yCenter + sin(angle) * (radiusClock + 50)).toFloat(),
                paint
            )
        }
    }

    private fun drawHands(canvas: Canvas?) {
//        val cal= Calendar.getInstance()
//        hour=cal.get(Calendar.HOUR_OF_DAY)
//        min=cal.get(Calendar.MINUTE)
//        sec=cal.get(Calendar.SECOND)

        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, (hour + min / 60) * 5f, true)
        drawHand(canvas, min.toFloat(), false)
        drawHand(canvas, sec.toFloat(), null)
    }

    private fun drawHand(canvas: Canvas?, loc: Float, isHour: Boolean?) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour == true) (radiusClock - handTruncationClock - hourHandTruncationClock)
            else if (isHour == false) (radiusClock - handTruncationClock)
            else radiusClock
        canvas?.drawLine(
            xCenter, yCenter,
            (xCenter + cos(angle) * handRadius).toFloat(),
            (yCenter + sin(angle) * handRadius).toFloat(), paint
        )
    }

    private fun drawNumbers(canvas: Canvas?) {
        paint.textSize = fontSizeClock.toFloat()
        for (i in numbers) {
            val tmp = i.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (i - 3)
            val x = xCenter + (cos(angle) * radiusClock).toFloat()
            val y = yCenter + (sin(angle) * radiusClock).toFloat()
            canvas?.drawText(tmp, x - 15, y + 10, paint)
        }
        canvas?.drawText(actualI.toString(), xCenter + 40, yCenter + 40, paint)
    }

    private fun drawCenter(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        canvas?.drawCircle((widthClock / 2).toFloat(), (heightClock / 2).toFloat(), 12f, paint)
    }

    private fun drawCircle(canvas: Canvas?) {
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