package kz.project.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kz.project.domain.repository.CompressImageRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.sqrt

class CompressImageRepositoryImpl @Inject constructor() : CompressImageRepository {

    override fun compressImage(image: File): File {
        val maxSizeBytes = 512 * 1024

        if (image.length() <= maxSizeBytes) {
            return image
        }

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(image.absolutePath, options)

        val ratio = sqrt((bitmap.byteCount / maxSizeBytes).toDouble())
        val newWidth = (bitmap.width / ratio).toInt()
        val newHeight = (bitmap.height / ratio).toInt()

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)

        val originalImageName = image.nameWithoutExtension
        val compressedImageName = "$originalImageName-compressed.jpg"

        val compressedFile = File(image.parent, compressedImageName)

        return try {
            val outputStream = FileOutputStream(compressedFile)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            outputStream.flush()
            outputStream.close()

            compressedFile
        } catch (e: Exception) {
            image
        }
    }
}