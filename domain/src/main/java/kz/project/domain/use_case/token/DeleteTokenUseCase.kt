package kz.project.domain.use_case.token

import kz.project.domain.repository.AccessTokenRepository
import javax.inject.Inject

class DeleteTokenUseCase @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository
) {

    operator fun invoke() = accessTokenRepository.deleteToken()
}