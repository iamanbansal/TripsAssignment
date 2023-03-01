package com.booking.tripsassignment

import com.booking.tripsassignment.data.Chain

data class BookingChainUiState(
    val isLoading: Boolean = false,
    val chains: List<Chain> = emptyList(),
    val error: String? = null,
)