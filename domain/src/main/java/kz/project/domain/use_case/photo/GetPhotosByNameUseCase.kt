package kz.project.domain.use_case.photo

import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosByNameUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    /**
     * Возвращает фотографии с запросом по имени.
     * @param name часть названия фотографии для поиска.
     * @param page номер страницы.
     * @param limit количество элементов на странице.
     */
    operator fun invoke(name: String, page: Int, limit: Int) =
        photoRepository.getPhotosByNameList(name, page, limit)
}