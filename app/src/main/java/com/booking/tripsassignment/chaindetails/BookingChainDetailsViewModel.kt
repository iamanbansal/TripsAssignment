package com.booking.tripsassignment.chaindetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.booking.tripsassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingChainDetailsViewModel @Inject constructor(
    private val repository: ChainDetailsRepository,
) : ViewModel() {

    val bookingChainsLiveData = repository.getBookingChainFlow()
        .map {
            when (it) {
                is Resource.Success -> BookingChainDetailsUiState(chain = it.data())
                is Resource.Error -> BookingChainDetailsUiState(error = it.exception.message)
            }
        }
        .onStart {
            BookingChainDetailsUiState(isLoading = true)
        }
        .asLiveData(viewModelScope.coroutineContext)

    fun fetchBookingChain(id: String) {
        viewModelScope.launch {
            repository.fetchBookings(id)
        }
    }
}