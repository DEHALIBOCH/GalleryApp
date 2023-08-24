package kz.project.domain.use_case.photo

import kz.project.domain.repository.LocalPhotosRepository
import javax.inject.Inject

class GetCachedPhotosUseCase @Inject constructor(
    private val localPhotosRepository: LocalPhotosRepository
) {

    operator fun invoke(
        new: Boolean,
        popular: Boolean,
        page: Int = 1,
    ) =
        if (new) localPhotosRepository.getNewPhotos(page) else localPhotosRepository.getPopularPhotos(page)
}