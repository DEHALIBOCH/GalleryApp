package kz.project.domain.repository

import io.reactivex.rxjava3.core.Single
import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.User
import kz.project.domain.model.user.UserToPost


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
    fun registerUser(user: UserToPost): Single<User>
}