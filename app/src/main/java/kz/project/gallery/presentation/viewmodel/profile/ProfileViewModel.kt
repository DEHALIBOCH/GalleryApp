package kz.project.gallery.presentation.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.model.user.UpdatePasswordForm
import kz.project.domain.model.user.User
import kz.project.domain.use_case.photo.GetPhotosByUserIdUseCase
import kz.project.domain.use_case.token.DeleteTokenUseCase
import kz.project.domain.use_case.user.DeleteUserUseCase
import kz.project.domain.use_case.user.GetCurrentUserUseCase
import kz.project.domain.use_case.user.UpdatePasswordUseCase
import kz.project.domain.use_case.validation.ValidateConfirmPasswordUseCase
import kz.project.domain.use_case.validation.ValidatePasswordUseCase
import kz.project.gallery.presentation.viewmodel.BaseViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getPhotosByUserIdUseCase: GetPhotosByUserIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
) : BaseViewModel() {

    var currentUser : User? = null

    private val _photosByUserIdLiveData = MutableLiveData<Resource<PhotoResponse>>()
    val photosByUserIdLiveData: LiveData<Resource<PhotoResponse>>
        get() = _photosByUserIdLiveData

    private val _currentUserLiveData = MutableLiveData<Resource<User>>()
    val currentUserLiveData: LiveData<Resource<User>>
        get() = _currentUserLiveData


    private val _passwordUpdatingResult = MutableLiveData<Resource<String>>()
    val passwordUpdatingResult: LiveData<Resource<String>>
        get() = _passwordUpdatingResult


    private val _passwordUpdateValidationResult = MutableLiveData<Resource<UpdatePasswordValidationForm>>()
    val passwordUpdateValidationResult: LiveData<Resource<UpdatePasswordValidationForm>>
        get() = _passwordUpdateValidationResult


    fun validateUpdatePassword(currentUserId: Int, updatePasswordValidationForm: UpdatePasswordValidationForm) {

        _passwordUpdateValidationResult.value = Resource.Loading()

        val oldPasswordValidationResult =
            validatePasswordUseCase.invoke(updatePasswordValidationForm.oldPassword)
        val newPasswordValidationResult =
            validatePasswordUseCase.invoke(updatePasswordValidationForm.newPassword)
        val confirmPasswordValidationResult = validateConfirmPasswordUseCase(
            updatePasswordValidationForm.newPassword, updatePasswordValidationForm.confirmNewPassword
        )

        val hasError = listOf(
            oldPasswordValidationResult,
            newPasswordValidationResult,
            confirmPasswordValidationResult
        ).any { !it.successful }

        if (hasError) {
            val validationError = updatePasswordValidationForm.copy(
                oldPasswordError = oldPasswordValidationResult.errorMessage,
                newPasswordError = newPasswordValidationResult.errorMessage,
                confirmNewPasswordError = confirmPasswordValidationResult.errorMessage,
            )
            _passwordUpdateValidationResult.value = Resource.Error(
                "Forms validation error", validationError
            )
            return
        }

        _passwordUpdateValidationResult.value = Resource.Success(updatePasswordValidationForm)

        updatePassword(
            currentUserId,
            UpdatePasswordForm(
                oldPassword = updatePasswordValidationForm.oldPassword,
                newPassword = updatePasswordValidationForm.newPassword,
            )
        )
    }

    private fun updatePassword(currentUserId: Int, updatePasswordForm: UpdatePasswordForm) {

        _passwordUpdatingResult.value = Resource.Loading()

        updatePasswordUseCase.invoke(
            currentUserId = currentUserId,
            updatePasswordForm = updatePasswordForm
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _passwordUpdatingResult.value = Resource.Success("")
                },
                {
                    _passwordUpdatingResult.value = Resource.Error(it.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    /**
     * Запрашивает текущего авторизованного пользователя
     */
    fun getCurrentUser() {
        _currentUserLiveData.value = Resource.Loading()
        getCurrentUserUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    _currentUserLiveData.value = Resource.Success(user)
                    currentUser = user
                },
                { error ->
                    _currentUserLiveData.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    /**
     * Запрашивает фото определенного пользователя используя его id
     */
    fun getPhotosByUserId(id: Int) {
        _photosByUserIdLiveData.value = Resource.Loading()

        getPhotosByUserIdUseCase.invoke(id, 1, 40)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResponse ->
                    _photosByUserIdLiveData.value = Resource.Success(photoResponse)
                },
                { error ->
                    _photosByUserIdLiveData.value = Resource.Error(error.message ?: Constants.UNEXPECTED_ERROR)
                }
            ).let(compositeDisposable::add)
    }

    /**
     * Удаляет из sharedPrefs данные об авторизованном пользователе.
     */
    fun signOut() = deleteTokenUseCase.invoke()
}