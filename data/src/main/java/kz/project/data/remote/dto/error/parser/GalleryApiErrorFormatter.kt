package kz.project.data.remote.dto.error.parser

import kz.project.data.common.Constants
import javax.inject.Inject

/**
 * Класс для форматирования ошибки получаемой с api
 */

class GalleryApiErrorFormatter @Inject constructor() {

    /**
     * Форматирует детали ошибки регистрации и убирает из них повторяющуюся информацию
     */
    fun formatRegistrationError(errorDetails: String?) = errorDetails?.replace(
        Regex("(?m)^.*(?<!Canonical):.*$\n"),
        ""
    ) ?: Constants.UNEXPECTED_ERROR


}