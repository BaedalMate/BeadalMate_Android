package com.mate.baedalmate.domain.usecase.member

import com.mate.baedalmate.domain.repository.MemberRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestGetResignUserUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    suspend operator fun invoke() = memberRepository.requestGetResignUser()
}