package kz.project.gallery.presentation.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.viewbinding.library.fragment.viewBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
import kz.project.domain.model.photo.PhotoUploadForm
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentCreatePhotoBinding
import kz.project.gallery.presentation.fragment.base_fragmnents.FileCreatingFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.create_photo.CreatePhotoViewModel
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import kz.project.gallery.utils.parseDate
import java.util.Date
import javax.inject.Inject

class CreatePhotoFragment : FileCreatingFragment(R.layout.fragment_create_photo) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: CreatePhotoViewModel by viewModels { factory }

    private val binding: FragmentCreatePhotoBinding by viewBinding()

    private val photoUri: Uri? by lazy {
        arguments?.getParcelable<Uri>(PHOTO_URI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewPhoto.setImageURI(photoUri)

        setupWidgets()
        observePostPhotoResult()
    }

    private fun observePostPhotoResult() = viewModel.postPhotoLiveData.observe(viewLifecycleOwner) { resource ->
        when (resource) {

            is Resource.Loading -> {
                showProgressBar(true)
            }

            is Resource.Error -> {
                showProgressBar(false)
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.something_went_wrong_pls_try_later),
                    Toast.LENGTH_LONG
                ).show()
            }

            is Resource.Success -> {
                showProgressBar(false)
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.successfully_uploaded),
                    Toast.LENGTH_LONG
                ).show()
                goBack()
            }
        }
    }

    private fun showProgressBar(flag: Boolean) {
        binding.progressBar.root.isVisible = flag
        isWidgetsEnabled(!flag)
    }

    private fun isWidgetsEnabled(flag: Boolean) = binding.apply {
        toolbarArrowBack.root.isEnabled = flag
        toolbarAddPhoto.isEnabled = flag
        photoNameEditText.isEnabled = flag
        photoDescriptionEditText.isEnabled = flag
        chipIsNew.isEnabled = flag
        chipIsPopular.isEnabled = flag
        chipAddNewTag.isEnabled = flag
    }

    private fun goBack() = requireActivity().supportFragmentManager.popBackStack()

    private fun setupWidgets() = binding.apply {

        toolbarArrowBack.root.setOnClickListener { goBack() }

        toolbarAddPhoto.setOnClickListener { postPhoto() }

        binding.chipAddNewTag.setOnClickListener {
            if (!checkChipCount()) {
                Toast.makeText(
                    requireContext(), requireContext().getString(R.string.chip_max_count), Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            showBottomSheetDialog()
        }

        progressBar.root.indeterminateDrawable = createCircularProgressDrawable(requireContext(), R.color.mainPink)
    }


    /**
     * Отправляет фото, перед этим проведя проверки
     */
    private fun postPhoto() {
        if (!validInputs()) {
            return
        }
        val photoUploadForm = createPhotoUploadForm()
        val file = createTemporaryImage(photoUri, requireContext())

        file?.let { viewModel.postPhoto(file, photoUploadForm) }
    }

    private fun createPhotoUploadForm(): PhotoUploadForm = PhotoUploadForm(
        name = binding.photoNameEditText.text.toString(),
        dateCreate = parseDate(Date().time),
        description = binding.photoDescriptionEditText.text.toString(),
        new = binding.chipIsNew.isChecked,
        popular = binding.chipIsPopular.isChecked,
    )

    private fun validInputs(): Boolean = binding.let {
        var flag = true

        if (it.photoNameEditText.text.toString().isBlank()) {
            it.textInputLayoutName.error = requireContext().getString(R.string.empty_photo_name)
            flag = false
        }
        if (it.photoDescriptionEditText.text.toString().isBlank()) {
            it.textInputLayoutDescription.error = requireContext().getString(R.string.empty_photo_description)
            flag = false
        }
        if (!it.chipIsNew.isChecked && !it.chipIsPopular.isChecked) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.not_checked_new_or_popular_chip),
                Toast.LENGTH_SHORT
            ).show()
            flag = false
        }

        return flag
    }


    /**
     * Если в chipGroup находится меньше 6 chip возвращает true
     */
    private fun checkChipCount() = binding.chipGroup.childCount < 6


    /**
     * Создаёт и показывает BottomSheetDialog для добавления нового тэга
     */
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = Dialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_create_tag)

        val tagNameEditText = bottomSheetDialog.findViewById<EditText>(R.id.editTextTag)
        val tagNameInputLayout = bottomSheetDialog.findViewById<TextInputLayout>(R.id.textInputLayoutTag)
        val createTagButton = bottomSheetDialog.findViewById<Button>(R.id.createTagButton)

        createTagButton.setOnClickListener {
            val tagName = tagNameEditText.text.toString()

            if (tagName.isBlank()) {
                tagNameInputLayout.error = requireContext().getString(R.string.invalid_tag_name)
                return@setOnClickListener
            }

            tagNameEditText.error = null

            val chip = createChip(tagName)
            addChipToChipGroup(chip)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.bottom_sheet_dialog_background)
            attributes.windowAnimations = R.style.BottomSheetAnimation
            setGravity(Gravity.BOTTOM)
        }

        bottomSheetDialog.show()
    }

    /**
     * Добавляет chip в chipGroup
     */
    private fun addChipToChipGroup(chip: Chip) {
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
        }
        binding.chipGroup.addView(chip)
    }

    /**
     * Создает chip с назавнием тэга
     */
    private fun createChip(tagName: String) = Chip(requireContext()).apply {
        text = tagName
        isCloseIconVisible = true
        setChipBackgroundColorResource(R.color.mainPink)
        setTextColor(requireContext().getColor(R.color.white))
        setCloseIconTintResource(R.color.white)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }

    companion object {

        const val FRAGMENT_TAG = "CreatePhotoFragment"
        const val PHOTO_URI = "Photo uri"
    }
}