package kz.project.data.repository

import android.content.SharedPreferences
import kz.project.domain.repository.AccessTokenRepository
import javax.inject.Inject

class AccessTokenRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AccessTokenRepository {

    /**
     * Сохраняет Authentication Token в shared prefs
     */
    override fun saveAuthToken(authToken: String, key: String) {
        sharedPreferences.edit().putString(key, authToken).apply()
    }

    /**
     * Сохраняет Refresh Token в shared prefs
     */
    override fun saveRefreshToken(refreshToken: String, key: String) {
        sharedPreferences.edit().putString(key, refreshToken).apply()
    }

    /**
     * Достает Authentication Token из shared prefs
     */
    override fun getAuthToken(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    /**
     * Достает Refresh Token из shared prefs
     */
    override fun getRefreshToken(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}