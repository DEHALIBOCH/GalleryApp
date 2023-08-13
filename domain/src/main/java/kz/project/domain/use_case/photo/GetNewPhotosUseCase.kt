package kz.project.domain.use_case.photo

import kz.project.domain.repository.PagingPhotoRepository
import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetNewPhotosUseCase @Inject constructor(
    private val pagingPhotoRepository: PagingPhotoRepository,
) {

    /**
     * Возвращает фотографии помеченные как новые.
     * @param page номер страницы.
     * @param limit количество элементов на странице.
     * @param new по дефолту true.
     */
    operator fun invoke(page: Int, limit: Int, new: Boolean = true) =
        pagingPhotoRepository.getAllPhotosList(
            new = new,
            popular = null,
            userId = null,
            page = page,
            limit = limit
        )
}