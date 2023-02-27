package com.booking.tripsassignment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingChainViewModel @Inject constructor(
    private val useCase: BookingChainUseCase,
) : ViewModel() {

    val bookingChainsLiveData = useCase.getBookingsChain()
        .onStart {
            emit(Resource.Loading)
        }
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            useCase.fetchBookings()
        }
    }
}