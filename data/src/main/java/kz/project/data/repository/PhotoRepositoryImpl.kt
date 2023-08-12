package kz.project.data.repository

import io.reactivex.rxjava3.core.Single
import kz.project.data.common.Constants
import kz.project.data.mappers.toPhoto
import kz.project.data.remote.PhotoApi
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoUploadForm
import kz.project.domain.repository.PhotoRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi
) : PhotoRepository {

    //TODO DataSource для фотографий

    override fun getAllPhotosList(
        new: Boolean?,
        popular: Boolean?,
        userId: Int?,
        page: Int,
        limit: Int
    ) = photoApi.getPhotosList(
        new = null,
        popular = null,
        userId = null,
        page = page,
        limit = limit,
    ).flatMap { response ->
        Single.just(response.photos?.filter { it.image != null }?.map { it.toPhoto() } ?: emptyList())
    }

    fun getPopularPhotosList(
        page: Int,
        limit: Int,
        popular: Boolean = true,
    ) = getAllPhotosList(
        new = null,
        popular = popular,
        userId = null,
        page = page,
        limit = limit,
    )

    fun getNewPhotosList(
        page: Int,
        limit: Int,
        new: Boolean = true,
    ) = getAllPhotosList(
        new = new,
        popular = null,
        userId = null,
        page = page,
        limit = limit,
    )

    fun getPhotosListByUserId(
        userId: Int,
        page: Int,
        limit: Int
    ) = getAllPhotosList(
        new = null,
        popular = null,
        userId = userId,
        page = page,
        limit = limit,
    )

    override fun getPhotosByNameList(
        name: String,
        page: Int,
        limit: Int
    ): Single<List<Photo>> = photoApi.getPhotosByNameList(
        name = name,
        page = page,
        limit = limit,
    ).flatMap { response ->
        Single.just(response.photos?.filter { it.image != null }?.map { it.toPhoto() } ?: emptyList())
    }

    override fun getPhotoById(id: Int) = photoApi.getPhotoById(id).flatMap {
        Single.just(it.toPhoto())
    }

    override fun postPhoto(file: File, photoUploadForm: PhotoUploadForm): Single<Photo> {

        val imagePart = file.asRequestBody(Constants.MEDIA_TYPE_IMAGE.toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData(Constants.FILE, file.name, imagePart)

        return photoApi.postImage(image).flatMap { imageDto ->
            val photoUploadFormWithImage =
                photoUploadForm.copy(image = "${Constants.PATH_TO_IMAGE}${imageDto.id}")

            photoApi.postPhoto(photoUploadFormWithImage).flatMap {
                Single.just(it.toPhoto())
            }
        }
    }
}