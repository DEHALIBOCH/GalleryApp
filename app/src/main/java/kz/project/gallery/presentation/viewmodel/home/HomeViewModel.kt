package kz.project.gallery.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.use_case.photo.GetPhotosByNameUseCase
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource

class HomeViewModel(
    private val isPopular: Boolean,
    private val getPhotosListUseCase: GetPhotosListUseCase,
    private val getPhotosByNameUseCase: GetPhotosByNameUseCase,
) : BaseViewModel() {

    private val popular: Boolean? = if (isPopular) true else null
    private val new: Boolean? = if (isPopular) null else true

    private var allPhotosResponse: PhotoResponse? = null
    var photosPage = Constants.PAGE
    var maxPhotosPage = Constants.MAX_PAGE

    private val _photosLiveData = MutableLiveData<Resource<List<Photo>>>()
    val photosLiveData: LiveData<Resource<List<Photo>>>
        get() = _photosLiveData

    init {
        getPagingPhotos()
    }

    /**
     * Метод для получения фото, важно - при создании вью модели он будет вызван в init блоке
     */
    fun getPagingPhotos() = getPhotos(false)

    fun refreshPhotos() {
        photosPage = Constants.PAGE
        allPhotosResponse = null
        getPhotos(true)
    }

    private fun getPhotos(isRefreshing: Boolean) {

        _photosLiveData.value = Resource.Loading()

        getPhotosListUseCase.invoke(
            page = photosPage,
            limit = Constants.LIMIT,
            popular = popular,
            new = new,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _photosLiveData.value = handleResponse(photoResponse, isRefreshing)
                },
                { error ->
                    _photosLiveData.value = Resource.Error(error.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }


    /**
     * Обрабатывает ответ с сервера исходя из параметра isRefreshing.
     * Если параметр isRefreshing == false, добавляет полученные фото в конец allPhotosResponse и возвращает его.
     * @param isRefreshing - происходит ли refresh с помощью SwipeRefreshLayout.
     */
    private fun handleResponse(photoResponse: PhotoResponse, isRefreshing: Boolean): Resource<List<Photo>> =
        if (!isRefreshing) {
            photosPage++
            maxPhotosPage = photoResponse.countOfPages
            if (allPhotosResponse == null) {
                allPhotosResponse = photoResponse
            } else {
                val oldPhotos = allPhotosResponse?.photos
                val newPhotos = photoResponse.photos
                oldPhotos?.addAll(newPhotos)
            }
            Resource.Success(allPhotosResponse?.photos ?: photoResponse.photos)
        } else {
            Resource.Success(photoResponse.photos)
        }


    /**
     * Получение фото по имени
     */
    fun getPhotosByName(name: String) {

        _photosLiveData.value = Resource.Loading()

        getPhotosByNameUseCase.invoke(
            name = name,
            page = Constants.PAGE,
            limit = Constants.LIMIT,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _photosLiveData.value = Resource.Success(
                        photoResponse.photos
                    )
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
        private val getPhotosByNameUseCase: GetPhotosByNameUseCase,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    isPopular = popular,
                    getPhotosListUseCase = getPhotosListUseCase,
                    getPhotosByNameUseCase = getPhotosByNameUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

