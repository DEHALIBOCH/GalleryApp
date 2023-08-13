package kz.project.domain.use_case.photo

import kz.project.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosByUserIdUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    /**
     * Возвращает фотографии созданные определенным пользователем.
     * @param userId id пользователя
     * @param page номер страницы.
     * @param limit количество элементов на странице.
     */
    operator fun invoke(userId: Int, page: Int, limit: Int) =
        photoRepository.getPhotosListByUserId(userId, page, limit)
}