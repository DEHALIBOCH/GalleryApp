package kz.project.gallery.presentation.fragment.base_fragmnents

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import kz.project.gallery.R

/**
 * Базовый класс для фрагментов, в которых необходимо работать с получением фотографий с устройства
 */
abstract class PhotoCaptureFragment(@LayoutRes contentLayoutId: Int) : FileCreatingFragment(contentLayoutId) {

    private lateinit var photoFromCameraUri: Uri


    /**
     * Проверяет были ли выданы permission требуемые для работы, возвращает true если все разрешения выданы
     */
    private fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(
        requireContext(), permission
    ) == PackageManager.PERMISSION_GRANTED


    private fun checkGalleryPermissions() =
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun checkCameraPermissions() =
        checkGalleryPermissions() && checkPermission(Manifest.permission.CAMERA)

    /**
     * Лаунчер для запроса permission
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(requireContext(), getString(R.string.no_permissions), Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Запрашивает разрешения необходимые для работы с имеющимися медиа файлами
     */
    private fun requestGalleryPermissions() = requestPermissionLauncher.apply {
        launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * Запрашивает разрешения необходимые для работы с камерой
     */
    private fun requestCameraPermissions() {
        requestGalleryPermissions()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    /**
     * Лаунчер для запуска камеры и получения фотографии
     */
    val takePictureResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isTaken ->
        if (!isTaken) {
            return@registerForActivityResult
        }
        photoCaptured(photoFromCameraUri)
    }

    /**
     * Лаунчер для получения фотографии из уже существующих медиа
     */
    private val startActivityForPickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { photoCaptured(uri) }
    }

    /**
     * Создаёт BottomSheetDialog для выбора фотографии с устройства
     */
    protected fun createBottomSheetDialog(context: Context) = Dialog(context).apply {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.bottom_sheet_dialog_modal_photo)

        val takePhotoFromGallery: AppCompatButton = findViewById(R.id.takePhotoFromGallery)
        val takePhotoFromCamera: AppCompatButton = findViewById(R.id.takePhotoFromCamera)

        takePhotoFromGallery.setOnClickListener {
            photoFromGallery()
        }
        takePhotoFromCamera.setOnClickListener {
            photoFromCamera()
        }

        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.bottom_sheet_dialog_background)
            attributes.windowAnimations = R.style.BottomSheetAnimation
            setGravity(Gravity.BOTTOM)
        }
    }

    /**
     * Обработка нажатия на кнопку запроса фото из камеры
     */
    private fun photoFromCamera() {
        if (!checkCameraPermissions()) {
            requestCameraPermissions()
            return
        }
        photoFromCameraUri = createUriForTemporaryPhoto()
        takePictureResultLauncher.launch(photoFromCameraUri)
    }


    /**
     * Обработка нажатия на кнопку запроса фото из медиа файлов
     */
    private fun photoFromGallery() {
        if (!checkGalleryPermissions()) {
            requestGalleryPermissions()
            return
        }
        startActivityForPickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    /**
     * Абстрактная функция(коллбек по сути), для обработки получения uri при выборе фото
     */
    abstract fun photoCaptured(photoUri: Uri)

}