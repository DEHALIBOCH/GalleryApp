package kz.project.domain.use_case.photo

import kz.project.domain.model.photo.Photo
import kz.project.domain.repository.LocalPhotosRepository
import javax.inject.Inject

class SaveCachedPhotosUseCase @Inject constructor(
    private val localPhotosRepository: LocalPhotosRepository
) {

    operator fun invoke(photos: List<Photo>) =
        localPhotosRepository.savePhotos(photos)
}