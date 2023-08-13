package kz.project.data.repository.paging_source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.data.common.Constants
import kz.project.data.mappers.toPhotoResponse
import kz.project.data.remote.PhotoApi
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse
import javax.inject.Inject

// TODO разобраться как совместить пагинацию и clean arch
class PhotosPagingSource(
    private val photoApi: PhotoApi,
    private val isPopular: Boolean?,
    private val isNew: Boolean?,
    private val userId: Int?,
) : RxPagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {

        val anchorPosition = state.anchorPosition ?: return null
        val page =
            state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {

        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(Constants.PHOTOS_PAGE_SIZE)

        return photoApi.getPhotosList(isNew, isPopular, userId, page, pageSize)
            .subscribeOn(Schedulers.io())
            .map { it.toPhotoResponse() }
            .map { transformToLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun transformToLoadResult(photoResponse: PhotoResponse, page: Int): LoadResult<Int, Photo> =
        LoadResult.Page(
            data = photoResponse.photos,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (page == photoResponse.countOfPages) null else page + 1,
        )


    /**
     * Фабрика для различных Дата Соурсов, т.к. это единственный способ прокидывать параметры который я придумал
     */
    class Factory(
        private val photoApi: PhotoApi
    ) {
        fun create(
            isPopular: Boolean?,
            isNew: Boolean?,
            userId: Int?,
        ) = PhotosPagingSource(photoApi, isPopular, isNew, userId)
    }
}
