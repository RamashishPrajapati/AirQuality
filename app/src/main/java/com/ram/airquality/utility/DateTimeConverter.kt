/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ram.airquality.utility

import android.annotation.SuppressLint
import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


/**
 * These functions create a formatted string that can be set in a TextView.
 * Convert a duration to a formatted string for display.
 * @param startTimeMilli the start of the interval
 * @param endTimeMilli the end of the interval
 * @param res resources used to load formatted strings
 */

@SuppressLint("StringFormatMatches")
fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    val durationMilli = endTimeMilli - startTimeMilli
    val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)

    return when {
        minutes < 1 -> "A few second ago"
        minutes.equals(1) -> "A minutes ago"
        minutes in 2..59 -> "$minutes minutes ago"
        minutes in 60..1440 -> convertLongToDateString(durationMilli, Constants.CUSTOM_TIME_FORMAT)
        else -> convertLongToDateString(durationMilli, Constants.CUSTOM_DATE_TIME_FORMAT)
    }
}


/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long, format: String): String {
    return SimpleDateFormat(format)
        .format(systemTime).toString()
}

