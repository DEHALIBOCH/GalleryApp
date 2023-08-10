package kz.project.gallery.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.project.domain.model.token.AccessToken
import kz.project.domain.use_case.GetAuthenticationTokenUseCase
import kz.project.domain.use_case.GetRefreshTokenUseCase
import kz.project.domain.use_case.RefreshTokenUseCase
import kz.project.domain.use_case.SaveAccessTokenUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AccessTokenViewModel @Inject constructor(
    private val getAuthenticationTokenUseCase: GetAuthenticationTokenUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : ViewModel() {

    private val _authenticationToken = MutableLiveData<AccessToken>()
    val authenticationToken: LiveData<AccessToken>
        get() = _authenticationToken

    // TODO вынести в BaseViewModel
    private val compositeDisposable = CompositeDisposable()

    /**
     * Достаёт токен авторизации из shared preferences и оборачивает его в Single<String>.
     */
    private fun getAuthTokenFromSharedPrefs() =
        Single.fromCallable { getAuthenticationTokenUseCase.invoke() }

    /**
     * Достаёт refresh токен из shared preferences и оборачивает его в Single<String>.
     */
    private fun getRefreshTokenFromSharedPrefs() =
        Single.fromCallable { getRefreshTokenUseCase.invoke() }

    /**
     * Необходима, чтобы поставить небольшую задержку после того как Single<T : Any> вернёт значение,
     * функция обернёт это значение снова в Single<> и поставит задержку, чтобы SplashFragment
     * можно было увидеть.
     */
    private fun <T : Any> setDelayToValueProcessing(value: T) =
        Single.just(value).delay(300, TimeUnit.MILLISECONDS)


    /**
     * Рефрешит токен и возвращает его с задержкой, если при рефреше произошла ошибка вернет пустой токен
     */
    fun refreshTokenWithDelay() {
        getRefreshTokenFromSharedPrefs()
            .subscribeOn(Schedulers.io())
            .flatMap { accessToken -> setDelayToValueProcessing(accessToken) }
            .flatMap { refreshToken -> refreshTokenUseCase.invoke(refreshToken) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { accessToken ->
                    saveAccessTokenUseCase.invoke(accessToken)
                    _authenticationToken.value = accessToken
                },
                {
                    _authenticationToken.value = AccessToken("", "")
                }
            ).let(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}