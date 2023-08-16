package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.project.domain.model.photo.Photo
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.viewmodel.home.HomeViewModel
import kz.project.gallery.utils.Constants
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
        setupSwipeRefresh()

        if (!isAlreadyLoaded) {
            isAlreadyLoaded = true
        }
    }

    private fun setupSwipeRefresh() = binding.swipeRefreshLayout.apply {
        setOnRefreshListener {
            viewModel.refreshPhotos()
        }
        setColorSchemeResources(R.color.mainPink)
        setProgressBackgroundColorSchemeResource(R.color.grayLight)
    }

    private fun observePhotosResult() = viewModel.photosLiveData.observe(viewLifecycleOwner) { resource ->
        when (resource) {

            is Resource.Loading -> {
                showErrorNotification(false)
            }

            is Resource.Error -> {
                hideSwipeRefresh()
                hideProgressBar()
                showErrorNotification(true)
            }

            is Resource.Success -> {
                hideSwipeRefresh()
                hideProgressBar()
                showErrorNotification(false)
                isLastPage = viewModel.photosPage == viewModel.maxPhotosPage
                photoAdapter.submitList(resource.data?.map { it.copy() })
                if (isLastPage) removeRecyclerViewPadding()
            }
        }
    }

    private fun hideSwipeRefresh() = binding.apply {
        isLoading = false
        swipeRefreshLayout.isRefreshing = false
    }

    private fun removeRecyclerViewPadding() = binding.recyclerView.setPadding(0, 0, 0, 0)

    private var isLoading = false
    private var isScrolling = false
    private var isLastPage = false

    private val recyclerViewScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemsCount = layoutManager.childCount
            val totalItemsCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemsCount >= totalItemsCount - 2
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemsCount >= Constants.LIMIT
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                isLoading = true
                viewModel.getPagingPhotos()
                isScrolling = false
            }
        }
    }

    private fun showErrorNotification(flag: Boolean) {
        binding.loadingError.isVisible = flag
    }

    private fun hideProgressBar() {
        binding.loadingProgressBar.isVisible = false
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter(PhotoItemType.BigItemTwoColumns)
        photoAdapter.setOnItemClickListener { photo ->
            goToPhotoDetailsFragment(photo)
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = photoAdapter
            addOnScrollListener(recyclerViewScrollListener)
        }
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