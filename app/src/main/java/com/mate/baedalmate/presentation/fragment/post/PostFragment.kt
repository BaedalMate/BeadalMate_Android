package com.mate.baedalmate.presentation.fragment.post

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.mate.baedalmate.R
import com.mate.baedalmate.common.GetDeviceSize
import com.mate.baedalmate.common.autoCleared
import com.mate.baedalmate.common.dp
import com.mate.baedalmate.common.extension.setOnDebounceClickListener
import com.mate.baedalmate.databinding.FragmentPostBinding
import com.mate.baedalmate.domain.model.DeliveryPlatform
import com.mate.baedalmate.domain.model.PlaceDto
import com.mate.baedalmate.domain.model.RecruitDetail
import com.mate.baedalmate.domain.model.ShippingFeeDto
import com.mate.baedalmate.presentation.viewmodel.RecruitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PostFragment : Fragment() {
    private var binding by autoCleared<FragmentPostBinding>()
    private val args by navArgs<PostFragmentArgs>()
    private val recruitViewModel by activityViewModels<RecruitViewModel>()
    private lateinit var glideRequestManager: RequestManager
    private lateinit var starIndicator: Array<ImageView?>
    private var deliveryFeeList = listOf<ShippingFeeDto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        glideRequestManager = Glide.with(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPostDetailData()
        setBackClickListener()
        initContents()
        observeCloseRecruitPostState()
        observeCancelRecruitPostState()
    }

    private fun getPostDetailData() {
        recruitViewModel.requestRecruitPost(postId = args.postId)
    }

    private fun setBackClickListener() {
        binding.btnPostActionbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun displayPostPlatform(platformName: String) {
        val platformImageDrawableId = when (platformName) {
            DeliveryPlatform.BAEMIN.name -> R.drawable.ic_baemin_circle
            DeliveryPlatform.COUPANG.name -> R.drawable.ic_coupang_circle
            DeliveryPlatform.DDGNGYO.name -> R.drawable.ic_ddangyo_circle
            DeliveryPlatform.YOGIYO.name -> R.drawable.ic_yogiyo_circle
            else -> R.drawable.ic_etc_circle
        }
        binding.imgPostBackDeliveryPlatform.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                platformImageDrawableId
            )
        )
    }

    private fun setStoreIconClickListener(place: PlaceDto) {
        binding.imgPostBackStore.setOnClickListener {
            with(place) {
                findNavController().navigate(
                    com.mate.baedalmate.presentation.fragment.post.PostFragmentDirections.actionPostFragmentToPostMapFragment(
                        name = this.name,
                        addressName = this.addressName,
                        roadAddressName = this.roadAddressName,
                        x = this.x,
                        y = this.y
                    )
                )
            }
        }
    }

    private fun displayUserScore(userScore: Float) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(3, 0, 0, 0)

        binding.layoutPostFrontUserScore.removeAllViews()
        for (i in starIndicator.indices) {
            starIndicator[i] = ImageView(requireContext())
            if (i <= userScore) {
                starIndicator[i]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_star_full
                    )
                )
            } else if (i > userScore && (i - userScore) != 0f && (i - userScore) < 1) {
                starIndicator[i]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_star_half
                    )
                )
            } else {
                starIndicator[i]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_star_empty
                    )
                )
            }
            starIndicator[i]!!.layoutParams = params
            binding.layoutPostFrontUserScore.addView(starIndicator[i])
        }

        val paramsTextView = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        paramsTextView.setMargins(5.dp, 0, 0, 0)
        val scoreTextView = TextView(requireContext())
        scoreTextView.apply {
            setTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.applesdgothic_neo_bold
                ), Typeface.NORMAL
            )
            text = "$userScore"
            layoutParams = paramsTextView
            gravity = Gravity.CENTER
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.main_FB5F1C
                )
            )

            binding.layoutPostFrontUserScore.addView(this)
        }
    }

    private fun observeCloseRecruitPostState() {
        recruitViewModel.isCloseRecruitPostSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess.getContentIfNotHandled() == true) {
                Toast.makeText(requireContext(), getString(R.string.post_alert_message_close), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun observeCancelRecruitPostState() {
        recruitViewModel.isCancelParticipateRecruitPostSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess.getContentIfNotHandled() == true) {
                Toast.makeText(requireContext(), getString(R.string.post_alert_message_cancel_participate), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun initContentsUserInfo(recruitDetail: RecruitDetail) {
        with(binding) {
            glideRequestManager.load("http://3.35.27.107:8080/images/${recruitDetail.profileImage}")
                .override(45.dp)
                .thumbnail(0.1f)
                .priority(Priority.HIGH)
                .centerCrop()
                .into(imgPostFrontUser)
            displayUserScore(recruitDetail.score)

            tvPostFrontUserName.text = recruitDetail.username
            tvPostFrontUserDormitory.text = recruitDetail.userDormitory
        }
    }

    private fun setDeadlineTime(deadlineDate: String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val deadlineTimeString: String = deadlineDate
        if (deadlineDate.isNotEmpty()) {
            val deadlineTime = LocalDateTime.parse(deadlineTimeString, formatter)
            binding.tvPostFrontContentsDeadlineTime.text =
                "${deadlineTime.hour}시 ${deadlineTime.minute}분"
        }
    }

    private fun setDeliveryFeeDetail(
        shippingFee: Int,
        shippingFeeDetail: List<ShippingFeeDto>,
        coupon: Int
    ) {
        val decimalFormat = DecimalFormat("#,###")
        binding.tvPostFrontContentsDeadlineDeliveryFee.text =
            "${decimalFormat.format(shippingFee)}원"

        deliveryFeeList = shippingFeeDetail
        binding.btnPostFrontContentsDeadlineDeliveryFeeHelp.setOnClickListener {
            findNavController().navigate(
                PostFragmentDirections.actionPostFragmentToPostDeliveryFeeHelpFragment(
                    deliveryFeeList = deliveryFeeList.toTypedArray(),
                    couponAmount = coupon
                )
            )
        }
    }

    private fun setDeadlinePeopleCount(minPeople: Int, currentPeople: Int) {
        binding.tvPostFrontContentsDeadlinePeople.text =
            "${currentPeople}명 / ${minPeople}명"
    }

    private fun initContentsPostInfo(recruitDetail: RecruitDetail) {
        binding.tvPostFrontContentsTitle.text = recruitDetail.title
        setDeadlineTime(recruitDetail.deadlineDate)
        setDeliveryFeeDetail(
            recruitDetail.shippingFee,
            recruitDetail.shippingFeeDetail,
            recruitDetail.coupon
        )
        setDeadlinePeopleCount(
            minPeople = recruitDetail.minPeople,
            currentPeople = recruitDetail.currentPeople
        )
        binding.tvPostFrontContentsDormitory.text = recruitDetail.dormitory
        binding.tvPostFrontContentsDetail.text = recruitDetail.description
    }

    private fun setRecruitActionButton(recruitDetail: RecruitDetail) {
        val isCurrentUserHost = recruitDetail.host
        val isCurrentUserParticipant = recruitDetail.participate

        if (!recruitDetail.active) { // 주최자,참여자에 관계없이 마감된 모집글인 경우
            with(binding) {
                layoutPostFrontContentsParticipateHost.visibility = View.GONE
                with(btnPostFrontContentsParticipateGuest) {
                    isEnabled = false
                    text = getString(R.string.post_not_active)
                }
            }
        } else if(isCurrentUserHost) { // 주최자이면서 진행중인 모집글인 경우
            with(binding) {
                layoutPostFrontContentsParticipateHost.visibility = View.VISIBLE
                btnPostFrontContentsParticipateGuest.isEnabled = false

                with(btnPostFrontContentsParticipateHostCancel) {
                    isEnabled = true
                    setOnClickListener { recruitViewModel.requestCancelRecruitPost(postId = args.postId) }
                }
                with(btnPostFrontContentsParticipateHostClose) {
                    isEnabled = true
                    setOnClickListener { recruitViewModel.requestCloseRecruitPost(postId = args.postId) }
                }
            }

        } else { // 참여자이면서 진행중인 모집글인 경우
            binding.layoutPostFrontContentsParticipateHost.visibility = View.GONE
            binding.btnPostFrontContentsParticipateHostCancel.isEnabled = false
            binding.btnPostFrontContentsParticipateHostClose.isEnabled = false

            with(binding.btnPostFrontContentsParticipateGuest) {
                isEnabled = true
                text = getString(R.string.post_participate_in)
                if (isCurrentUserParticipant) {
                    text = getString(R.string.post_participate_out)
                    setOnClickListener { recruitViewModel.requestCancelParticipateRecruitPost(postId = args.postId) }
                } else {
                    text = getString(R.string.post_participate_in)
                    setOnClickListener {
                        findNavController().navigate(
                            PostFragmentDirections.actionPostFragmentToPostMenuBottomSheetDialogFragment(
                                recruitId = args.postId
                            )
                        )
                    }
                }
            }
        }
    }

    private fun initContents() {
        with(binding) {
            starIndicator = arrayOfNulls<ImageView>(5)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    recruitViewModel.recruitPostDetail.observe(viewLifecycleOwner) { recruitDetail ->
                        glideRequestManager.load("http://3.35.27.107:8080/images/${recruitDetail.image}")
                            .override(GetDeviceSize.getDeviceWidthSize(requireContext()))
                            .thumbnail(0.1f)
                            .priority(Priority.HIGH)
                            .centerCrop()
                            .into(imgPostBack)

                        displayPostPlatform(recruitDetail.platform)
                        setStoreIconClickListener(recruitDetail.place)
                        initContentsUserInfo(recruitDetail)
                        initContentsPostInfo(recruitDetail)
                        setRecruitActionButton(recruitDetail)
                        setReportPostClickListener(recruitDetail)
                        setModifyPostClickListener(recruitDetail)
                    }
                }
            }
        }
    }

    private fun setReportPostClickListener(recruitDetail: RecruitDetail) {
        with(binding.tvPostFrontUserReport) {
            val span = SpannableString(this.text)
            span.setSpan(UnderlineSpan(), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.text = span

            visibility = if (recruitDetail.host) View.GONE else View.VISIBLE
            setOnDebounceClickListener {
                findNavController().navigate(PostFragmentDirections.actionPostFragmentToReportPostFragment(
                    postId = args.postId,
                    postWriterName = recruitDetail.username,
//                    postWriterUserId = recruitDetail.id // TODO API 수정시 수정
                ))
            }
        }
    }

    private fun setModifyPostClickListener(recruitDetail: RecruitDetail) {
        with(binding.tvPostFrontContentsDetailModify) {
            visibility = if (recruitDetail.host) View.VISIBLE else View.GONE
            setOnDebounceClickListener {
                // TODO 수정하기 구현 추가시 Navigate 추가
            }
        }
    }
}