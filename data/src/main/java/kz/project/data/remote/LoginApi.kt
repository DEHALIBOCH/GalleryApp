package kz.project.data.remote

import kz.project.data.common.Constants
import kz.project.data.remote.dto.token.AccessTokenDto
import kz.project.data.remote.dto.user.UserToPostDto
import kz.project.domain.model.user.UserToReceive
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Api используемое для авторизации и регистрации
 */
interface LoginApi {

    @GET("/oauth/v2/token")
    fun loginWithEmailAndPassword(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("grant_type") grantType: String = "password",
        @Query("username") userName: String,
        @Query("password") password: String,
        @Query("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
    ): Single<AccessTokenDto>

    @GET("/oauth/v2/token")
    fun refreshToken(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("grant_type") grantType: String = "refresh_token",
        @Query("refresh_token") refreshToken: String,
        @Query("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
    ): Single<AccessTokenDto>


    @POST("/api/users")
    fun registerUser(@Body user: UserToPostDto): Single<UserToReceive> //TODO протестить
}