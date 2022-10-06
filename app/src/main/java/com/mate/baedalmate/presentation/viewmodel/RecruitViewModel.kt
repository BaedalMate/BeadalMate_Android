package com.mate.baedalmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mate.baedalmate.data.datasource.remote.recruit.MainRecruitDto
import com.mate.baedalmate.data.datasource.remote.recruit.MainRecruitList
import com.mate.baedalmate.data.datasource.remote.recruit.TagRecruitList
import com.mate.baedalmate.domain.model.RecruitList
import com.mate.baedalmate.domain.usecase.recruit.RequestRecruitListUseCase
import com.mate.baedalmate.domain.usecase.recruit.RequestRecruitMainListUseCase
import com.mate.baedalmate.domain.usecase.recruit.RequestRecruitTagListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecruitViewModel @Inject constructor(
    private val recruitListUseCase: RequestRecruitListUseCase,
    private val recruitMainListUseCase: RequestRecruitMainListUseCase,
    private val recruitTagListUseCase: RequestRecruitTagListUseCase
) : ViewModel() {
    private val _recruitListAll = MutableLiveData(RecruitList(emptyList()))
    val recruitListAll: LiveData<RecruitList> get() = _recruitListAll
    private val _recruitListKorean = MutableLiveData(RecruitList(emptyList()))
    val recruitListKorean: LiveData<RecruitList> get() = _recruitListKorean
    private val _recruitListChinese = MutableLiveData(RecruitList(emptyList()))
    val recruitListChinese: LiveData<RecruitList> get() = _recruitListChinese
    private val _recruitListJapanese = MutableLiveData(RecruitList(emptyList()))
    val recruitListJapanese: LiveData<RecruitList> get() = _recruitListJapanese
    private val _recruitListWestern = MutableLiveData(RecruitList(emptyList()))
    val recruitListWestern: LiveData<RecruitList> get() = _recruitListWestern
    private val _recruitListFastfood = MutableLiveData(RecruitList(emptyList()))
    val recruitListFastfood: LiveData<RecruitList> get() = _recruitListFastfood
    private val _recruitListBunsik = MutableLiveData(RecruitList(emptyList()))
    val recruitListBunsik: LiveData<RecruitList> get() = _recruitListBunsik
    private val _recruitListDessert = MutableLiveData(RecruitList(emptyList()))
    val recruitListDessert: LiveData<RecruitList> get() = _recruitListDessert
    private val _recruitListChicken = MutableLiveData(RecruitList(emptyList()))
    val recruitListChicken: LiveData<RecruitList> get() = _recruitListChicken
    private val _recruitListPizza = MutableLiveData(RecruitList(emptyList()))
    val recruitListPizza: LiveData<RecruitList> get() = _recruitListPizza
    private val _recruitListAsia = MutableLiveData(RecruitList(emptyList()))
    val recruitListAsia: LiveData<RecruitList> get() = _recruitListAsia
    private val _recruitListPackedmeal = MutableLiveData(RecruitList(emptyList()))
    val recruitListPackedmeal: LiveData<RecruitList> get() = _recruitListPackedmeal

    private val _isRecruitListLoad = MutableLiveData(false)
    val isRecruitListLoad: LiveData<Boolean> get() = _isRecruitListLoad

    private val _recruitHomeRecentList = MutableLiveData(
        MainRecruitList(
            listOf(
                MainRecruitDto(
                    "", 0, "", "", 0,
                    "", 0, "", 0, 0f, ""
                )
            )
        )
    )
    val recruitHomeRecentList: LiveData<MainRecruitList> get() = _recruitHomeRecentList

    private val _recruitHomeRecommendList = MutableLiveData(
        MainRecruitList(
            listOf(
                MainRecruitDto(
                    "", 0, "", "", 0,
                    "", 0, "", 0, 0f, ""
                )
            )
        )
    )
    val recruitHomeRecommendList: LiveData<MainRecruitList> get() = _recruitHomeRecommendList

    private val _isRecruitMainListLoad = MutableLiveData(false)
    val isRecruitMainListLoad: LiveData<Boolean> get() = _isRecruitMainListLoad

    private val _recruitHomeTagList = MutableLiveData(TagRecruitList(emptyList()))
    val recruitHomeTagList: LiveData<TagRecruitList> get() = _recruitHomeTagList

    private val _isRecruitTagListLoad = MutableLiveData(false)
    val isRecruitTagListLoad: LiveData<Boolean> get() = _isRecruitTagListLoad

    fun requestCategoryRecruitList(
        categoryId: Int? = null,
        page: Int = 0,
        size: Int = 4,
        sort: String
    ) =
        viewModelScope.launch {
            val response = recruitListUseCase.invoke(
                categoryId = categoryId,
                page = page,
                size = size,
                sort = sort
            )
            if (response.isSuccessful) {
                when (categoryId) {
                    null -> {
                        _recruitListAll.postValue(response.body())
                    }
                    1 -> {
                        _recruitListKorean.postValue(response.body())
                    }
                    2 -> {
                        _recruitListChinese.postValue(response.body())
                    }
                    3 -> {
                        _recruitListJapanese.postValue(response.body())
                    }
                    4 -> {
                        _recruitListWestern.postValue(response.body())
                    }
                    5 -> {
                        _recruitListFastfood.postValue(response.body())
                    }
                    6 -> {
                        _recruitListBunsik.postValue(response.body())
                    }
                    7 -> {
                        _recruitListDessert.postValue(response.body())
                    }
                    8 -> {
                        _recruitListChicken.postValue(response.body())
                    }
                    9 -> {
                        _recruitListPizza.postValue(response.body())
                    }
                    10 -> {
                        _recruitListAsia.postValue(response.body())
                    }
                    11 -> {
                        _recruitListPackedmeal.postValue(response.body())
                    }
                    else -> {
                        _recruitListAll.postValue(response.body())
                    }
                }
                _isRecruitListLoad.postValue(true)
            } else {
                _isRecruitListLoad.postValue(false)
            }
        }

    fun requestHomeRecruitRecentList(page: Int = 0, size: Int = 4, sort: String) =
        viewModelScope.launch {
            val response = recruitMainListUseCase.invoke(page = page, size = size, sort = sort)
            if (response.isSuccessful) {
                _recruitHomeRecentList.postValue(response.body())
            } else {
            }
        }

    fun requestHomeRecruitRecommendList(page: Int = 0, size: Int = 4, sort: String) =
        viewModelScope.launch {
            val response = recruitMainListUseCase.invoke(page = page, size = size, sort = sort)
            if (response.isSuccessful) {
                _recruitHomeRecommendList.postValue(response.body())
            } else {
            }
        }

    fun requestHomeRecruitTagList(page: Int = 0, size: Int = 4, sort: String) =
        viewModelScope.launch {
            val response = recruitTagListUseCase.invoke(page = page, size = size, sort = sort)
            if (response.isSuccessful) {
                _recruitHomeTagList.postValue(response.body())
                _isRecruitTagListLoad.postValue(true)
            } else {
                _isRecruitTagListLoad.postValue(false)
            }
        }
}