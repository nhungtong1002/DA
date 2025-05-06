package com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions

import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant.PATTERN_DETAIL_TIME
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant.PATTERN_TIME
import java.util.concurrent.TimeUnit

fun Long.convertMinutesToMillisecond()
    = this * 60 * 1000;

fun Long.convertSecondToMillisecond(): Long {
    return this * 1000;
}

fun Long.toDateTimeMMSS(): String {
    return String.format(
        PATTERN_TIME,
        TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.HOURS.toMinutes(1)
    )
}

fun Long.toDateTimeDetail(): String {
    return String.format(
        PATTERN_DETAIL_TIME,
        TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.HOURS.toMinutes(1)
    )
}