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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.project.domain.model.photo.Photo
import kz.project.domain.use_case.photo.GetNewPhotosUseCase
import kz.project.domain.use_case.photo.GetPopularPhotosUseCase
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    // TODO рефакторинг - по сути эти 2 usecase можно заменить одним
    private val getNewPhotosUseCase: GetNewPhotosUseCase,
    private val getPopularPhotosUseCase: GetPopularPhotosUseCase,
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _newPhotosLiveData = MutableLiveData<Resource<PagingData<Photo>>>()
    val newPhotosLiveData: LiveData<Resource<PagingData<Photo>>>
        get() = _newPhotosLiveData


    private val _popularPhotosLiveData = MutableLiveData<Resource<PagingData<Photo>>>()
    val popularPhotosLiveData: LiveData<Resource<PagingData<Photo>>>
        get() = _popularPhotosLiveData


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPopularPhotos() {
        _popularPhotosLiveData.value = Resource.Loading()

        getPopularPhotosUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT
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

        getNewPhotosUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT
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