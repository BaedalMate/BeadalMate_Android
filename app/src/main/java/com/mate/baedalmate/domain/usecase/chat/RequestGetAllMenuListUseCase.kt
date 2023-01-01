package com.mate.baedalmate.domain.usecase.chat

import com.mate.baedalmate.domain.repository.ChatRepository
import javax.inject.Inject

class RequestGetAllMenuListUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(id: Int) = chatRepository.requestGetAllMenuList(id = id)
}