package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSignUpBinding
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.signin_signup.LoginViewModel
import kz.project.gallery.presentation.viewmodel.signin_signup.RegistrationForm
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import javax.inject.Inject


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: LoginViewModel by viewModels { factory }

    private val binding: FragmentSignUpBinding by viewBinding()
    private lateinit var disposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()

        observeValidationResult()
        observeRegistrationResult()
    }

    /**
     * Обсервит LiveData из ViewModel
     */
    private fun observeValidationResult() {
        viewModel.registrationValidationResult.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is Resource.Loading -> {
                    showProgressBar(flag = true)
                }

                is Resource.Error -> {
                    val errors = value.data
                    showErrors(errors)
                    showProgressBar(flag = false)
                }

                is Resource.Success -> {
                    val errors = value.data
                    showErrors(errors)
                }
            }
        })
    }


    /**
     * Если валидация прошла успешно обсервит результат регистрации
     */
    private fun observeRegistrationResult() {
        val context = requireContext()
        viewModel.registrationResult.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    showProgressBar(flag = false)
                    Toast.makeText(
                        context,
                        context.getString(R.string.successful_registration),
                        Toast.LENGTH_SHORT
                    ).show()

                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SignInFragment>(R.id.mainActivityFragmentContainerView)
                    }
                }

                is Resource.Error -> {
                    showProgressBar(false)
                    val message = value.message ?: "Unexpected Error"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * Вещает error на TextInputLayout
     */
    private fun showErrors(errors: RegistrationForm?) = errors?.let { error ->
        binding.textInputLayoutUsername.error = error.usernameError
        binding.textInputLayoutEmail.error = error.emailError
        binding.textInputLayoutPassword.error = error.passwordError
        binding.textInputLayoutConfirmPassword.error = error.confirmPasswordError
        binding.textInputLayoutBirthday.error = error.birthdayError
    }


    /**
     * Показывает прогресс бар и убирает возможность нажатия на виджеты экрана
     */
    private fun showProgressBar(flag: Boolean) = binding.apply {
        isWidgetsEnabled(flag)
        progressBar.root.isVisible = flag
    }

    private fun isWidgetsEnabled(flag: Boolean) = binding.apply {
        toolbarCancel.root.isEnabled = !flag
        usernameEditText.isEnabled = !flag
        birthdayEditText.isEnabled = !flag
        emailEditText.isEnabled = !flag
        passwordEditText.isEnabled = !flag
        confirmPasswordEditText.isEnabled = !flag
        signUpButton.isEnabled = !flag
        signInButton.isEnabled = !flag
    }

    /**
     * Настраивает функциональность кнопок на экране
     */
    private fun setupButtons() = binding.apply {

        toolbarCancel.root.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        signInButton.setOnClickListener {
            val fragment = parentFragmentManager.findFragmentByTag(SignInFragment.FRAGMENT_TAG)

            if (fragment is SignInFragment) {
                parentFragmentManager.popBackStack()
                return@setOnClickListener
            }

            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<SignInFragment>(R.id.mainActivityFragmentContainerView)
                addToBackStack(null)
            }
        }

        signUpButton.setOnClickListener {
            registerUser()
        }

        progressBar.root.indeterminateDrawable = createCircularProgressDrawable(requireContext(), R.color.mainPink)

        setupBirthdayEditText()

    }

    private fun setupBirthdayEditText() = binding.apply {
        disposable = birthdayEditText.textChanges()
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { event ->

                },
                {

                }
            )
    }


    /**
     *  Отправляет RegistrationForm в функцию ViewModel для валидации полей, при успешной валидации
     *  полей ViewModel отправит запрос в api
     */
    private fun registerUser(registrationForm: RegistrationForm = createRegistrationForm()) {
        viewModel.validateRegistrationInputs(registrationForm)
    }

    /**
     * Создает RegistrationForm - класс обертку для формы регистрации
     */
    private fun createRegistrationForm(
        username: String = getInputUsername(),
        email: String = getInputEmail(),
        birthday: String = getInputBirthday(),
        password: String = getInputPassword(),
        passwordConfirmation: String = getInputPasswordConfirmation(),
    ): RegistrationForm {

        return RegistrationForm(
            username = username,
            email = email,
            birthday = birthday,
            password = password,
            confirmedPassword = passwordConfirmation
        )
    }

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputEmail() = binding.emailEditText.text.toString()

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputPasswordConfirmation() = binding.confirmPasswordEditText.text.toString()

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputPassword() = binding.passwordEditText.text.toString()

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputBirthday() = binding.birthdayEditText.text.toString()

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputUsername() = binding.usernameEditText.text.toString()

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    companion object {
        const val FRAGMENT_TAG = "SignUpFragment"
    }
}