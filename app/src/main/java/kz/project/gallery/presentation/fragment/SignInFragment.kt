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
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSignInBinding
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.signin_signup.AuthenticationForm
import kz.project.gallery.presentation.viewmodel.signin_signup.LoginViewModel
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import javax.inject.Inject


class SignInFragment : Fragment(R.layout.fragment_sign_in) {


    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: LoginViewModel by viewModels { factory }

    private val binding: FragmentSignInBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        observeValidationResult()
        observeAuthenticationResult()
    }


    /**
     * Настраивает функциональность кнопок на экране
     */
    private fun setupButtons() = with(binding) {
        toolbarCancel.root.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        signUpButton.setOnClickListener {
            val fragment = parentFragmentManager.findFragmentByTag(SignUpFragment.FRAGMENT_TAG)

            if (fragment is SignUpFragment) {
                parentFragmentManager.popBackStack()
                return@setOnClickListener
            }

            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<SignUpFragment>(R.id.mainActivityFragmentContainerView, SignUpFragment.FRAGMENT_TAG)
                addToBackStack(null)
            }
        }

        forgotLoginText.setOnClickListener {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.functionality_under_development),
                Toast.LENGTH_SHORT
            ).show()
        }

        signInButton.setOnClickListener {
            authenticateUser()
        }

        progressBar.root.indeterminateDrawable = createCircularProgressDrawable(requireContext(), R.color.mainPink)
    }

    /**
     *  Отправляет AuthenticationForm в функцию ViewModel для валидации полей, при успешной валидации
     *  полей ViewModel отправит запрос в api
     */
    private fun authenticateUser(authenticationForm: AuthenticationForm = createAuthenticationForm()) {
        viewModel.validateAuthenticationInputs(authenticationForm)
    }

    /**
     * Создаёт AuthenticationForm - класс обертку для формы авторизации
     */
    private fun createAuthenticationForm(
        email: String = getInputEmail(), password: String = getInputPassword()
    ): AuthenticationForm {
        return AuthenticationForm(
            email = email, password = password
        )
    }

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputPassword() = binding.passwordEditText.text.toString()

    /**
     * Получает введённую строку из EditText
     */
    private fun getInputEmail() = binding.emailEditText.text.toString()

    /**
     * Обсервит LiveData из ViewModel
     */
    private fun observeValidationResult() {
        viewModel.authenticationValidationResult.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is Resource.Loading -> {
                    showProgressBar(flag = true)
                }

                is Resource.Error -> {
                    showErrors(value.data)
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
    private fun observeAuthenticationResult() {
        val context = requireContext()
        viewModel.authenticationResult.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is Resource.Loading -> Unit

                is Resource.Success -> {
                    showProgressBar(flag = false)

                    Toast.makeText(
                        context,
                        context.getString(R.string.successful_authentication),
                        Toast.LENGTH_SHORT
                    ).show()

                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<MainFragment>(R.id.mainActivityFragmentContainerView, MainFragment.FRAGMENT_TAG)
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
    private fun showErrors(errors: AuthenticationForm?) = with(binding) {
        textInputLayoutEmail.error = errors?.emailError
        textInputLayoutPassword.error = errors?.passwordError
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
        emailEditText.isEnabled = !flag
        passwordEditText.isEnabled = !flag
        signUpButton.isEnabled = !flag
        signInButton.isEnabled = !flag
    }

    companion object {
        const val FRAGMENT_TAG = "SignInFragment"
    }
}