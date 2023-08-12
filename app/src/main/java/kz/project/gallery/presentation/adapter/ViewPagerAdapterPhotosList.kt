package kz.project.gallery.presentation.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.project.gallery.presentation.fragment.PhotoListFragment

/**
 * Адаптер, для ViewPager2, используемый во фрагменте со списком фотографий
 */
class ViewPagerAdapterPhotosList(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {

        0 -> PhotoListFragment().apply {
            arguments = bundleOf(PhotoListFragment.NEW to true)
        }

        1 -> PhotoListFragment().apply {
            arguments = bundleOf(PhotoListFragment.POPULAR to true)
        }

        else -> PhotoListFragment().apply {
            arguments = bundleOf(PhotoListFragment.NEW to true)
        }
    }
}