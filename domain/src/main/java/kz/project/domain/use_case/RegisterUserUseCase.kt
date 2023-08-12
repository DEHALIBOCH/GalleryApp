package kz.project.domain.use_case

import io.reactivex.rxjava3.core.Single
import kz.project.domain.model.user.User
import kz.project.domain.model.user.UserToPost
import kz.project.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * UseCase для регистрации пользователя
 */
class RegisterUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(user: UserToPost): Single<User> {
        return loginRepository.registerUser(user)
    }
}