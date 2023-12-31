package kz.project.gallery.presentation.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.user.User
import kz.project.domain.use_case.user.GetCurrentUserUseCase
import kz.project.domain.use_case.user.GetUserByIdUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

/**
 * ViewModel для хранения модели текущего авторизованного пользователя
 */
class UserViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : BaseViewModel() {

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>>
        get() = _currentUser

    private val _userById = MutableLiveData<Resource<User>>()
    val userById: LiveData<Resource<User>>
        get() = _userById

    fun getCurrentUser() {
        _currentUser.value = Resource.Loading()
        getCurrentUserUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    _currentUser.value = Resource.Success(user)
                },
                { error ->
                    _currentUser.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    fun getUserById(userId: String) {
        val digits = userId.replace("\\D+".toRegex(), "")

        _userById.value = Resource.Loading()

        try {
            getUserByIdUseCase.invoke(id = digits.toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user ->
                        _userById.value = Resource.Success(user)
                    },
                    { error ->
                        _userById.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                    }
                ).let(compositeDisposable::add)
        } catch (e: Exception) {
            Resource.Success(
                User(
                    id = -1,
                    email = "undefined@mail.com",
                    enabled = false,
                    phone = "",
                    fullName = "",
                    username = "",
                    birthday = "",
                    roles = emptyList(),
                )
            )
        }


    }

}