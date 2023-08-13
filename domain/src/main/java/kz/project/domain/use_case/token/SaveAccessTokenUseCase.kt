package kz.project.domain.use_case.token

import kz.project.domain.common.Constants
import kz.project.domain.model.token.AccessToken
import kz.project.domain.repository.AccessTokenRepository
import javax.inject.Inject

/**
 * UseCase для сохранения AccessToken в хранилище
 */
class SaveAccessTokenUseCase @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository
) {

    operator fun invoke(
        accessToken: AccessToken,
        authTokenKey: String = Constants.AUTH_TOKEN_TAG,
        refreshTokenKey: String = Constants.REFRESH_TOKEN_TAG
    ) {
        accessTokenRepository.apply {
            saveAuthToken(
                authToken = accessToken.accessToken,
                key = authTokenKey
            )
            saveRefreshToken(
                refreshToken = accessToken.refreshToken,
                key = refreshTokenKey
            )
        }
    }
}