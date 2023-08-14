package kz.project.gallery.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource

class HomeViewModel(
    private val isPopular: Boolean,
    private val getPhotosListUseCase: GetPhotosListUseCase,
) : BaseViewModel() {


    private val popular: Boolean? = if (isPopular) true else null
    private val new: Boolean? = if (isPopular) null else true


    private val _photosLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val photosLiveData: LiveData<Resource<PhotoResponse>>
        get() = _photosLiveData


    fun getPhotos() {
        _photosLiveData.value = Resource.Loading()

        getPhotosListUseCase.invoke(
            page = Constants.PAGE,
            limit = Constants.LIMIT,
            popular = popular,
            new = new,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _photosLiveData.value = Resource.Success(photoResponse)
                },
                { error ->
                    _photosLiveData.value = Resource.Error(error.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val popular: Boolean,
        private val getPhotosListUseCase: GetPhotosListUseCase,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    isPopular = popular,
                    getPhotosListUseCase = getPhotosListUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

