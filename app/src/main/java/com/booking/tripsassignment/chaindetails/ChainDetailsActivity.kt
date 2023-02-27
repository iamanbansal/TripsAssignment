package com.booking.tripsassignment.chaindetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.booking.tripsassignment.R
import dagger.hilt.android.AndroidEntryPoint

const val CHAIN_ID_KEY = "chain_id_key"

@AndroidEntryPoint
class ChainDetailsActivity : AppCompatActivity() {

    private val viewModel: BookingChainDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chain_details)

        if(intent.extras !=null && intent.extras!!.containsKey(CHAIN_ID_KEY)){
            val id = intent.extras!!.getString(CHAIN_ID_KEY)!!
            viewModel.fetchBookingChain(id)
        }

        viewModel.bookingChainsLiveData.observe(this){
            Log.d("***ChainDetailsActivity***", "onCreate() called $it")
        }
    }
}