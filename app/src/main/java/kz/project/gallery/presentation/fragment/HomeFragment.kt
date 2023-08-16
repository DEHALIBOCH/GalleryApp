package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentHomeBinding
import kz.project.gallery.presentation.adapter.ViewPagerAdapterPhotosList
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.home.HomeViewModel
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.SearchQueryEventBus
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: HomeViewModel by activityViewModels { factory }

    private val binding: FragmentHomeBinding by viewBinding()
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        setupViewPager()
    }

    /**
     * Настраивает ViewPager
     */
    private fun setupViewPager() = binding.apply {

        viewPager.adapter = ViewPagerAdapterPhotosList(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.tab_new)
                1 -> requireContext().getString(R.string.tab_popular)
                else -> throw IllegalArgumentException()
            }
        }.attach()
    }

    /**
     * Настраивает searchView
     */
    private fun setupSearchView() {
        disposable = binding.searchEditText.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { query ->
                    if (query.isNotBlank()) SearchQueryEventBus.sendSearchText(query.toString())
                },
                { error ->
                    Log.e(Constants.SEARCH_QUERY_ERROR, error.message ?: Constants.UNEXPECTED_ERROR)
                }
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        const val FRAGMENT_TAG = "HomeFragment"
    }
}