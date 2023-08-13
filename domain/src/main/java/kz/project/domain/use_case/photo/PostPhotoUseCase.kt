package kz.project.domain.use_case.photo

import kz.project.domain.model.photo.PhotoUploadForm
import kz.project.domain.repository.PhotoRepository
import java.io.File
import javax.inject.Inject

class PostPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {

    /**
     * Отправляет фото на сервер.
     * @param file сама фотография, желательно не большого размера.
     * @param photoUploadForm форма отправки, содержащая основную информацию о фото.
     */
    operator fun invoke(file: File, photoUploadForm: PhotoUploadForm) =
        photoRepository.postPhoto(file, photoUploadForm)
}