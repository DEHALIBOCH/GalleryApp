package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kz.project.gallery.presentation.fragment.base_fragmnents.PagingPhotoFragment
import kz.project.gallery.presentation.viewmodel.home.HomeViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.RecyclerViewScrollListener
import kz.project.gallery.utils.Resource
import kz.project.gallery.utils.SearchQueryEventBus
import kz.project.gallery.utils.createCircularProgressDrawable
import javax.inject.Inject


class PhotoListFragment : PagingPhotoFragment<FragmentPhotoListBinding>(R.layout.fragment_photo_list) {

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

    private lateinit var disposable: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProgressBar(binding.progressBar, R.color.mainPink, requireContext())
        observePhotosResult()
        setupRecyclerView(requireContext(), PhotoItemType.BigItemTwoColumns, 2, binding.recyclerView)
        setupSwipeRefresh()
        observeSearchQuery()
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

    private fun showErrorNotification(flag: Boolean) = binding.apply {
        loadingError.root.isVisible = flag
        recyclerView.isVisible = !flag
    }

    private fun hideProgressBar() {
        binding.loadingProgressBar.isVisible = false
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhotoListBinding.inflate(inflater, container, false)

    override val getPagingData: () -> Unit
        get() = { viewModel.getPagingPhotos() }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoListFragment"
        const val POPULAR = "Popular"
    }

}