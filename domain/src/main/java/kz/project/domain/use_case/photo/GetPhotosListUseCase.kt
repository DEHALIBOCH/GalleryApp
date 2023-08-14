package kz.project.domain.use_case.photo

import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosListUseCase @Inject constructor(
    private val pagingPhotoRepository: PhotoRepository,
) {

    /**
     * Возвращает фотографии помеченные как новые.
     * @param page номер страницы.
     * @param limit количество элементов на странице.
     * @param popular по дефолту true.
     */
    operator fun invoke(page: Int, limit: Int, popular: Boolean?, new: Boolean?) =
        pagingPhotoRepository.getAllPhotosList(
            new = new,
            popular = popular,
            userId = null,
            page = page,
            limit = limit,
        )
}