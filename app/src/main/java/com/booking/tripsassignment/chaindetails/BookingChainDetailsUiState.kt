package com.booking.tripsassignment.chaindetails

import com.booking.tripsassignment.data.BookingChain

data class BookingChainDetailsUiState(
    val isLoading: Boolean = false,
    val chain: BookingChain? = null,
    val error: String? = null,
)