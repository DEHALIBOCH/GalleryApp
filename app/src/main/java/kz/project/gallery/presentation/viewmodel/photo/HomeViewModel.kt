package kz.project.gallery.presentation.viewmodel.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getPhotosListUseCase: GetPhotosListUseCase,
) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    private val _newPhotosLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val newPhotosLiveData: LiveData<Resource<PhotoResponse>>
        get() = _newPhotosLiveData


    private val _popularPhotosLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val popularPhotosLiveData: LiveData<Resource<PhotoResponse>>
        get() = _popularPhotosLiveData


    fun getPopularPhotos() {
        _popularPhotosLiveData.value = Resource.Loading()

        getPhotosListUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT,
            true,
            null,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _popularPhotosLiveData.value = Resource.Success(photoResponse)
                },
                { error ->
                    _popularPhotosLiveData.value = Resource.Error(error.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    fun getNewPhotos() {
        _newPhotosLiveData.value = Resource.Loading()

        getPhotosListUseCase.invoke(
            Constants.PAGE,
            Constants.LIMIT,
            null,
            true,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _newPhotosLiveData.value = Resource.Success(photoResponse)
                },
                { error ->
                    _newPhotosLiveData.value = Resource.Error(error.localizedMessage ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}