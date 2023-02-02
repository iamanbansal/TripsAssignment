package com.booking.tripsassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.booking.tripsassignment.usecase.BookingChainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingChainViewModel @Inject constructor(
    private val useCase: BookingChainUseCase,
) : ViewModel() {

    val bookingChainsLiveData = useCase.getBookingsChain().asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            useCase.fetchBookings()
        }
    }


}