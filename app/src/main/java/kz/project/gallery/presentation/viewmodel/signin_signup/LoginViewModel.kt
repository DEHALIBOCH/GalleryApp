package kz.project.gallery.presentation.viewmodel.signin_signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.User
import kz.project.domain.use_case.singup_signin.LoginWithEmailAndPasswordUseCase
import kz.project.domain.use_case.singup_signin.RegisterUserUseCase
import kz.project.domain.use_case.token.SaveAccessTokenUseCase
import kz.project.domain.use_case.validation.ValidateBirthdayUseCase
import kz.project.domain.use_case.validation.ValidateConfirmPasswordUseCase
import kz.project.domain.use_case.validation.ValidateEmailUseCase
import kz.project.domain.use_case.validation.ValidatePasswordUseCase
import kz.project.domain.use_case.validation.ValidateUsernameUseCase
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.Resource
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val validateBirthdayUseCase: ValidateBirthdayUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val saveAccessTokenUseCase: SaveAccessTokenUseCase,
) : ViewModel() {

    // TODO вынести в BaseViewModel
    private val compositeDisposable = CompositeDisposable()

    /** Результат валидации полей ввода регистрации */
    private val _registrationValidationResult = MutableLiveData<Resource<RegistrationForm>>()
    val registrationValidationResult: LiveData<Resource<RegistrationForm>>
        get() = _registrationValidationResult

    /** Результат регистрации на сервере */
    private val _registrationResult = MutableLiveData<Resource<User>>()
    val registrationResult: LiveData<Resource<User>>
        get() = _registrationResult

    /** Результат валидации полей ввода авторизации */
    private val _authenticationValidationResult = MutableLiveData<Resource<AuthenticationForm>>()
    val authenticationValidationResult: LiveData<Resource<AuthenticationForm>>
        get() = _authenticationValidationResult

    /** Результат авторизации */
    private val _authenticationResult = MutableLiveData<Resource<AccessToken>>()
    val authenticationResult: LiveData<Resource<AccessToken>>
        get() = _authenticationResult

    /**
     * Валидирует введённые пользователем данные на фрагменте регистрации
     */
    fun validateRegistrationInputs(registrationForm: RegistrationForm) {
        _registrationValidationResult.value = Resource.Loading()


        val usernameValidationResult = validateUsernameUseCase.invoke(registrationForm.username)
        val emailValidationResult = validateEmailUseCase.invoke(registrationForm.email)
        val birthdayValidationResult = validateBirthdayUseCase.invoke(registrationForm.birthday)
        val passwordValidationResult = validatePasswordUseCase.invoke(registrationForm.email)
        val confirmPasswordValidationResult = validateConfirmPasswordUseCase.invoke(
            registrationForm.password, registrationForm.confirmedPassword
        )

        val hasError = listOf(
            usernameValidationResult,
            emailValidationResult,
            birthdayValidationResult,
            passwordValidationResult,
            confirmPasswordValidationResult,
        ).any { !it.successful }

        if (hasError) {
            val validationError = registrationForm.copy(
                usernameError = usernameValidationResult.errorMessage,
                emailError = emailValidationResult.errorMessage,
                passwordError = passwordValidationResult.errorMessage,
                confirmPasswordError = confirmPasswordValidationResult.errorMessage,
                birthdayError = birthdayValidationResult.errorMessage,
            )
            _registrationValidationResult.value = Resource.Error(
                "Forms validation Error", validationError
            )
            return
        }

        _registrationValidationResult.value = Resource.Success(registrationForm)

        registerUser(registrationForm)
    }

    /**
     * Регистрирует пользователя в api
     */
    private fun registerUser(registrationForm: RegistrationForm) {
        _registrationResult.value = Resource.Loading()

        registerUserUseCase(registrationForm.toUserToPost())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userResponse ->
                    _registrationResult.value = Resource.Success(userResponse)
                },
                { error ->
                    _registrationResult.value = Resource.Error(
                        error.message ?: "Unexpected error"
                    )
                }
            ).let(compositeDisposable::add)

    }

    /**
     * Валидирует введённые пользователем данные на фрагменте авторизации с помощью email и пароля.
     */
    fun validateAuthenticationInputs(authenticationForm: AuthenticationForm) {
        _authenticationValidationResult.value = Resource.Loading()

        val emailValidationResult = validateEmailUseCase.invoke(authenticationForm.email)
        val passwordValidationResult = validateUsernameUseCase.invoke(authenticationForm.password)

        val hasError = listOf(
            emailValidationResult,
            passwordValidationResult,
        ).any { !it.successful }

        if (hasError) {
            val validationError = authenticationForm.copy(
                emailError = emailValidationResult.errorMessage,
                passwordError = passwordValidationResult.errorMessage,
            )
            _authenticationValidationResult.value = Resource.Error(
                "Forms validation Error", validationError
            )
            return
        }

        _authenticationValidationResult.value = Resource.Success(authenticationForm)

        authenticateUser(authenticationForm)
    }

    private fun authenticateUser(authenticationForm: AuthenticationForm) {
        _authenticationResult.value = Resource.Loading()

        loginWithEmailAndPasswordUseCase.invoke(authenticationForm.email, authenticationForm.password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { accessTokenResponse ->
                    saveAccessTokenUseCase.invoke(accessTokenResponse)
                    _authenticationResult.value = Resource.Success(accessTokenResponse)
                },
                { error ->
                    _authenticationResult.value = Resource.Error(
                        error.message ?: Constants.UNEXPECTED_ERROR
                    )
                }
            ).let(compositeDisposable::add)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
