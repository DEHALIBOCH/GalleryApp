package kz.project.data.remote.dto.error.parser

import kz.project.data.common.Constants
import kz.project.data.remote.dto.error.ApiException
import kz.project.data.remote.dto.error.error_model.Error
import kz.project.data.remote.dto.error.error_model.RegistrationErrorDetail
import com.google.gson.Gson
import kz.project.data.remote.dto.error.error_model.UpdatePasswordErrorDto
import javax.inject.Inject

/**
 * Класс который парсит Json-ответ ошибки с сервера и возвращает объект Throwable содержащий информацию
 * об ошибке, объект Throwable затем, в репозитории, пробрасывается дальше и должен быть там обработан.
 */
class ErrorParser @Inject constructor(
    private val gson: Gson,
    private val galleryApiErrorFormatter: GalleryApiErrorFormatter,
) {

    /**
     * Парсит ошибку регистрации и затем пробрасывает ее дальше
     */
    fun parseRegistrationErrorMessage(errorBody: String?): Throwable {
        return try {
            val errorDetail = gson.fromJson(
                errorBody,
                RegistrationErrorDetail::class.java
            )
            val errorDetailsString =
                galleryApiErrorFormatter.formatRegistrationError(errorDetail.detail)
            ApiException(errorDetailsString)

        } catch (e: Exception) {
            Exception(Constants.UNEXPECTED_ERROR)
        }
    }

    /**
     * Парсит ошибку обновления пароля
     */
    fun parsePasswordUpdateError(errorBody: String?): Throwable {
        return try {
            val errorDetail = gson.fromJson(errorBody, UpdatePasswordErrorDto::class.java)
            return when (errorDetail.detail) {
                Constants.NOTICE_TRYING_TO_ACCESS -> {
                    ApiException(Constants.PASSWORD_UPDATE_SUCCESSFUL)
                }

                Constants.OLD_IS_PASSWORD_INCORRECT -> {
                    ApiException(Constants.OLD_PASSWORD_INCORRECT)
                }

                else -> {
                    Exception(errorDetail.detail ?: Constants.UNEXPECTED_ERROR)
                }
            }
        } catch (e: Exception) {
            Exception(Constants.UNEXPECTED_ERROR)
        }
    }

    /**
     * Парсит ошибку аутентификации и затем пробрасывает ее дальше
     */
    fun parseErrorMessage(errorBody: String?): Throwable {
        return try {
            val errorDetail = gson.fromJson<Error>(errorBody, Error::class.java)
            ApiException(errorDetail.detail ?: Constants.UNEXPECTED_ERROR)
        } catch (e: Exception) {
            Exception(Constants.UNEXPECTED_ERROR)
        }
    }

}