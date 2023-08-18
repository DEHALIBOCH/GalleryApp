package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.domain.model.photo.Photo
import kz.project.domain.use_case.photo.GetPhotosByNameUseCase
import kz.project.domain.use_case.photo.GetPhotosListUseCase
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.viewmodel.home.HomeViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.RecyclerViewScrollListener
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.SearchQueryEventBus
import kz.project.gallery.utils.createCircularProgressDrawable
import javax.inject.Inject


class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {

    @Inject
    lateinit var getPhotosListUseCase: GetPhotosListUseCase

    @Inject
    lateinit var getPhotosByNameUseCase: GetPhotosByNameUseCase


    private val popular: Boolean by lazy { arguments?.getBoolean(POPULAR) ?: false }

    private val factory: HomeViewModel.Factory by lazy {
        HomeViewModel.Factory(
            popular = popular,
            getPhotosListUseCase = getPhotosListUseCase,
            getPhotosByNameUseCase = getPhotosByNameUseCase,
        )
    }
    private val viewModel: HomeViewModel by viewModels { factory }

    private val binding: FragmentPhotoListBinding by viewBinding()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var disposable: Disposable

    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProgressBar()
        observePhotosResult()
        setupRecyclerView()
        setupSwipeRefresh()
        observeSearchQuery()
    }

    private fun setupProgressBar() = binding.apply {
        progressBar.indeterminateDrawable = createCircularProgressDrawable(requireContext(), R.color.mainGray)
    }

    /**
     * Обсервит запросы приходящие из фрагмента с searchView
     */
    private fun observeSearchQuery() {
        disposable = SearchQueryEventBus.searchTextObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { query ->
                    viewModel.getPhotosByName(query)
                },
                { error ->
                    Log.e(Constants.SEARCH_QUERY_ERROR, error.message ?: Constants.UNEXPECTED_ERROR)
                }
            )
    }

    private fun setupSwipeRefresh() = binding.swipeRefreshLayout.apply {
        setOnRefreshListener { viewModel.refreshPhotos() }
        setColorSchemeResources(R.color.mainPink)
        setProgressBackgroundColorSchemeResource(R.color.grayLight)
    }

    private fun observePhotosResult() = viewModel.photosLiveData.observe(viewLifecycleOwner) { resource ->
        when (resource) {

            is Resource.Loading -> {
                isLoading = true
                showErrorNotification(false)
            }

            is Resource.Error -> {
                isLoading = false
                hideSwipeRefresh()
                hideProgressBar()
                showErrorNotification(true)
            }

            is Resource.Success -> {
                isLoading = false
                hideSwipeRefresh()
                hideProgressBar()
                showErrorNotification(false)
                isLastPage = viewModel.photosPage == viewModel.maxPhotosPage
                photoAdapter.submitList(resource.data?.map { it.copy() })
            }
        }
    }

    private fun hideSwipeRefresh() = binding.apply {
        swipeRefreshLayout.isRefreshing = false
    }

    private val recyclerViewScrollListener = object : RecyclerViewScrollListener(
        { viewModel.getPagingPhotos() }
    ) {
        override fun isLoading(): Boolean = isLoading

        override fun isLastPage(): Boolean = isLastPage
    }

    private fun showErrorNotification(flag: Boolean) = binding.apply {
        loadingError.isVisible = flag
        recyclerView.isVisible = !flag
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

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoListFragment"
        const val POPULAR = "Popular"
    }

}