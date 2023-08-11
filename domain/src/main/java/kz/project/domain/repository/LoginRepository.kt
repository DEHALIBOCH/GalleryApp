package kz.project.domain.repository

import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.UserToPost
import kz.project.domain.model.user.UserToReceive
import io.reactivex.rxjava3.core.Single


interface LoginRepository {

    /**
     * Абстрактный метод для авторизации используя логин и пароль
     */
    fun loginWithEmailAndPassword(userName: String, password: String): Single<AccessToken>

    /**
     * Получает новый токен авторизации
     */
    fun refreshToken(refreshToken: String) : Single<AccessToken>

    /**
     * Регистрация пользователя
     */
    fun registerUser(user: UserToPost): Single<UserToReceive>
}