package com.booking.tripsassignment.utils

import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.data.ChainTitle
import org.joda.time.LocalDate
import java.lang.StringBuilder
import java.util.*
import kotlin.Comparator


fun buildChainBySorting(list: List<Booking>, pastTripTitle: String, upcomingTripTitle: String): List<Chain> {
    if (list.isEmpty()) return emptyList()

    //sort based on sorting
    Collections.sort(list, Comparator { b1, b2 -> b1.checkin.compareTo(b2.checkin) })

    val chains = ArrayList<ArrayList<Booking>>()

    chains.add(arrayListOf(list.first()))

    //segregate chronological bookings into list of chain
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

    //segregate chains into past and upcoming bookings
    chains.forEach {
        val chain = buildBookingChain(it)
        if (chain.checkin.isBefore(now)) {
            pastChains.add(chain)
        } else {
            upComingChains.add(chain)
        }
    }

    //merge lists with title
    return ArrayList<Chain>().apply {
        if (upComingChains.isNotEmpty()) {
            this.add(ChainTitle(upcomingTripTitle))
            this.addAll(upComingChains)
        }
        if (pastChains.isNotEmpty()) {
            this.add(ChainTitle(pastTripTitle))
            this.addAll(pastChains)
        }
    }
}

fun buildBookingChain(chain: List<Booking>): BookingChain {
    return BookingChain(
        chain.map { it.checkin }.joinToString { "" },
        chain,
        chain.first().checkin,
        chain.last().checkout,
        getFormattedDateRange(chain),
        getFormattedTitle(chain),
        getFormattedBookingsCount(chain),
        chain.first().hotel.mainPhoto
    )
}

/**
 *  @param chain list of all bookings in the chain
 *  @return Number of bookings with suffix "booking/s"
 */

fun getFormattedBookingsCount(chain: List<Booking>): String {
    return if (chain.size == 1) {
        "1 booking"
    } else {
        "${chain.size} bookings"
    }
}

/**
 *  Format names of the booking locations (cities) in chronological order prefixed with "Trip to "
 *  @param chain list of all bookings in the chain
 *  @return formatter String of all cities
 */
fun getFormattedTitle(chain: List<Booking>): String {
    val uniqueTitleList = chain.map { it.hotel.cityName }.toSet().toList()
    val builder = StringBuilder("Trips to ")
    val cities = when (uniqueTitleList.size) {
        1 -> uniqueTitleList.first()
        2 -> String.format("%s and %s", uniqueTitleList.first(), uniqueTitleList.last())
        else -> {
            val lastCities = uniqueTitleList.take(uniqueTitleList.size - 1).joinToString(", ")
            String.format("%s and %s", lastCities, uniqueTitleList.last())
        }
    }
    return builder.append(cities).toString()
}

/**
 *  Format date range for booking chain based on checkin of first booking and checkout of last booking
 *  @param chain list of all bookings in the chain
 *  @return formatter date range
 */
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