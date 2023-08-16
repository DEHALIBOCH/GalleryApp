package kz.project.domain.repository

import java.io.File

/**
 * Репозиторий для сжатия больших фото, т.к. api не принимает файлы большого размера
 */
interface CompressImageRepository {

    fun compressImage(image: File) : File
}