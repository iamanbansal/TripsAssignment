package com.booking.tripsassignment.di

import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.usecase.BookingChainUseCaseImpl
import com.booking.tripsassignment.usecase.ChainCache
import com.booking.tripsassignment.usecase.InMemoryChainCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


const val CHAIN_CACHE = "chain_cache"

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideInMemoryCache(): ChainCache = InMemoryChainCache()

}