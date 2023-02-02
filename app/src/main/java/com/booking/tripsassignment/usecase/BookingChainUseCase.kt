package com.booking.tripsassignment.usecase

import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.TestCase
import com.booking.tripsassignment.utils.Resource
import com.booking.tripsassignment.utils.buildChainBySorting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

interface BookingChainUseCase {
    fun getBookingsChain(): Flow<Resource<List<Chain>>>
    suspend fun fetchBookings(id: Int = TestCase.MIX_1.bookerId)
}

class BookingChainUseCaseImpl(
    private val bookingRepository: BookingRepository
) : BookingChainUseCase {
    override fun getBookingsChain(): Flow<Resource<List<Chain>>> {
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

    override suspend fun fetchBookings(id: Int) {
        bookingRepository.fetchBookings(id)
    }
}