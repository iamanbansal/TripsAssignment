package com.booking.tripsassignment.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test

import org.junit.Rule

class MockNetworkBookingRepositoryTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    lateinit var repository: BookingRepository

    @Before
    fun setUp() {
        repository = MockNetworkBookingRepository()
    }

    @Test
    fun fetchBookings() = runBlocking {
        val expectedList = listOf(Resource.Loading, Resource.Success(listOf<Booking>()))
        val actualList = arrayListOf<Resource<List<Booking>>>()
//        val collectJob = launch (UnconfinedTestDispatcher(testScheduler)) {


        val job = launch {


        }
        repository.getBookingsFlow().test {
            val item = awaitItem()
            actualList.add(item)
        }
        repository.fetchBookings(TestCase.PAST_CHAIN.bookerId)
//

//            repository.getMovieSearchFlow().toList(actualList)

        TestCoroutineDispatcher()
//
//        }

//        val actualList = repository.getMovieSearchFlow().toList()

        job.join()
        assert(actualList.size == expectedList.size)
        job.cancel()
    }

    @Test
    fun getMovieSearchFlow() {
    }
}