package kz.project.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import kz.project.data.common.Constants
import kz.project.data.remote.PhotoApi
import kz.project.data.repository.paging_source.PhotosPagingSource
import kz.project.domain.model.photo.Photo
import kz.project.domain.repository.PagingPhotoRepository
import javax.inject.Inject

class PagingPhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi
) : PagingPhotoRepository {

    override fun getAllPhotosList(
        new: Boolean?,
        popular: Boolean?,
        userId: Int?,
        page: Int,
        limit: Int
    ): Flowable<PagingData<Photo>> = Pager(
        config = PagingConfig(
            pageSize = Constants.PHOTOS_PAGE_SIZE,
            enablePlaceholders = true,
            maxSize = Constants.PHOTOS_MAX_SIZE,
            prefetchDistance = Constants.PHOTOS_PREFETCH_DISTANCE,
            initialLoadSize = Constants.PHOTOS_INITIAL_LOAD_SIZE,
        )
    ) {
        PhotosPagingSource.Factory(photoApi).create(popular, new, userId)
    }.flowable
}