package com.booking.tripsassignment.chaindetails

import android.content.res.Resources.NotFoundException
import android.util.Log
import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.di.CHAIN_CACHE
import com.booking.tripsassignment.usecase.ChainCache
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

interface ChainDetailsRepository {

    suspend fun fetchBookings(chainId: String)

    fun getBookingChainFlow(): Flow<Resource<BookingChain>>

    fun storeChain(chains: List<Chain>)
}


class ChainDetailsRepositoryImp @Inject constructor(
    private val cache: ChainCache
) : ChainDetailsRepository {

    private val chainFlow = MutableSharedFlow<Resource<BookingChain>>()

    override suspend fun fetchBookings(chainId: String) = withContext(Dispatchers.Default) {
        val chain = cache.getChain(chainId)
        if (chain == null) {
            chainFlow.emit(Resource.Error(NotFoundException("Booking Chain Not Found")))
        } else {
            chainFlow.emit(Resource.Success(chain))
        }
    }

    override fun getBookingChainFlow(): Flow<Resource<BookingChain>> {
        return chainFlow.asSharedFlow()
    }

    override fun storeChain(chains: List<Chain>) {
        cache.storeChain(chains)
    }

}