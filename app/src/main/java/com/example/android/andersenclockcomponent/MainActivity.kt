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
                    clockView.setHourHandColor(Color.RED)
                    clockView.setMinuteHandColor(Color.GREEN)
                    clockView.setSecondHandColor(Color.MAGENTA)
                    clockView.setHourHandSize(CustomClockView.HOUR_HAND_WIDTH_2)
                    clockView.setMinuteHandSize(CustomClockView.MINUTE_HAND_WIDTH_2)
                    clockView.setSecondHandSize(CustomClockView.SECOND_HAND_WIDTH_2)
                    false
                }
                false -> {
                    clockView.setHourHandColor(Color.BLACK)
                    clockView.setMinuteHandColor(Color.BLACK)
                    clockView.setSecondHandColor(Color.RED)
                    clockView.setHourHandSize(CustomClockView.HOUR_HAND_WIDTH)
                    clockView.setMinuteHandSize(CustomClockView.MINUTE_HAND_WIDTH)
                    clockView.setSecondHandSize(CustomClockView.SECOND_HAND_WIDTH)
                    true
                }
            }
        }
    }
}