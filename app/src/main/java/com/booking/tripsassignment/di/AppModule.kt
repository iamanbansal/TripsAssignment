package com.booking.tripsassignment.di

import android.content.Context
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.resource.ResourceManager
import com.booking.tripsassignment.resource.ResourceManagerImpl
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.usecase.BookingChainUseCaseImpl
import com.booking.tripsassignment.usecase.ChainCache
import com.booking.tripsassignment.usecase.InMemoryChainCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager = ResourceManagerImpl(context)

}