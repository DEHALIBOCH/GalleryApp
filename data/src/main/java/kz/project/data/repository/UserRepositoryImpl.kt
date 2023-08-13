package kz.project.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kz.project.data.mappers.toUser
import kz.project.data.remote.UserApi
import kz.project.data.remote.dto.error.parser.ErrorParser
import kz.project.domain.model.user.UpdatePasswordForm
import kz.project.domain.model.user.User
import kz.project.domain.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val errorParser: ErrorParser
) : UserRepository {

    override fun getCurrentUser(): Single<User> = userApi.getCurrentUser().flatMap {
        Single.just(it.toUser())
    }

    override fun updatePassword(id: Int, updatePasswordForm: UpdatePasswordForm) =
        userApi.updatePassword(
            id = id,
            updatePasswordForm = updatePasswordForm,
        ).onErrorResumeNext { throwable ->
            val errorBody = (throwable as HttpException).response()?.errorBody()?.string()
            Single.error(errorParser.parsePasswordUpdateError(errorBody))
        }.flatMap { userToReceiveDto ->
            Single.just(userToReceiveDto.toUser())
        }

    override fun getUserById(id: Int) = userApi.getUserById(id).flatMap { userDto ->
        Single.just(userDto.toUser())
    }

    override fun deleteUser(currUserId: Int): Completable = userApi.deleteUser(currUserId)
}