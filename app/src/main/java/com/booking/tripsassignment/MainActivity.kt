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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BookingChainAdapter.ClickListener {

    private val viewModel: BookingChainViewModel by viewModels()
    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.bookingChainsLiveData.observe(this) {
            binding.progressBar.isVisible = it.isLoading

            binding.message.apply {
                isVisible = !it.error.isNullOrBlank()

                if (it.error.isNullOrBlank().not()) {
                    text = it.error
                }
            }

            binding.recyclerview.apply {
                isVisible = it.chains.isNotEmpty()
                if (it.chains.isNotEmpty()) {
                    adapter = BookingChainAdapter(it.chains, this@MainActivity)
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