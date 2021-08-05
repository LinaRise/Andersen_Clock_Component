package com.example.android.andersenclockcomponent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*


class CustomClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mSAgree = 0f
    private var mHAgree = 0f
    private var mMAgree = 0f
    private var hour = 0
    private var minute = 0
    private var second = 0

    private var clockPaint: Paint
    var hourHandPaint: Paint
    var minuteHandPaint: Paint
    var secondHandPaint: Paint
    private var centerPaint: Paint
    private var linesPaint: Paint

    companion object {
        const val UPDATE = 500L
        const val CIRCLE_WIDTH = 25f
        const val LINE_WIDTH = 15f
        const val HOUR_HAND_WIDTH = 20f
        const val HOUR_HAND_WIDTH_2 = 10f
        const val MINUTE_HAND_WIDTH = 12f
        const val MINUTE_HAND_WIDTH_2 = 6f
        const val SECOND_HAND_WIDTH = 5f
        const val SECOND_HAND_WIDTH_2 = 3f
        const val CENTER_RADIUS = 20f
        const val PADDING = 50
        const val SMOOTH_HOUR = 0.081f
        const val SMOOTH_MINUTE = 0.016f
    }

    private var mHeight = 0
    private var mWidth = 0
    private var centerX = 0
    private var centerY = 0

    private var radius = 0f
    private var mMin = 0

    private val rangeMinutes = 1..60
    private var isInit = false

    private var secondHandColor = 0
    private var minuteHandColor = 0
    private var hourHandColor = 0
    private var secondHandSize = 0f
    private var minuteHandSize = 0f
    private var hourHandSize = 0f


    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomClockView, 0, 0)
        try {
            hourHandColor = ta.getColor(R.styleable.CustomClockView_hour_hand_color, Color.BLACK)
            minuteHandColor =
                ta.getColor(R.styleable.CustomClockView_minute_hand_color, Color.BLACK)
            secondHandColor = ta.getColor(R.styleable.CustomClockView_second_hand_color, Color.RED)
            hourHandSize =
                ta.getDimension(R.styleable.CustomClockView_second_hand_color, HOUR_HAND_WIDTH)
            minuteHandSize =
                ta.getDimension(R.styleable.CustomClockView_second_hand_color, MINUTE_HAND_WIDTH)
            secondHandSize =
                ta.getDimension(R.styleable.CustomClockView_second_hand_color, SECOND_HAND_WIDTH)
        } finally {
            ta.recycle()
        }

        clockPaint = Paint().apply {
            strokeWidth = CIRCLE_WIDTH
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
        }
        hourHandPaint = Paint().apply {
            isAntiAlias = true
            color = hourHandColor
            strokeWidth = hourHandSize
        }

        minuteHandPaint = Paint().apply {
            isAntiAlias = true
            color = minuteHandColor
            strokeWidth = minuteHandSize
        }
        secondHandPaint = Paint().apply {
            isAntiAlias = true
            color = secondHandColor
            strokeWidth = secondHandSize
        }
        centerPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
        }

        linesPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeWidth = LINE_WIDTH
        }
    }

    private fun initClock() {
        mHeight = height
        mWidth = width

        centerX = mWidth / 2
        centerY = mHeight / 2

        //что из этого больше
        mMin = mHeight.coerceAtMost(mWidth)
        radius = (mMin / 2 - PADDING).toFloat()

        isInit = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isInit) {
            initClock()
        }
        drawClock(canvas)
        postInvalidateDelayed(UPDATE)
    }

    private fun drawClock(canvas: Canvas?) {
        drawZoneLine(canvas)

        canvas?.drawCircle(
            centerX.toFloat(),
            centerY.toFloat(),
            radius,
            clockPaint
        )
        drawHands(canvas)
        drawCenter(canvas)
    }

    private fun drawHands(canvas: Canvas?) {
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)
        mHAgree = (360 / 12 * hour).toFloat()
        mMAgree = (360 / 60 * minute).toFloat()
        mSAgree = (360 / 60 * second).toFloat()

        drawHourLine(canvas)
        drawMinuteLine(canvas)
        drawSecondLine(canvas)
    }


    private fun drawHourLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(mHAgree + mMAgree * SMOOTH_HOUR, centerX.toFloat(), centerY.toFloat())
        canvas?.drawLine(
            centerX.toFloat(),
            centerY.toFloat(), centerX.toFloat(), centerY.toFloat() - 250, hourHandPaint
        )
        canvas?.restore()
    }

    private fun drawMinuteLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(mMAgree + mSAgree * SMOOTH_MINUTE, centerX.toFloat(), centerY.toFloat())
        canvas?.drawLine(
            centerX.toFloat(),
            centerY.toFloat(), centerX.toFloat(), centerY.toFloat() - 300, minuteHandPaint
        )
        canvas?.restore()
    }

    private fun drawSecondLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(
            mSAgree, centerX.toFloat(),
            centerY.toFloat()
        )
        canvas?.drawLine(
            centerX.toFloat(),
            centerY.toFloat(),
            centerX.toFloat(),
            centerY.toFloat() - 400,
            secondHandPaint
        )
        canvas?.restore()
    }

    private fun drawZoneLine(canvas: Canvas?) {
        for (i in rangeMinutes) {
            canvas?.save()
            canvas?.rotate((360 / 60 * i).toFloat(), centerX.toFloat(), centerY.toFloat())
            if (i % 5 == 0) {
                linesPaint.strokeWidth = 5F
                linesPaint.color = Color.BLUE
                canvas?.drawLine(
                    centerX.toFloat(),
                    (centerY + radius),
                    centerX.toFloat(),
                    centerY.toFloat() + radius - PADDING - 10f,
                    linesPaint
                )
            } else {
                linesPaint.strokeWidth = 2F
                linesPaint.color = Color.GREEN
                canvas?.drawLine(
                    centerX.toFloat(),
                    (centerY + radius),
                    centerX.toFloat(),
                    centerY.toFloat() + radius - PADDING + 10f,
                    linesPaint
                )
            }
            canvas?.restore()
        }
    }

    private fun drawCenter(canvas: Canvas?) {
        canvas?.drawCircle(
            centerX.toFloat(),
            centerY.toFloat(),
            CENTER_RADIUS,
            centerPaint
        )
    }
}

