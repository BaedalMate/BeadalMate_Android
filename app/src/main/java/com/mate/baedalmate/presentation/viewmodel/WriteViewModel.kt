package com.mate.baedalmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mate.baedalmate.BuildConfig
import com.mate.baedalmate.common.ListLiveData
import com.mate.baedalmate.data.datasource.remote.recruit.DeliveryPlatform
import com.mate.baedalmate.data.datasource.remote.recruit.Dormitory
import com.mate.baedalmate.data.datasource.remote.recruit.MenuDto
import com.mate.baedalmate.data.datasource.remote.recruit.PlaceDto
import com.mate.baedalmate.data.datasource.remote.recruit.RecruitFinishCriteria
import com.mate.baedalmate.data.datasource.remote.recruit.ShippingFeeDto
import com.mate.baedalmate.data.datasource.remote.recruit.TagDto
import com.mate.baedalmate.data.datasource.remote.write.PlaceMeta
import com.mate.baedalmate.data.datasource.remote.write.RegionInfo
import com.mate.baedalmate.data.datasource.remote.write.ResultSearchKeyword
import com.mate.baedalmate.domain.usecase.write.RequestKakaoLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(private val kakaoLocalUseCase: RequestKakaoLocalUseCase) :
    ViewModel() {
    var categoryId = 0

    var deadLinePeopleCount = 0
    var deadLineAmount = 0
    var deadLineTime = "2020-12-24T16:28:27" // TODO 수정 필요
    var deadLineCriterion = RecruitFinishCriteria.NUMBER
    var isDeliveryFeeFree = false
    var deliveryFeeRangeList = mutableListOf<ShippingFeeDto>()

    var deliveryDormitory = Dormitory.NURI
    var deliveryStore = MutableLiveData(PlaceDto("", "", "", 0f, 0f))
    var deliveryPlatform = DeliveryPlatform.BAEMIN
    var isCouponUse = false
    var couponAmount = 0

    var postTitle = ""
    var postDetail = ""
    var postTagList = mutableListOf<TagDto>()

    var menuList = mutableListOf<MenuDto>()
    var postMenuList = ListLiveData<List<String>>()
}