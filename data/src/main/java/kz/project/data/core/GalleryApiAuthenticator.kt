package kz.project.data.core

import android.util.Log
import kz.project.data.common.Constants
import kz.project.domain.use_case.GetRefreshTokenUseCase
import kz.project.domain.use_case.RefreshTokenUseCase
import kz.project.domain.use_case.SaveAccessTokenUseCase
import dagger.Lazy
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class GalleryApiAuthenticator @Inject constructor(
    private val refreshTokenUseCase: Lazy<RefreshTokenUseCase>,
    private val getRefreshTokenUseCase: Lazy<GetRefreshTokenUseCase>,
    private val saveAccessTokenUseCase: Lazy<SaveAccessTokenUseCase>,
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {

        val refreshToken = getRefreshTokenUseCase.get().invoke()

        return try {
            val newToken = refreshTokenUseCase.get().invoke(refreshToken)
                .subscribeOn(Schedulers.io())
                .blockingGet()

            saveAccessTokenUseCase.get().invoke(newToken)

            response.request.newBuilder()
                .removeHeader(Constants.AUTHORIZATION)
                .header(
                    Constants.AUTHORIZATION,
                    "${Constants.BEARER} ${newToken.accessToken}"
                ).build()

        } catch (e: Exception) {
            Log.e(Constants.AUTHORIZATION, e.message.toString())
            null
        }
    }
}