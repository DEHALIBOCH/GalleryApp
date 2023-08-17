package kz.project.gallery.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import kz.project.domain.model.user.User
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSettingsBinding
import kz.project.gallery.presentation.fragment.base_fragmnents.PhotoCaptureFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.profile.ProfileViewModel
import kz.project.gallery.presentation.viewmodel.profile.UpdatePasswordValidationForm
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import kz.project.gallery.utils.parseDate
import javax.inject.Inject


class SettingsFragment : PhotoCaptureFragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { factory }

    private val binding: FragmentSettingsBinding by viewBinding()
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var alertDialog: AlertDialog
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDialog = createBottomSheetDialog(requireContext())
        alertDialog = createAlertDialog(requireContext())

        setupWidgets()

        if (viewModel.currentUser == null) {
            viewModel.getCurrentUser()
        }

        observeUserResult()
        observePasswordUpdateValidation()
        observePasswordUpdate()

        // TODO работа над удалением пользователя
    }

    /**
     * Настраивает функционал кнопок
     */
    private fun setupWidgets() = binding.apply {

        usernameEditText.isEnabled = false
        birthdayEditText.isEnabled = false
        emailEditText.isEnabled = false

        toolbarCancel.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        signOutTextView.setOnClickListener {
            alertDialog.show()
        }

        profileAvatarImageView.root.setOnClickListener {
            bottomSheetDialog.show()
        }

        toolbarSave.setOnClickListener {
            updateUserPassword()
        }

        loadingProgressBar.indeterminateDrawable =
            createCircularProgressDrawable(requireContext(), R.color.mainPink)
    }

    /**
     * Обновляет пароль пользователя
     */
    private fun updateUserPassword() {
        val updatePasswordValidationForm = createUpdatePasswordValidationForm()
        currentUser?.let {
            viewModel.validateUpdatePassword(it.id, updatePasswordValidationForm)
        }
    }

    /**
     *
     */
    private fun observePasswordUpdateValidation() {
        viewModel.passwordUpdateValidationResult.observe(viewLifecycleOwner) { resourse ->
            when (resourse) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }

                is Resource.Error -> {
                    showProgressBar(false)
                    setErrorsToInputFields(resourse.data)
                }

                is Resource.Success -> {
                    showProgressBar(true)
                    setErrorsToInputFields(resourse.data)
                }
            }
        }
    }

    /**
     * Обсервит статус обновления пароля на сервере
     */
    private fun observePasswordUpdate() {
        viewModel.passwordUpdatingResult.observe(viewLifecycleOwner) { resourse ->
            when (resourse) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }

                is Resource.Error -> {
                    showProgressBar(false)
                    Toast.makeText(
                        requireContext(),
                        resourse.message ?: requireContext().getString(R.string.something_went_wrong_pls_try_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Success -> Unit
            }
        }
    }

    private fun setErrorsToInputFields(updatePasswordValidationForm: UpdatePasswordValidationForm?) =
        binding.apply {
            textInputLayoutOldPassword.error = updatePasswordValidationForm?.oldPasswordError
            textInputLayoutNewPassword.error = updatePasswordValidationForm?.newPasswordError
            textInputLayoutConfirmNewPassword.error = updatePasswordValidationForm?.confirmNewPasswordError
        }

    private fun createUpdatePasswordValidationForm() = UpdatePasswordValidationForm(
        oldPassword = binding.oldPasswordEditText.text.toString(),
        newPassword = binding.newPasswordEditText.text.toString(),
        confirmNewPassword = binding.confirmNewPasswordEditText.text.toString(),
    )

    /**
     * Обсервит результат получения текущего пользователя
     */
    private fun observeUserResult() {
        viewModel.currentUserLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }

                is Resource.Error -> {
                    showProgressBar(false)
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.something_went_wrong_pls_try_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Success -> {
                    showProgressBar(false)
                    currentUser = resource.data
                    setUserInformation(currentUser)
                }
            }
        }
    }

    /**
     * Устанавливает данные о пользователе в поля
     */
    private fun setUserInformation(user: User?) = binding.apply {
        user?.let {
            usernameEditText.setText(it.username)
            it.birthday?.let { birthday -> birthdayEditText.setText(parseDate(birthday)) }
            emailEditText.setText(it.email)
        }
    }

    private fun showProgressBar(flag: Boolean) {
        binding.loadingProgressBar.isVisible = flag
        isWidgetsEnabled(!flag)
    }

    private fun isWidgetsEnabled(flag: Boolean) = binding.apply {
        toolbarCancel.root.isEnabled = flag
        toolbarSave.isEnabled = flag
        profileAvatarImageView.root.isEnabled = flag
        oldPasswordEditText.isEnabled = flag
        newPasswordEditText.isEnabled = flag
        confirmNewPasswordEditText.isEnabled = flag
        deleteAccountTextView.isEnabled = flag
        signOutTextView.isEnabled = flag
    }

    /**
     * Проверяет введенные пользователем поля ввода пароля
     */


    /**
     * Создает AlertDialog который, показывается когда user, нажимает на sign out
     */
    private fun createAlertDialog(context: Context): AlertDialog = AlertDialog.Builder(context).apply {

        setTitle(context.getString(R.string.sign_out))
        setMessage(context.getString(R.string.sure_to_sign_out))

        setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
            viewModel.signOut()
            goToWelcomeFragment()
            dialog.dismiss()
        }
        setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

    }.create().apply {
        setOnShowListener { dialog ->
            val pinkColor = context.getColor(R.color.mainPink)
            (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(pinkColor)
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(pinkColor)
        }
    }


    /**
     * После выхода из аккаунта отправляет пользователя на WelcomeFragment
     */
    private fun goToWelcomeFragment() = requireActivity().supportFragmentManager.apply {

        popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        commit {
            setReorderingAllowed(true)
            replace<WelcomeFragment>(
                R.id.mainActivityFragmentContainerView,
                WelcomeFragment.FRAGMENT_TAG,
            )
        }
    }

    override fun photoCaptured(photoUri: Uri) {
        bottomSheetDialog.dismiss()

        Glide.with(requireContext())
            .load(photoUri)
            .circleCrop()
            .into(binding.profileAvatarImageView.root)
    }


    companion object {
        const val FRAGMENT_TAG = "SettingsFragment"
    }
}