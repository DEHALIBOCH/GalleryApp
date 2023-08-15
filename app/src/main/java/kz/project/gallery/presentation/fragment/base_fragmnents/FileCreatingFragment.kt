package kz.project.gallery.presentation.fragment.base_fragmnents

import android.net.Uri
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kz.project.gallery.R
import kz.project.gallery.utils.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Базовый класс для фрагментов, в которых вынесена логика работы по созданию файлов
 */
open class FileCreatingFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected fun createUriForTemporaryPhoto(): Uri {
        val tempImagesDirectory = File(
            requireContext().filesDir, requireContext().getString(R.string.temp_images_dir)
        )

        tempImagesDirectory.mkdir()

        val temporaryImage = File(tempImagesDirectory, requireContext().getString(R.string.temp_image))

        return FileProvider.getUriForFile(
            requireContext(), getString(R.string.authorities), temporaryImage
        )
    }

    protected fun createTemporaryImage(selectedImageUri: Uri?): File? = try {
        selectedImageUri?.let {
            val inputStream = requireContext().contentResolver.openInputStream(selectedImageUri)
            val file = File.createTempFile(createNameForImage(), Constants.JPG, requireContext().cacheDir)

            file.outputStream().use { fileOutputStream ->
                inputStream?.copyTo(fileOutputStream)
            }
            inputStream?.close()

            file
        }
    } catch (e: Exception) {
        Log.e("FILE_EXCEPTION", e.localizedMessage ?: "")
        null
    }

    private fun createNameForImage(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "${Constants.TEMP_IMAGE}_$timeStamp"
    }

}