package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentHomeBinding
import kz.project.gallery.presentation.adapter.ViewPagerAdapterPhotosList
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.photo.HomeViewModel
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: HomeViewModel by activityViewModels { factory }

    private val binding: FragmentHomeBinding by viewBinding()

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
        // TODO("Not yet implemented")
    }

    companion object {
        const val FRAGMENT_TAG = "HomeFragment"
    }
}