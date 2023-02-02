package com.booking.tripsassignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booking.tripsassignment.data.BookingChain
import com.booking.tripsassignment.data.Chain
import com.booking.tripsassignment.data.ChainTitle
import com.booking.tripsassignment.databinding.TripCardItemLayoutBinding
import com.booking.tripsassignment.databinding.TripsHeaderItemLayoutBinding
import com.booking.tripsassignment.utils.ImageLoader

class BookingChainAdapter(private val list: List<Chain>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ViewType.BookingChain.type -> BookingChainViewHolder(
                TripCardItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> BookingChainTitleViewHolder(
                TripsHeaderItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is BookingChainViewHolder -> holder.bind(list[position] as BookingChain)
            is BookingChainTitleViewHolder -> holder.bind(list[position] as ChainTitle)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is ChainTitle) {
            ViewType.Title.type
        } else {
            ViewType.BookingChain.type
        }

    }

    class BookingChainViewHolder(
        private val binding: TripCardItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookingChain): Unit = with(binding) {
            cities.text = item.dateRange
            dates.text = item.dateRange
            nights.text = item.subTitle
            ImageLoader.loadImage(tripImage, item.imageUrl)
        }
    }

    class BookingChainTitleViewHolder(
        private val binding: TripsHeaderItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChainTitle): Unit = with(binding) {
            tripsHeader.text = item.title
        }
    }
}

enum class ViewType(val type: Int) {
    Title(1),
    BookingChain(2)
}