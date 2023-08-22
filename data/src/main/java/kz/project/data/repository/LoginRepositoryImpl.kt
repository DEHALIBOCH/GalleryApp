package kz.project.data.repository

import io.reactivex.rxjava3.core.Single
import kz.project.data.mappers.toAccessToken
import kz.project.data.mappers.toUser
import kz.project.data.mappers.toUserToPostDto
import kz.project.data.remote.LoginApi
import kz.project.data.remote.dto.error.parser.ErrorParser
import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.User
import kz.project.domain.model.user.UserToPost
import kz.project.domain.repository.LoginRepository
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val errorParser: ErrorParser
) : LoginRepository {


    override fun loginWithEmailAndPassword(
        userName: String,
        password: String
    ): Single<AccessToken> {
        return loginApi.loginWithEmailAndPassword(
            userName = userName,
            password = password,
        ).onErrorResumeNext { throwable ->
            val errorBody = (throwable as HttpException).response()?.errorBody()?.string()
            Single.error(errorParser.parseErrorMessage(errorBody))
        }.flatMap { response ->
            Single.just(response.toAccessToken())
        }
    }

    override fun registerUser(user: UserToPost): Single<User> {
        return loginApi.registerUser(user.toUserToPostDto())
            .onErrorResumeNext { throwable ->
                val errorBody = (throwable as HttpException).response()?.errorBody()?.string()
                Single.error(errorParser.parseRegistrationErrorMessage(errorBody))
            }.flatMap { response ->
                Single.just(response.toUser())
            }
    }

    override fun refreshToken(refreshToken: String): Single<AccessToken> {
        return loginApi.refreshToken(refreshToken = refreshToken)
            .onErrorResumeNext { throwable ->
                val errorBody = (throwable as HttpException).response()?.errorBody()?.string()
                Single.error(errorParser.parseErrorMessage(errorBody))
            }.flatMap { response ->
                Single.just(response.toAccessToken())
            }
    }
}