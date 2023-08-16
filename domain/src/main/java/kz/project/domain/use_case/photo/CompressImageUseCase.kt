package kz.project.domain.use_case.photo

import kz.project.domain.repository.CompressImageRepository
import java.io.File
import javax.inject.Inject

class CompressImageUseCase @Inject constructor(
    private val compressImageRepository: CompressImageRepository
) {

    operator fun invoke(image: File) = compressImageRepository.compressImage(image)
}