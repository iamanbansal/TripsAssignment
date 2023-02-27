package com.booking.tripsassignment.di

import com.booking.tripsassignment.chaindetails.ChainDetailsRepository
import com.booking.tripsassignment.chaindetails.ChainDetailsRepositoryImp
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.resource.ResourceManager
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.usecase.BookingChainUseCaseImpl
import com.booking.tripsassignment.usecase.ChainCache
import com.booking.tripsassignment.usecase.InMemoryChainCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named


@Module
@InstallIn(ViewModelComponent::class)
object BookingChainModule {

    @Provides
    @JvmStatic
    fun bindBookingRepository(): BookingRepository = MockNetworkBookingRepository()

    @Provides
    @JvmStatic
    fun provideBookingChainUseCase(
        repository: BookingRepository,
        chainDetailsRepository: ChainDetailsRepository,
        resourceManager: ResourceManager
    ): BookingChainUseCase {
        return BookingChainUseCaseImpl(resourceManager, repository, chainDetailsRepository)
    }


    @Provides
    @JvmStatic
    fun bindChainDetailsRepository(chainCache: ChainCache): ChainDetailsRepository =
        ChainDetailsRepositoryImp(chainCache)

}