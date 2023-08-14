package kz.project.gallery.presentation.viewmodel.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.use_case.photo.GetPagingPhotosUseCase
import kz.project.domain.use_case.photo.GetPhotosByUserIdUseCase
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getPagingPhotosUseCase: GetPagingPhotosUseCase,
    private val getPhotosByUserIdUseCase: GetPhotosByUserIdUseCase,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _newPhotosLiveData = MutableLiveData<Resource<PagingData<Photo>>>()
    val newPhotosLiveData: LiveData<Resource<PagingData<Photo>>>
        get() = _newPhotosLiveData


    private val _popularPhotosLiveData = MutableLiveData<Resource<PagingData<Photo>>>()
    val popularPhotosLiveData: LiveData<Resource<PagingData<Photo>>>
        get() = _popularPhotosLiveData

    private val _photosByUserIdLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val photosByUserIdLiveData: LiveData<Resource<PhotoResponse>>
        get() = _photosByUserIdLiveData

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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPopularPhotos() {
        _popularPhotosLiveData.value = Resource.Loading()

        getPagingPhotosUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT,
            true,
            null,
        ).observeOn(AndroidSchedulers.mainThread())
            .map { pagingData ->
                pagingData.filter { it.image.name.isNotBlank() }
            }.cachedIn(viewModelScope)
            .subscribe(
                { pagingData ->
                    _popularPhotosLiveData.value = Resource.Success(pagingData)
                },
                {
                    _popularPhotosLiveData.value = Resource.Error(it.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNewPhotos() {
        _newPhotosLiveData.value = Resource.Loading()

        getPagingPhotosUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT,
            null,
            true,
        ).observeOn(AndroidSchedulers.mainThread())
            .map { pagingData ->
                pagingData.filter { it.image.name.isNotBlank() }
            }.cachedIn(viewModelScope)
            .subscribe(
                { pagingData ->
                    _newPhotosLiveData.value = Resource.Success(pagingData)
                },
                {
                    _newPhotosLiveData.value = Resource.Error(it.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}