package kz.project.gallery.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.user.User
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoDetailsBinding
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.user.UserViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.GlideApp
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import kz.project.gallery.utils.parseDate
import javax.inject.Inject


class PhotoDetailsFragment : Fragment(R.layout.fragment_photo_details) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: UserViewModel by activityViewModels { factory }

    private val binding: FragmentPhotoDetailsBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = arguments?.getSerializable(PHOTO_TAG) as? Photo

        setupWidgets()

        observeUserResult(photo)
    }

    private fun setupWidgets() = binding.apply {
        toolbarArrowBack.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        loadingProgressBar.root.indeterminateDrawable =
            createCircularProgressDrawable(requireContext(), R.color.mainPink)
    }

    private fun observeUserResult(photo: Photo?) = photo?.let {

        viewModel.getUserById(photo.user)

        viewModel.userById.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }

                is Resource.Error -> {
                    showProgressBar(false)
                    setupWidgets(photo, null)
                }

                is Resource.Success -> {
                    showProgressBar(false)
                    setupWidgets(photo, resource.data)
                }
            }
        }
    }


    private fun showProgressBar(flag: Boolean) = binding.apply {
        loadingProgressBar.root.isVisible = flag
    }

    private fun setupWidgets(photo: Photo, user: User?) = binding.apply {

        val loadingUrl = "${Constants.IMAGE_LOADING_URL}${photo.image.name}"
        GlideApp.with(requireContext())
            .load(loadingUrl)
            .placeholder(ColorDrawable(Color.TRANSPARENT))
            .fitCenter()
            .into(photoImageView)

        photoNameTextView.text = photo.name
        userNameTextView.text = user?.username ?: requireContext().getString(R.string.undefined_user)
        viewsCountTextView.isVisible = true
        viewsIconImageView.isVisible = true
        binding.dateCreatedTextView.text = parseDate(photo.dateCreate)
        photoDescriptionTextView.text = photo.description
        addChipToChipGroup(createChip("New:${photo.new}"))
        addChipToChipGroup(createChip("Popular:${photo.popular}"))
    }

    /**
     * Добавляет chip в chipGroup
     */
    private fun addChipToChipGroup(chip: Chip) = binding.chipGroup.addView(chip)

    /**
     * Создает chip с назавнием тэга
     */
    private fun createChip(tagName: String) = Chip(requireContext()).apply {
        text = tagName
        isCloseIconVisible = false
        setChipBackgroundColorResource(R.color.mainPink)
        setTextColor(requireContext().getColor(R.color.white))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoDetailsFragment"
        const val PHOTO_TAG = "Photo"
    }
}