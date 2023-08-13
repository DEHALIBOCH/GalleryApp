package kz.project.domain.use_case.photo

import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotoByIdUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    /**
     * Возвращает фотографию по её id
     * @param id id фотографии
     */
    operator fun invoke(id: Int) =
        photoRepository.getPhotoById(id)
}