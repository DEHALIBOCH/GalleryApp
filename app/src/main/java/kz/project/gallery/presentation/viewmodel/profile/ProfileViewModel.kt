package kz.project.gallery.presentation.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.model.user.User
import kz.project.domain.use_case.photo.GetPhotosByUserIdUseCase
import kz.project.domain.use_case.user.GetCurrentUserUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getPhotosByUserIdUseCase: GetPhotosByUserIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : BaseViewModel() {


    private val _photosByUserIdLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val photosByUserIdLiveData: LiveData<Resource<PhotoResponse>>
        get() = _photosByUserIdLiveData

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>>
        get() = _currentUser


    /**
     * Запрашивает текущего авторизованного пользователя
     */
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

    /**
     * Запрашивает фото определенного пользователя используя его id
     */
    fun getPhotosByUserId(id: Int) {
        _photosByUserIdLiveData.value = Resource.Loading()

        getPhotosByUserIdUseCase.invoke(id, 1, 40)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _photosByUserIdLiveData.value = Resource.Success(photoResponse)
                },
                { error ->
                    _photosByUserIdLiveData.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

}