package kz.project.domain.repository

import kz.project.domain.model.token.AccessToken

/**
 * Интерфейс репозитория для сохранения [AccessToken] в какое-нибудь хранилище
 */
interface AccessTokenRepository {

    /**
     * Сохраняет Auth Token
     */
    fun saveAuthToken(authToken: String, key: String)

    /**
     * Сохраняет Refresh Token
     */
    fun saveRefreshToken(refreshToken: String, key: String)


    /**
     * Достает Auth Token
     */
    fun getAuthToken(key: String): String

    /**
     * Достает Refresh Token
     */
    fun getRefreshToken(key: String): String

}