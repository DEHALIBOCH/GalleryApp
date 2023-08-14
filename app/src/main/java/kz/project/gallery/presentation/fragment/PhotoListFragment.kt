package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import kz.project.domain.model.photo.Photo
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.photo.HomeViewModel
import kz.project.gallery.utils.Resource
import javax.inject.Inject


class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: HomeViewModel by activityViewModels { factory }

    private val binding: FragmentPhotoListBinding by viewBinding()
    private lateinit var photoAdapter: PhotoAdapter
    private var isAlreadyLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popular = arguments?.getBoolean(POPULAR) ?: false

        observeValidationResult(popular)
        setupRecyclerView()

        if (!isAlreadyLoaded) {
            getPhotos(popular)
            isAlreadyLoaded = true
        }
    }


    private fun observeValidationResult(popular: Boolean) {
        if (popular) observePopularPhotosResult()
        else observeNewPhotosResult()
    }

    private fun getPhotos(popular: Boolean) {
        if (popular) {
            viewModel.getPopularPhotos()
        } else {
            viewModel.getNewPhotos()
        }
    }

    private fun observeNewPhotosResult() {
        viewModel.newPhotosLiveData.observe(viewLifecycleOwner) { resource ->
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
                    submitDataWithAdapter(resource.data)
                }
            }
        }
    }


    private fun observePopularPhotosResult() {
        viewModel.popularPhotosLiveData.observe(viewLifecycleOwner) { resource ->
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
                    submitDataWithAdapter(resource.data)
                }
            }
        }
    }

    private fun submitDataWithAdapter(pagingData: PagingData<Photo>?) = pagingData?.let {
        photoAdapter.submitData(lifecycle, it)
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