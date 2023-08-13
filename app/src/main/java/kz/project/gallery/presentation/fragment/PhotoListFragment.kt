package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import kz.project.domain.model.photo.Photo
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.photo.HomeViewModel
import kz.project.gallery.utils.Resource
import javax.inject.Inject


class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: HomeViewModel by activityViewModels { factory }

    private val binding: FragmentPhotoListBinding by viewBinding()
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO рефактор, во вьюмодели 2 юз-кейса, можно обойтись одним и здесь использовать только один boolean
        val popular = arguments?.getBoolean(POPULAR) ?: false
        val new = arguments?.getBoolean(NEW) ?: false

        setupRecyclerView()
        getPhotos(popular, new)
        setupSwipeToRefresh(popular, new)
    }

    private fun setupSwipeToRefresh(popular: Boolean, new: Boolean) = binding.swipeRefresh.apply {
        setColorSchemeColors(requireContext().getColor(R.color.mainPink), requireContext().getColor(R.color.mainGray))
        setOnRefreshListener {
            getPhotos(popular, new)
        }
    }

    private fun getPhotos(popular: Boolean, new: Boolean) {
        // TODO рефактор, во вьюмодели 2 юз-кейса, можно обойтись одним и здесь использовать только один boolean
        if (popular) {
            viewModel.getPopularPhotos()
            observePopularPhotosResult()
        } else {
            viewModel.getNewPhotos()
            observeNewPhotosResult()
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
                    hideSwapRefresh()
                }

                is Resource.Success -> {
                    showProgressBar(false)
                    showErrorNotification(false)
                    submitDataWithAdapter(resource.data)
                    hideSwapRefresh()
                }
            }
        }
    }

    private fun hideSwapRefresh() {
        binding.swipeRefresh.isRefreshing = false
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
        adapter.submitData(lifecycle, it)
    }

    private fun showErrorNotification(flag: Boolean) {
        binding.loadingError.isVisible = flag
    }

    private fun showProgressBar(flag: Boolean) {
        binding.loadingProgressBar.isVisible = flag
    }

    private fun setupRecyclerView() {
        adapter = PhotoAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoListFragment"
        const val POPULAR = "Popular"
        const val NEW = "New"
    }

}