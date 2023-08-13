package kz.project.domain.use_case.photo

import kz.project.domain.repository.PagingPhotoRepository
import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPopularPhotosUseCase @Inject constructor(
    private val pagingPhotoRepository: PagingPhotoRepository,
) {

    /**
     * Возвращает фотографии помеченные как новые.
     * @param page номер страницы.
     * @param limit количество элементов на странице.
     * @param popular по дефолту true.
     */
    operator fun invoke(page: Int, limit: Int, popular: Boolean = true) =
        pagingPhotoRepository.getAllPhotosList(
            new = null,
            popular = popular,
            userId = null,
            page = page,
            limit = limit,
        )
}