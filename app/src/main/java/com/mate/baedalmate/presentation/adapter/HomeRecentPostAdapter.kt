package com.mate.baedalmate.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mate.baedalmate.data.datasource.remote.recruit.MainRecruitDto
import com.mate.baedalmate.databinding.ItemHomeBottomPostRecentBinding
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeRecentPostAdapter :
    ListAdapter<MainRecruitDto, HomeRecentPostAdapter.HomeRecentPostViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<MainRecruitDto>() {
            override fun areItemsTheSame(oldItem: MainRecruitDto, newItem: MainRecruitDto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MainRecruitDto,
                newItem: MainRecruitDto
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecentPostViewHolder =
        HomeRecentPostViewHolder(
            ItemHomeBottomPostRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: HomeRecentPostAdapter.HomeRecentPostViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<MainRecruitDto>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class HomeRecentPostViewHolder(private val binding: ItemHomeBottomPostRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: MainRecruitDto) {
            val decimalFormat = DecimalFormat("#,###")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val deadlineTimeString: String = post.deadlineDate
            var durationMinute = "0"

            if (post.deadlineDate != "") {
                val deadlineTime = LocalDateTime.parse(deadlineTimeString, formatter)
                val currentTime = LocalDateTime.now()
                val duration = Duration.between(currentTime, deadlineTime).toMinutes()
                durationMinute = duration.toString()
            }

            with(binding) {
                // TODO: 카테고리 이미지 설정
                tvHomeBottomPostRecentItemBottomTitle.text = "${post.place}"
                tvHomeBottomPostRecentItemTopPerson.text = "${post.currentPeople}/${post.minPeople}"
                tvHomeBottomPostRecentItemTopTime.text = "${durationMinute}분"
                tvHomeBottomPostRecentItemBottomDeliveryCurrent.text =
                    " ${decimalFormat.format(post.shippingFee)}원"
                tvHomeBottomPostRecentItemBottomMinCurrent.text =
                    " ${decimalFormat.format(post.minPeople)}원"
                tvHomeBottomPostRecentItemBottomUser.text = "${post.username} · ${post.dormitory}"
                tvHomeBottomPostRecentItemBottomUserStar.text = " ★ ${post.userScore}"
            }
        }
    }
}