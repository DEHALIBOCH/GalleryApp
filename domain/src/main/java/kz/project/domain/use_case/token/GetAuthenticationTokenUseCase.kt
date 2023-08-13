package kz.project.domain.use_case.token

import kz.project.domain.common.Constants
import kz.project.domain.repository.AccessTokenRepository
import javax.inject.Inject

/**
 * UseCase для получения Authentication Token из хранилища
 */
class GetAuthenticationTokenUseCase @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository
) {

    operator fun invoke(key: String = Constants.AUTH_TOKEN_TAG) : String {
        return accessTokenRepository.getAuthToken(key)
    }
}