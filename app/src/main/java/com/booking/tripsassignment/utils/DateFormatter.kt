package com.booking.tripsassignment.utils

import java.text.SimpleDateFormat
import java.util.*

enum class DateFormatter(
    val startDateFormat: SimpleDateFormat,
    val endDateFormat: SimpleDateFormat
    ) {
        SAME_MONTH_YEAR(
            SimpleDateFormat("d", Locale.ENGLISH),
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
        ),
        SAME_YEAR(
            SimpleDateFormat("d MMM", Locale.ENGLISH),
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
        ),
        DIFF_YEAR(
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH),
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
        )
    }