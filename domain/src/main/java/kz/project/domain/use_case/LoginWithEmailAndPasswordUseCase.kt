package kz.project.domain.use_case

import kz.project.domain.model.token.AccessToken
import kz.project.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * UseCase для авторизации посредствам логина и пароля
 */
class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(userName: String, password: String): Single<AccessToken> {
        return loginRepository.loginWithEmailAndPassword(userName, password)
    }
}