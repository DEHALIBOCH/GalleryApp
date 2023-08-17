package kz.project.gallery.presentation.viewmodel.settings

import kz.project.domain.use_case.token.DeleteTokenUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val deleteTokenUseCase: DeleteTokenUseCase
) : BaseViewModel() {

    /**
     * Удаляет из sharedPrefs данные об авторизованном пользователе.
     */
    fun signOut() = deleteTokenUseCase.invoke()
}