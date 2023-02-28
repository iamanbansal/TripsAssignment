package com.booking.tripsassignment.usecase

import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface ChainCache {
    fun storeChain(chains: List<Chain>)
    suspend fun getChain(id: String):BookingChain?
}

class InMemoryChainCache : ChainCache {
    lateinit var list:List<Chain>

    override fun storeChain(chains: List<Chain>) {
        list = chains
    }

    override suspend fun getChain(id: String): BookingChain? {

        list.forEach {
            if(it is BookingChain && it.id == id){
                return it
            }
        }
        return null
    }

}