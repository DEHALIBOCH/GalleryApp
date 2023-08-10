package kz.project.data.remote

import kz.project.data.remote.dto.user.UpdatePasswordForm
import kz.project.data.remote.dto.user.UserDto
import kz.project.data.remote.dto.user.UserToReceiveDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Api для получения и изменения информации о юзерах, текущем юзере и т.д.
 */
interface UserApi {

    @GET("/api/users/current")
    fun getCurrentUser(): Single<UserDto>

    @PUT("/api/users/update_password/{id}")
    fun updatePassword(@Path("id") id: Int, updatePasswordForm: UpdatePasswordForm): Single<UserToReceiveDto>
    @GET("/api/users/{id}")
    fun getUserById(@Path("id") id: Int): Single<UserDto>

    @DELETE("/api/users/{id}")
    fun deleteUser(): Completable


}