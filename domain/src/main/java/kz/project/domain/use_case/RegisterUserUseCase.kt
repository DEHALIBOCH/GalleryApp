package kz.project.domain.use_case

import kz.project.domain.model.user.UserToPost
import kz.project.domain.model.user.UserToReceive
import kz.project.domain.repository.LoginRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * UseCase для регистрации пользователя
 */
class RegisterUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(user: UserToPost): Single<UserToReceive> {
        return loginRepository.registerUser(user)
    }
}