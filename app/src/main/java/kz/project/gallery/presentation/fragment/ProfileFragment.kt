package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.user.User
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentProfileBinding
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.fragment.base_fragmnents.PagingPhotoFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.profile.ProfileViewModel
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.createCircularProgressDrawable
import kz.project.gallery.utils.parseDate
import javax.inject.Inject


class ProfileFragment : PagingPhotoFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: ProfileViewModel by viewModels { factory }


    private var isAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupRecyclerView(requireContext(), PhotoItemType.SmallItemFourColumns, 4, binding.recyclerView)

        observeUserPhotos()
        observeCurrentUser()

        if (!isAlreadyLoaded) {
            viewModel.getCurrentUser()
            isAlreadyLoaded = true
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override val getPagingData: () -> Unit
        get() = { Unit }

    private fun observeUserPhotos() {

        viewModel.photosByUserIdLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> Unit

                is Resource.Error -> {
                    hideProgressBar()
                    showError(true)
                }

                is Resource.Success -> {
                    hideProgressBar()
                    if (resource.data?.photos?.isEmpty() == true) showError(true)
                    setLoadedPhotosCount(resource.data?.photos?.size ?: 0)
                    photoAdapter.submitList(resource.data?.photos?.map { it.copy() })
                }
            }
        }
    }

    private fun setLoadedPhotosCount(loadedPhotosCount: Int) = binding.apply {
        val count = if (loadedPhotosCount > 999) "999+" else loadedPhotosCount.toString()
        loadedCountTextView.text = requireContext().getString(R.string.profile_loaded, count)
    }

    private fun hideProgressBar() = binding.apply {
        loadingProgressBar.root.isVisible = false
    }


    private fun observeCurrentUser() {

        viewModel.currentUserLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> Unit

                is Resource.Error -> {
                    showError(true)
                }

                is Resource.Success -> {
                    setupWidgets(resource.data)
                    viewModel.getPhotosByUserId(resource.data?.id ?: -1)
                }
            }
        }
    }

    private fun setupWidgets(user: User?) = binding.apply {
        registrationDateTextView.text =
            if (user?.birthday?.isNotEmpty() == true) {
                parseDate(user.birthday.toString())
            } else {
                requireContext().getString(R.string.date_created_plug)
            }
        userNameTextView.text = user?.username ?: requireContext().getString(R.string.undefined_user)
    }

    private fun showError(flag: Boolean) = binding.apply {
        loadingError.root.isVisible = flag
    }


    /**
     * Настраивает функционал кнопок
     */
    private fun setupButtons() = binding.apply {

        val progressBar = loadingProgressBar.root.findViewById<ProgressBar>(R.id.progressBarInLoadingProgressBar)
        progressBar.indeterminateDrawable = createCircularProgressDrawable(requireContext(), R.color.mainGray)

        toolbarSettingsButton.root.setOnClickListener(::goToSettingsFragment)
    }

    /**
     * Открывает фрагмент с настройками информации пользователя
     */
    private fun goToSettingsFragment(view: View?) {

        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<SettingsFragment>(R.id.mainActivityFragmentContainerView, SettingsFragment.FRAGMENT_TAG)
            addToBackStack(null)
        }

    }

    companion object {
        const val FRAGMENT_TAG = "ProfileFragment"
    }
}