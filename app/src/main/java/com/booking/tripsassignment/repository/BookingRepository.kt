package com.booking.tripsassignment.repository

import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.utils.NetworkError
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * An interface to represent a repository that returns a list of bookings for the given user.
 */
interface BookingRepository {
    /**
     * Emit list of bookings for a given user Id into flow.
     *
     * Assumptions:
     *
     *  - All the bookings emitted will belong to that user.
     */
    suspend fun fetchBookings(userId: Int)
    /**
     * Return the flow of state of list of booking
     */
    fun getBookingsFlow(): Flow<Resource<List<Booking>>>
}

/**
 *  An implementation for the repository which returns bookings based on a generated dataset.
 *  Check [TestCase] class to refer to the different scenarios and which userId will provide bookings for those scenarios.
 */
class MockNetworkBookingRepository : BookingRepository {

    private val resultFlow = MutableSharedFlow<Resource<List<Booking>>>()

    override suspend fun fetchBookings(userId: Int) = withContext(Dispatchers.IO) {

        delay(Random.nextInt(10, 2000).toLong())
        if (Random.nextInt(0, 21) % 10 == 0) {
            resultFlow.emit(Resource.Error(NetworkError("API call error")))
        }

        val bookings = MockDataGenerator.bookingsForUser(userId)
        if (bookings == null) {
            resultFlow.emit(Resource.Error(NetworkError("API call error")))
        } else {
            resultFlow.emit(Resource.Success(bookings))
        }
    }

    override fun getBookingsFlow() = resultFlow
}