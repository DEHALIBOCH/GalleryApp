package kz.project.domain.use_case

import kz.project.domain.common.Constants
import kz.project.domain.repository.AccessTokenRepository
import javax.inject.Inject

/**
 * UseCase для получения Refresh Token из хранилища
 */
class GetRefreshTokenUseCase @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository
) {

    operator fun invoke(key: String = Constants.REFRESH_TOKEN_TAG): String {
        return accessTokenRepository.getRefreshToken(key)
    }
}