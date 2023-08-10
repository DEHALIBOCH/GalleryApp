package kz.project.domain.use_case

import kz.project.domain.model.token.AccessToken
import kz.project.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * UseCase для рефреша AccessToken
 */
class RefreshTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(refreshToken: String): Single<AccessToken> = loginRepository.refreshToken(refreshToken)
}