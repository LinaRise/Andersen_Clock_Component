package com.example.android.andersenclockcomponent

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnInProgram: Button
    private lateinit var clockView: CustomClockView

    private var defaultStyle = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnInProgram = findViewById(R.id.btn_program_change)
        clockView = findViewById(R.id.clockView)

        btnInProgram.setOnClickListener {
            defaultStyle = when (defaultStyle) {
                true -> {
                    clockView.hourHandPaint.color = Color.RED
                    clockView.minuteHandPaint.color = Color.GREEN
                    clockView.secondHandPaint.color = Color.MAGENTA
                    clockView.hourHandPaint.strokeWidth = CustomClockView.HOUR_HAND_WIDTH_2
                    clockView.minuteHandPaint.strokeWidth = CustomClockView.MINUTE_HAND_WIDTH_2
                    clockView.secondHandPaint.strokeWidth = CustomClockView.SECOND_HAND_WIDTH_2
                    false
                }
                false -> {
                    clockView.hourHandPaint.color = Color.BLACK
                    clockView.minuteHandPaint.color = Color.BLACK
                    clockView.secondHandPaint.color = Color.RED
                    clockView.hourHandPaint.strokeWidth = CustomClockView.HOUR_HAND_WIDTH
                    clockView.minuteHandPaint.strokeWidth = CustomClockView.MINUTE_HAND_WIDTH
                    clockView.secondHandPaint.strokeWidth = CustomClockView.SECOND_HAND_WIDTH
                    true
                }
            }
        }
    }
}