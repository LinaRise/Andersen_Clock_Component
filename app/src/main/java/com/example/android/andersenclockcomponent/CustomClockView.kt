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
    private var mSAgree = 0
    private var mHAgree = 0
    private var mMAgree = 0

    private val clockPaint: Paint
    private val hourHandPaint: Paint
    private val minuteHandPaint: Paint
    private var secondHandPaint: Paint
    private val centerPaint: Paint
    private val linesPaint: Paint

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
    }

    private var mHeight = 0
    private var mWidth = 0
    private var centerX = 0
    private var centerY = 0

    private var radius = 0
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
            minuteHandColor = ta.getColor(R.styleable.CustomClockView_minute_hand_color, Color.BLACK)
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
            color = Color.BLACK
            style = Paint.Style.STROKE
        }
        hourHandPaint = Paint().apply {
            color = hourHandColor
            strokeWidth = hourHandSize
        }

        minuteHandPaint = Paint().apply {
            color = minuteHandColor
            strokeWidth = minuteHandSize
        }
        secondHandPaint = Paint().apply {
            color = secondHandColor
            strokeWidth = secondHandSize
        }
        centerPaint = Paint().apply {
            color = Color.BLACK
        }

        linesPaint = Paint().apply {
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
        radius = mMin / 2 - PADDING

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
            radius.toFloat(),
            clockPaint
        )
        drawHands(canvas)
        drawCenter(canvas)
    }

    private fun drawHands(canvas: Canvas?) {
        val calendar = Calendar.getInstance()
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)
        val mSecond = calendar.get(Calendar.SECOND)
        update(mHour, mMinute, mSecond)

        drawHourLine(canvas)
        drawMinuteLine(canvas)
        drawSecondLine(canvas)
    }

    private fun update(hh: Int, mm: Int, ss: Int) {
        mHAgree = hh * 360 / 12
        mMAgree = mm * 6
        mSAgree = ss * 6
        invalidate()
    }

    private fun drawHourLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(mHAgree.toFloat(), centerX.toFloat(), centerY.toFloat())
        canvas?.drawLine(
            centerX.toFloat(),
            centerY.toFloat(), centerX.toFloat(), centerY.toFloat() - 250, hourHandPaint
        )
        canvas?.restore()
    }

    private fun drawMinuteLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(mMAgree.toFloat(), centerX.toFloat(), centerY.toFloat())
        canvas?.drawLine(
            centerX.toFloat(),
            centerY.toFloat(), centerX.toFloat(), centerY.toFloat() - 300, minuteHandPaint
        )
        canvas?.restore()
    }


    private fun drawSecondLine(canvas: Canvas?) {
        canvas?.save()
        canvas?.rotate(
            mSAgree.toFloat(), centerX.toFloat(),
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
                    (centerY + radius).toFloat(),
                    centerX.toFloat(),
                    centerY.toFloat() + radius - PADDING - 10f,
                    linesPaint
                )
            } else {
                linesPaint.strokeWidth = 2F
                linesPaint.color = Color.GREEN
                canvas?.drawLine(
                    centerX.toFloat(),
                    (centerY + radius).toFloat(),
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


    fun setSecondHandColor(color: Int) {
        secondHandPaint.color = color
        postInvalidate()
    }

    fun setMinuteHandColor(color: Int) {
        minuteHandPaint.color = color
        postInvalidate()
    }

    fun setHourHandColor(color: Int) {
        hourHandPaint.color = color
        postInvalidate()
    }

    fun setSecondHandSize(size: Float) {
        secondHandPaint.strokeWidth = size
        postInvalidate()
    }

    fun setMinuteHandSize(size: Float) {
        minuteHandPaint.strokeWidth = size
        postInvalidate()
    }

    fun setHourHandSize(size: Float) {
        hourHandPaint.strokeWidth = size
        postInvalidate()
    }
}

