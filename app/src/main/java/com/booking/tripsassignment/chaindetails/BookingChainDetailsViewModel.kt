package com.booking.tripsassignment.chaindetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingChainDetailsViewModel @Inject constructor(
    private val repository: ChainDetailsRepository,
) : ViewModel() {

    val bookingChainsLiveData = repository.getBookingChainFlow()
        .onStart {
            emit(Resource.Loading)
        }
        .asLiveData(viewModelScope.coroutineContext)

    fun fetchBookingChain(id: String) {
        viewModelScope.launch {
            repository.fetchBookings(id)
        }
    }
}