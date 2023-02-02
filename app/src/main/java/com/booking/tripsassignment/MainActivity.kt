package com.booking.tripsassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.booking.tripsassignment.databinding.ActivityMainScreenBinding
import com.booking.tripsassignment.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel:BookingChainViewModel by viewModels()
    private lateinit var binding:ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.bookingChainsLiveData.observe(this){

            when(it){
                is Resource.Success->{
                    binding.recyclerview.adapter = BookingChainAdapter(it.data()!!)

                }
            }
        }
    }
}