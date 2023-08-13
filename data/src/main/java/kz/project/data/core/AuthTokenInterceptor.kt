package kz.project.data.core

import kz.project.data.common.Constants
import kz.project.domain.use_case.token.GetAuthenticationTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Добавляет Header с авторизационным токеном к запросам
 */
class AuthTokenInterceptor @Inject constructor(
    private val getAuthenticationTokenUseCase: GetAuthenticationTokenUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = getAuthenticationTokenUseCase.invoke()

        val newRequest = chain.request()
            .newBuilder()
            .header(
                Constants.AUTHORIZATION,
                "${Constants.BEARER} $authToken"
            )
            .build()

        return chain.proceed(newRequest)
    }
}