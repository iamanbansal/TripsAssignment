package com.booking.tripsassignment.usecase

import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.ChainTitle
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockDataGenerator
import com.booking.tripsassignment.repository.TestCase
import com.booking.tripsassignment.utils.NetworkError
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BookingChainUseCaseTest {

    private lateinit var useCase: BookingChainUseCase
    private val repository: BookingRepository = mock()

    @Before
    fun setUp() {
        useCase = BookingChainUseCaseImpl(mock(),repository, mock())
    }

    @Test
    fun `test getBookingsChain should return loading when flow send loading`() {

        whenever(repository.getBookingsFlow()).thenReturn(flowOf(Resource.Loading))
        runTest {
            useCase.getBookingsChain().collect {
                assert(it == Resource.Loading)
            }
        }
    }

    @Test
    fun `test getBookingsChain should return error when flow send error`() {

        val result = Resource.Error(NetworkError("network problem"))
        whenever(repository.getBookingsFlow()).thenReturn(flowOf(result))
        runTest {
            useCase.getBookingsChain().collect {
                assert(it == result)
            }
        }
    }


    @Test
    fun `test getBookingsChain verify booking chains`() {

        val list = MockDataGenerator.bookingsForUser(TestCase.PAST_CHAIN.bookerId)!!
        whenever(repository.getBookingsFlow()).thenReturn(flowOf(Resource.Success(list)))
        runTest {
            useCase.getBookingsChain().collect {
                assert(it is Resource.Success)
                val chains = it.data()!!
                assert(chains.size == 2)

                val title = chains.first() as ChainTitle
                assert(title.title == "Past Trips")
                val chain = chains[1] as BookingChain
                val last = chains.last() as BookingChain
                assert(chain.checkin == chain.checkin)
                assert(chain.checkout == last.checkout)
                assert(chain.title == "Trips to Milan, Florence and Amalfi")
                assert(chain.subTitle == "7 bookings")
                assert(chain.imageUrl == chain.list.first().hotel.mainPhoto)
            }
        }
    }


    @Test
    fun `test fetchBookings should call repository fetchBookings`() {
        runTest {
            useCase.fetchBookings(123)

            verify(repository).fetchBookings(123)
        }
    }
}