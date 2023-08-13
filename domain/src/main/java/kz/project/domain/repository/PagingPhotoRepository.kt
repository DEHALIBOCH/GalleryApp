package kz.project.domain.repository

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import kz.project.domain.model.photo.Photo

interface PagingPhotoRepository {

    /**
     * Получение фото исходя из параметров new и popular
     */
    fun getAllPhotosList(
        new: Boolean?,
        popular: Boolean?,
        userId: Int?,
        page: Int,
        limit: Int,
    ): Flowable<PagingData<Photo>>

}