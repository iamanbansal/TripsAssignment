package com.booking.tripsassignment.utils

import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.data.ChainTitle
import com.booking.tripsassignment.usecase.DateFormatter
import org.joda.time.LocalDate
import java.util.*
import kotlin.Comparator

fun buildChainBySorting(list: List<Booking>): List<Chain> {
    if (list.isEmpty()) return emptyList()

    Collections.sort(list, Comparator { b1, b2 -> b1.checkin.compareTo(b2.checkin) })

    val chains = ArrayList<ArrayList<Booking>>()

    chains.add(arrayListOf(list.first()))

    for (i in 1..list.lastIndex) {
        if (list[i].checkin == chains.last().last().checkout) {
            chains.last().add(list[i])
        } else {
            chains.add(arrayListOf(list[i]))
        }
    }

    val now = LocalDate.now()
    val pastChains = arrayListOf<Chain>()
    val upComingChains = arrayListOf<Chain>()

    chains.forEach {
        val chain = buildBookingChain(it)
        if (chain.checkin.isBefore(now)) {
            pastChains.add(chain)
        } else {
            upComingChains.add(chain)
        }
    }
    return ArrayList<Chain>().apply {
        if (upComingChains.isNotEmpty()) {
            this.add(ChainTitle("Upcoming Trips"))
            this.addAll(upComingChains)
        }
        if (pastChains.isNotEmpty()) {
            this.add(ChainTitle("Past Trips"))
            this.addAll(pastChains)
        }
    }
}

fun buildBookingChain(chain: List<Booking>): BookingChain {
    return BookingChain(
        chain,
        chain.first().checkin,
        chain.last().checkout,
        getFormattedDateRange(chain),
        getFormattedTitle(chain),
        getFormattedBookings(chain),
        chain.first().hotel.mainPhoto
    )
}

fun getFormattedBookings(chain: List<Booking>): String {
    return if (chain.size == 1) {
        "1 booking"
    } else {
        "${chain.size} bookings"
    }
}

fun getFormattedTitle(chain: List<Booking>): String {
    val uniqueTitleList = chain.map { it.hotel.cityName }.toSet().toList()
    return when (uniqueTitleList.size) {
        1 -> uniqueTitleList.first()
        2 -> String.format("%s and %s", uniqueTitleList.first(), uniqueTitleList.last())
        else -> {
            val lastCities = uniqueTitleList.take(uniqueTitleList.size - 1).joinToString(", ")
            String.format("%s and %s", lastCities, uniqueTitleList.last())
        }
    }
}

fun getFormattedDateRange(chain: List<Booking>): String {
    val startDate = chain.first().checkin.toDate()
    val endDate = chain.last().checkout.toDate()

    val formatter = if (startDate.year == endDate.year) {
        if (startDate.month == endDate.month) {
            DateFormatter.SAME_MONTH_YEAR
        } else {
            DateFormatter.SAME_YEAR
        }
    } else {
        DateFormatter.DIFF_YEAR
    }
    return formatter.startDateFormat.format(startDate) +
            "-" +
            formatter.endDateFormat.format(endDate)
}