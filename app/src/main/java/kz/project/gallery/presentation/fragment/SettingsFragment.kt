package kz.project.gallery.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSettingsBinding
import kz.project.gallery.presentation.fragment.base_fragmnents.PhotoCaptureFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.settings.SettingsViewModel
import javax.inject.Inject


class SettingsFragment : PhotoCaptureFragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: SettingsViewModel by viewModels { factory }

    private val binding: FragmentSettingsBinding by viewBinding()
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDialog = createBottomSheetDialog(requireContext())
        alertDialog = createAlertDialog(requireContext())

        setupWidgets()
    }

    /**
     * Настраивает функционал кнопок
     */
    private fun setupWidgets() = binding.apply {

        toolbarCancel.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        signOutTextView.setOnClickListener {
            alertDialog.show()
        }

        binding.profileAvatarImageView.root.setOnClickListener {
            bottomSheetDialog.show()
        }
    }


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