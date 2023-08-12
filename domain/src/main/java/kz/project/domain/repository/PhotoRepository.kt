package kz.project.domain.repository

import io.reactivex.rxjava3.core.Single
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoUploadForm
import java.io.File

interface PhotoRepository {

    /**
     * Получение фото исходя из параметров new и popular
     */
    fun getAllPhotosList(
        new: Boolean?,
        popular: Boolean?,
        userId: Int?,
        page: Int,
        limit: Int,
    ): Single<List<Photo>>

    /**
     * Получение фото исходя из определенного имени
     */
    fun getPhotosByNameList(
        name: String,
        page: Int,
        limit: Int,
    ): Single<List<Photo>>

    /**
     * Получение определенного фото по id
     */
    fun getPhotoById(id: Int): Single<Photo>

    /**
     * Отправка фото на сервер
     */
    fun postPhoto(file: File, photoUploadForm: PhotoUploadForm): Single<Photo>
}