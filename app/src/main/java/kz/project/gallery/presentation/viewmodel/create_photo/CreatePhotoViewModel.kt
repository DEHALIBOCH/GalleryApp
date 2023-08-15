package kz.project.gallery.presentation.viewmodel.create_photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoUploadForm
import kz.project.domain.use_case.photo.PostPhotoUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import java.io.File
import javax.inject.Inject

class CreatePhotoViewModel @Inject constructor(
    private val postPhotoUseCase: PostPhotoUseCase,
) : BaseViewModel() {

    private val _postPhotoLiveData = MutableLiveData<Resource<Photo>>()
    val postPhotoLiveData: LiveData<Resource<Photo>>
        get() = _postPhotoLiveData

    fun postPhoto(file: File, photoUploadForm: PhotoUploadForm) {

        _postPhotoLiveData.value = Resource.Loading()

        postPhotoUseCase.invoke(file, photoUploadForm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photo ->
                    _postPhotoLiveData.value = Resource.Success(photo)
                },
                { error ->
                    _postPhotoLiveData.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }
}