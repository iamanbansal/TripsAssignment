package com.booking.tripsassignment.utils

import com.booking.tripsassignment.data.*
import com.booking.tripsassignment.repository.*
import org.joda.time.LocalDate
import org.junit.Test

import org.junit.Assert.*

class BookingChainUtilKtTest {

     val pastTripTitle = "Past Trips"
     val upcomingTripTitle = "Upcoming Trips"

    @Test
    fun `test buildChainBySorting for past bookings`() {
        val booking = MockDataGenerator.bookingsForUser(TestCase.PAST_BOOKINGS.bookerId)
        val chains = buildChainBySorting(booking!!, pastTripTitle, upcomingTripTitle)
        val today = LocalDate.now()
        assert((chains.first() as ChainTitle).title == pastTripTitle)
        for (i in 1..chains.lastIndex) {
            (chains[i] as BookingChain).list.forEach {
                assert(it.checkin.isBefore(today))
            }
        }
    }

    @Test
    fun `test buildChainBySorting for upcoming bookings`() {

        val booking = MockDataGenerator.bookingsForUser(TestCase.FUTURE_BOOKINGS.bookerId)
        val chains = buildChainBySorting(booking!!, pastTripTitle, upcomingTripTitle)
        val today = LocalDate.now()
        assert((chains.first() as ChainTitle).title == upcomingTripTitle)
        for (i in 1..chains.lastIndex) {
            (chains[i] as BookingChain).list.forEach {
                assert(it.checkin.isAfter(today))
            }
        }
    }

    @Test
    fun `test buildChainBySorting for past & upcoming bookings`() {

        val booking = MockDataGenerator.bookingsForUser(TestCase.MIX_1.bookerId)
        val chains = buildChainBySorting(booking!!, pastTripTitle, upcomingTripTitle)
        val today = LocalDate.now()
        assert((chains.first() as ChainTitle).title == upcomingTripTitle)

        var upcomingStartIndex = Int.MIN_VALUE
        for (i in 1..chains.lastIndex) {
            val chain = chains[i]
            if (chain is BookingChain) {
                chain.list.forEach {
                    assert(it.checkin.isAfter(today))
                }
            } else {
                upcomingStartIndex = i
                break
            }
        }

        assert((chains[upcomingStartIndex] as ChainTitle).title == pastTripTitle)
        for (i in upcomingStartIndex + 1..chains.lastIndex) {
            (chains[i] as BookingChain).list.forEach {
                assert(it.checkin.isBefore(today))
            }
        }
    }

    @Test
    fun `test buildBookingChain to verify chain details`() {
        val booking = buildBooking(
            booker = Booker(12, "f1", "l1"),
            hotel = City.NICE.hotels()[0],
            price = Price.mocksEUR[0],
            days = -50,
            duration = 4
        )
        val booking2 = buildBooking(
            booker = Booker(15, "f2", "l2"),
            hotel = City.AGRA.hotels()[0],
            price = Price.mocksEUR[0],
            days = -51,
            duration = 7
        )
        val chains = listOf(booking, booking2)

        val chain = buildBookingChain(chains)

        assertEquals(chains, chain.list)
        assertEquals(booking.checkin, chain.checkin)
        assertEquals(booking2.checkout, chain.checkout)
        assertEquals(booking.hotel.mainPhoto, chain.imageUrl)
        assertEquals(getFormattedTitle(chains), chain.title)
        assertEquals(getFormattedDateRange(chains), chain.dateRange)
        assertEquals(getFormattedBookingsCount(chains), chain.subTitle)
    }

    @Test
    fun `test getFormattedBookings`() {
        val booking = buildBooking(
            booker = Booker(12, "f1", "l1"),
            hotel = City.NICE.hotels()[0],
            price = Price.mocksEUR[0],
            days = -50,
            duration = 4
        )
        val booking2 = buildBooking(
            booker = Booker(15, "f2", "l2"),
            hotel = City.AGRA.hotels()[0],
            price = Price.mocksEUR[0],
            days = -51,
            duration = 7
        )

        val bookingCount1 = getFormattedBookingsCount(listOf(booking))

        assertEquals("1 booking", bookingCount1)

        val bookingCount2 = getFormattedBookingsCount(listOf(booking, booking2))

        assertEquals("2 bookings", bookingCount2)
    }

    @Test
    fun `test getFormattedTitle verify the string`() {
        val booking = buildBooking(
            booker = Booker(12, "f1", "l1"),
            hotel = City.NICE.hotels()[0],
            price = Price.mocksEUR[0],
            days = -50,
            duration = 9
        )
        val booking2 = buildBooking(
            booker = Booker(15, "f2", "l2"),
            hotel = City.AGRA.hotels()[0],
            price = Price.mocksEUR[0],
            days = -40,
            duration = 7
        )

        val booking3 = buildBooking(
            booker = Booker(154, "f3", "l3"),
            hotel = City.AGRA.hotels()[0],
            price = Price.mocksEUR[0],
            days = -30,
            duration = 4
        )

        val booking4 = buildBooking(
            booker = Booker(152, "f2", "l2"),
            hotel = City.GOA.hotels()[0],
            price = Price.mocksEUR[0],
            days = -20,
            duration = 2
        )

        val title1 = getFormattedTitle(listOf(booking))

        assertEquals("Trips to ${booking.hotel.cityName}", title1)

        val title2 = getFormattedTitle(listOf(booking, booking2))

        assertEquals("Trips to ${booking.hotel.cityName} and ${booking2.hotel.cityName}", title2)


        val title3 = getFormattedTitle(listOf(booking, booking2, booking3, booking4))

        assertEquals(
            "Trips to ${booking.hotel.cityName}, ${booking2.hotel.cityName} and ${booking4.hotel.cityName}",
            title3
        )
    }

    @Test
    fun `test getFormattedDateRange same year`() {
        val booking = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2023, 1, 1),
            checkout = LocalDate(2023, 1, 15),
            price = Price.mocksEUR[0]
        )
        val booking2 = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2023, 5, 1),
            checkout = LocalDate(2023, 5, 6),
            price = Price.mocksEUR[0]
        )

        val dates = getFormattedDateRange(listOf(booking, booking2))

        assertEquals("1 Jan-6 May 2023", dates)
    }

    @Test
    fun `test getFormattedDateRange same month same year`() {
        val booking = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2023, 1, 1),
            checkout = LocalDate(2023, 1, 15),
            price = Price.mocksEUR[0]
        )
        val booking2 = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2023, 1, 18),
            checkout = LocalDate(2023, 1, 30),
            price = Price.mocksEUR[0]
        )

        val dates = getFormattedDateRange(listOf(booking, booking2))

        assertEquals("1-30 Jan 2023", dates)
    }

    @Test
    fun `test getFormattedDateRange different year`() {
        val booking = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2022, 1, 1),
            checkout = LocalDate(2022, 1, 15),
            price = Price.mocksEUR[0]
        )
        val booking2 = Booking(
            id = "1",
            hotel = City.STOCKHOLM.hotels()[1],
            checkin = LocalDate(2023, 1, 18),
            checkout = LocalDate(2023, 1, 30),
            price = Price.mocksEUR[0]
        )

        val dates = getFormattedDateRange(listOf(booking, booking2))

        assertEquals("1 Jan 2022-30 Jan 2023", dates)
    }
}