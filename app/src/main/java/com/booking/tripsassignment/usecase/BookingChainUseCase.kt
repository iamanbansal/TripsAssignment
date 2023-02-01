package com.booking.tripsassignment.usecase

import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.TestCase
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.collections.ArrayList

class BookingChainUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    fun getBookingsChain(): Flow<Resource<List<BookingChain>>> {
        return bookingRepository.getBookingsFlow()
            .mapLatest {
                when (it) {
                    is Resource.Loading -> Resource.Loading

                    is Resource.Success ->
                        Resource.Success(buildChainBySorting(it.data()!!))

                    is Resource.Error -> Resource.Error(it.exception)
                }
            }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchBookings(id: Int = TestCase.PAST_CHAIN.bookerId) {
        bookingRepository.fetchBookings(id)
    }

    fun buildChainBySorting(list: List<Booking>): List<BookingChain> {

        val today = LocalDate.now()
        val map = mutableMapOf<LocalDate, ArrayList<Booking>>()

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

        return ArrayList<BookingChain>().apply {
            chains.forEach {
                this.add(buildBookingChain(it))
            }
        }
    }

    private fun buildBookingChain(chain: List<Booking>): BookingChain {
        return BookingChain(
            chain,
            chain.first().checkin,
            chain.last().checkout,
            getFormattedDateRange(chain),
            getFormattedTitle(chain),
            getFormattedBookings(chain)
        )
    }

    private fun getFormattedBookings(chain: List<Booking>): String {
        return if (chain.size == 1) {
            "1 booking"
        } else {
            "${chain.size} bookings"
        }
    }

    private fun getFormattedTitle(chain: List<Booking>): String {
        val uniqueTitleList = chain.map { it.hotel.cityName }.toSet().toList()
        return when (uniqueTitleList.size) {
            1 -> uniqueTitleList.first()
            2 -> String.format("%s and %s", uniqueTitleList.first(), uniqueTitleList.last())
            else -> {
                val lastCities = uniqueTitleList.take(uniqueTitleList.size-1).joinToString(", ")
                String.format("%s and %s", lastCities, uniqueTitleList.last())
            }
        }
    }

    private fun getFormattedDateRange(chain: List<Booking>): String {
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
        return "${formatter.startDateFormat.format(startDate)}-${formatter.endDateFormat.format(endDate)}"
    }


    enum class DateFormatter(val startDateFormat: SimpleDateFormat, val endDateFormat: SimpleDateFormat) {
        SAME_MONTH_YEAR(SimpleDateFormat("d", Locale.ENGLISH), SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)),
        SAME_YEAR(SimpleDateFormat("d MMM", Locale.ENGLISH), SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)),
        DIFF_YEAR(
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH),
            SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
        )
    }
}