package kz.project.data.common

object Constants {

    /**
     * Жестко закодированное значение, без него невозможно авторизоваться
     */
    const val CLIENT_ID = "70_1r5x3tudicv4o4g84cs0sc8ocs0koso40w0o4g84k0s4cc844o"

    /**
     * Жестко закодированное значение, без него невозможно авторизоваться
     */
    const val CLIENT_SECRET = "1kda6tulcbr480g4k4og88kggksko8occkkc444cs8cssgkcco"

    const val UNEXPECTED_ERROR = "Unexpected Error"

    /**
     * Необходима в хедерах запроса в api
     */
    const val AUTHORIZATION = "Authorization"

    /**
     * Необходима в хедерах запроса в api
     */
    const val BEARER = "Bearer"

    /**
     * Максимальное кол-во попыток повторной авторизации, чтобы не попасть в dead-loop
     */
    const val MAX_AUTH_ATTEMPTS = 3

    /**
     *  Количество загружаемых элементов на 1 странице
     */
    const val PHOTOS_PER_PAGE_LIMIT = 12

    const val EPOCH_TIME = "01.01.1970"

    const val MEDIA_TYPE_IMAGE = "image/*"

    const val FILE = "file"

    const val PATH_TO_IMAGE = "api/media_objects/"

    const val NOTICE_TRYING_TO_ACCESS = "Notice: Trying to access array offset on value of type null"

    const val OLD_IS_PASSWORD_INCORRECT = "oldPassword is incorrect"

    const val PASSWORD_UPDATE_SUCCESSFUL = "Successful password update"
}