package com.booking.tripsassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.booking.tripsassignment.chaindetails.CHAIN_ID_KEY
import com.booking.tripsassignment.chaindetails.ChainDetailsActivity
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.databinding.ActivityMainScreenBinding
import com.booking.tripsassignment.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BookingChainAdapter.ClickListener {

    private val viewModel:BookingChainViewModel by viewModels()
    private lateinit var binding:ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.bookingChainsLiveData.observe(this){
            binding.progressBar.isVisible = it is Resource.Loading
            binding.message.isVisible = it is Resource.Error
            binding.recyclerview.isVisible = it is Resource.Success

            when(it){
                is Resource.Success->{
                    if(it.data()!!.isEmpty()){
                        binding.recyclerview.isVisible = false
                        binding.message.text="Travel Booking Not Found"
                        binding.message.isVisible = true
                    }else {
                        binding.recyclerview.adapter = BookingChainAdapter(it.data()!!, this)
                    }
                }
                is Resource.Error->{
                    binding.message.text = it.exception.message
                }
            }
        }
    }

    override fun onClick(chain: BookingChain) {
        val intent = Intent(this, ChainDetailsActivity::class.java)
        intent.putExtra(CHAIN_ID_KEY, chain.id)
        startActivity(intent)
    }
}