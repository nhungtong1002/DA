package com.example.applearndriver.utils

import android.os.CountDownTimer
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.utils.extensions.convertMinutesToMillisecond
import com.example.applearndriver.utils.extensions.toDateTimeMMSS

object CountDownInstance {
    private const val REQUESTED_TICK_INTERVAL = 1000L
    private var countDownTimer: CountDownTimer? = null
    private var currentTimeStamp = AppConstant.EXAM_TEST_FULL_TIME

    val CurrentTimeStamp: Long
        get() = currentTimeStamp

    val CurrentTime: String
        get() = currentTimeStamp.toDateTimeMMSS()

    fun setInitialTimeStamp(minutes: Int) {
        currentTimeStamp = minutes.toLong().convertMinutesToMillisecond()
    }

    fun startCountDownFrom(
        timestamp: Long,
        onTickEvent: () -> Unit,
        onFinishEvent: () -> Unit,
    ) {
        if (countDownTimer == null) {
            countDownTimer = object : CountDownTimer(timestamp, REQUESTED_TICK_INTERVAL) {
                override fun onTick(p0: Long) {
                    currentTimeStamp = p0
                    onTickEvent()
                }

                override fun onFinish() {
                    onFinishEvent()
                }
            }

            countDownTimer?.start()
        }
    }

    fun cancelCountDown() {
        countDownTimer?.cancel()
        countDownTimer = null
    }
}
