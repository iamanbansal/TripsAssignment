package com.booking.tripsassignment.di

import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.usecase.BookingChainUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BookingChainModule {

    @Provides
    @JvmStatic
    fun bindBookingRepository(): BookingRepository = MockNetworkBookingRepository()

    @Provides
    @JvmStatic
    fun provideBookingChainUseCase(repository: BookingRepository):BookingChainUseCase{
        return BookingChainUseCaseImpl(repository)
    }

}