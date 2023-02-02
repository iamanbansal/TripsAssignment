package com.booking.tripsassignment.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MockNetworkBookingRepositoryTest {
    lateinit var repository: BookingRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun fetchBookings() =  runTest(StandardTestDispatcher()) {
        repository = MockNetworkBookingRepository()
        repository.getBookingsFlow().test {
            repository.fetchBookings(TestCase.PAST_CHAIN.bookerId)
            assert(awaitItem() is Resource.Loading)
            assert(awaitItem() is Resource.Success)
        }
    }
}