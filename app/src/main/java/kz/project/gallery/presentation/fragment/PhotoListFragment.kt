package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kz.project.domain.model.photo.Photo
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.viewmodel.home.HomeViewModel
import kz.project.gallery.utils.Resource
import javax.inject.Inject


class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {

    @Inject
    lateinit var getPhotosListUseCase: GetPhotosListUseCase
    private var popular: Boolean = false

    private val factory: HomeViewModel.Factory by lazy {
        HomeViewModel.Factory(popular, getPhotosListUseCase)
    }
    private val viewModel: HomeViewModel by viewModels { factory }

    private val binding: FragmentPhotoListBinding by viewBinding()
    private lateinit var photoAdapter: PhotoAdapter

    private var isAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popular = arguments?.getBoolean(POPULAR) ?: false

        observePhotosResult()
        setupRecyclerView()

        if (!isAlreadyLoaded) {
            viewModel.getPhotos()
            isAlreadyLoaded = true
        }
    }

    private fun observePhotosResult() = viewModel.photosLiveData.observe(viewLifecycleOwner) { resource ->
        when (resource) {

            is Resource.Loading -> {
                showProgressBar(true)
                showErrorNotification(false)
            }

            is Resource.Error -> {
                showProgressBar(false)
                showErrorNotification(true)
            }

            is Resource.Success -> {
                showProgressBar(false)
                showErrorNotification(false)
                photoAdapter.submitList(resource.data?.photos)
            }
        }
    }


    private fun showErrorNotification(flag: Boolean) {
        binding.loadingError.isVisible = flag
    }

    private fun showProgressBar(flag: Boolean) {
        binding.loadingProgressBar.isVisible = flag
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter(PhotoItemType.BigItemTwoColumns)
        photoAdapter.setOnItemClickListener { photo ->
            goToPhotoDetailsFragment(photo)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = photoAdapter
    }

    private fun goToPhotoDetailsFragment(photo: Photo) = requireActivity().supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<PhotoDetailsFragment>(
            R.id.mainActivityFragmentContainerView,
            PhotoDetailsFragment.FRAGMENT_TAG,
            bundleOf(PhotoDetailsFragment.PHOTO_TAG to photo)
        )
        addToBackStack(null)
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoListFragment"
        const val POPULAR = "Popular"
    }

}