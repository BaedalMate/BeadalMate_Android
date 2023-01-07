package com.mate.baedalmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mate.baedalmate.domain.model.ApiResult
import com.mate.baedalmate.domain.model.RecruitList
import com.mate.baedalmate.domain.usecase.search.RequestGetSearchTagKeywordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val requestGetSearchTagKeywordUseCase: RequestGetSearchTagKeywordUseCase
) : ViewModel() {
    private val _searchKeywordResults = MutableLiveData(RecruitList(emptyList()))
    val searchKeywordResults: LiveData<RecruitList> get() = _searchKeywordResults

    fun requestSearchKeywordResult(
        keyword: String,
        page: Int = 0,
        size: Int = 100,
        sort: String
    ) = viewModelScope.launch {
        requestGetSearchTagKeywordUseCase(
            keyword = keyword,
            page = page,
            size = size,
            sort = sort
        ).let { ApiResponse ->
            when (ApiResponse.status) {
                ApiResult.Status.SUCCESS -> {
                    ApiResponse.data.let { _searchKeywordResults.postValue(it) }
                }
            }
        }
    }

    fun clearSearchKeywordResult() {
        _searchKeywordResults.postValue(RecruitList(emptyList()))
    }
}