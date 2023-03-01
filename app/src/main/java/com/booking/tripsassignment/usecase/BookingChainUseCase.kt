package com.booking.tripsassignment.usecase

import com.booking.tripsassignment.R
import com.booking.tripsassignment.chaindetails.ChainDetailsRepository
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.TestCase
import com.booking.tripsassignment.resource.ResourceManager
import com.booking.tripsassignment.utils.Resource
import com.booking.tripsassignment.utils.buildChainBySorting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

interface BookingChainUseCase {
    fun getBookingsChain(): Flow<Resource<List<Chain>>>
    suspend fun fetchBookings(id: Int = TestCase.MIX_1.bookerId)
}

class BookingChainUseCaseImpl(
    private val resourceManager: ResourceManager,
    private val bookingRepository: BookingRepository,
    private val chainDetailsRepository: ChainDetailsRepository
) : BookingChainUseCase {
    override fun getBookingsChain(): Flow<Resource<List<Chain>>> {
        return bookingRepository.getBookingsFlow()
            .map {
                when (it) {
                    is Resource.Success -> {
                        val pastTripTitle = resourceManager.getString(R.string.past_trip_title)
                        val upcomingTripTitle = resourceManager.getString(R.string.upcoming_trip_title)
                        val chainList = buildChainBySorting(it.data()!!, pastTripTitle, upcomingTripTitle)
                        chainDetailsRepository.storeChain(chainList)
                        Resource.Success(chainList)
                    }
                    is Resource.Error -> Resource.Error(it.exception)
                }
            }
            .catch { Resource.Error(it) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun fetchBookings(id: Int) {
        bookingRepository.fetchBookings(id)
    }
}