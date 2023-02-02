package com.booking.tripsassignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.usecase.BookingChainUseCase
import com.booking.tripsassignment.utils.NetworkError
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BookingChainViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: BookingChainViewModel

    private val bookingChainUseCase = mock<BookingChainUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `test live data should emit as same data as useCase flow `() = runTest(StandardTestDispatcher()) {
        val expectedFlow =
            flowOf(Resource.Loading, Resource.Success(listOf<Chain>()), Resource.Error(NetworkError()))

        val actualList = arrayListOf<Resource<List<Chain>>>()
        val expectedList = arrayListOf<Resource<List<Chain>>>()
        whenever(bookingChainUseCase.getBookingsChain()).thenReturn(expectedFlow)
        viewModel = BookingChainViewModel(bookingChainUseCase)

        viewModel.bookingChainsLiveData.observeForever {
            actualList.add(it)
        }

        delay(10)

        expectedFlow.toList(expectedList)
        expectedList.forEachIndexed { index, resource ->
            assert(resource == actualList[index])
        }
    }
}
