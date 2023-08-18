package kz.project.gallery.presentation.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentMainBinding
import kz.project.gallery.presentation.fragment.base_fragmnents.PhotoCaptureFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.user.UserViewModel
import javax.inject.Inject

class MainFragment : PhotoCaptureFragment(R.layout.fragment_main) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: UserViewModel by activityViewModels { factory }

    private val binding: FragmentMainBinding by viewBinding()
    private lateinit var bottomSheetDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDialog = createBottomSheetDialog(requireContext())
        addFragmentIfContainerIsEmpty()
        binding.bottomNavigationView.setOnItemSelectedListener(navigationListener)
    }


    /**
     * Если контейнер пустой, добавит фрагмент
     */
    private fun addFragmentIfContainerIsEmpty() {
        if (childFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) == null) {
            childFragmentManager.commit {
                replace<HomeFragment>(R.id.mainFragmentFragmentContainerView, HomeFragment.FRAGMENT_TAG)
            }
        }
    }

    /**
     * NavigationListener для BottomNavigationView
     */
    private val navigationListener = NavigationBarView.OnItemSelectedListener { menuItem ->
        when (menuItem.itemId) {

            R.id.homeBottomNavBar -> {
                replaceFragment<HomeFragment>(HomeFragment.FRAGMENT_TAG)
                true
            }

            R.id.photoBottomNavBar -> {
                bottomSheetDialog.show()
                false
            }

            R.id.profileBottomNavBar -> {
                replaceFragment<ProfileFragment>(ProfileFragment.FRAGMENT_TAG)
                true
            }

            R.id.favoritesBottomNavBar -> {
                replaceFragment<FavoritesFragment>(FavoritesFragment.FRAGMENT_TAG)
                true
            }

            else -> {
                false
            }
        }

    }

    /**
     * Переставляет фрагмент предварительно проверяя, был ли он создан до этого.
     * @param tag - Тэг привязанный к фрагменту, по нему будет произведен поиск в бэк стеке
     */
    private inline fun <reified T : Fragment> replaceFragment(tag: String) {

        val fragment = childFragmentManager.findFragmentByTag(tag)

        if (fragment is T) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.mainFragmentFragmentContainerView, fragment, tag)
                addToBackStack(null)
            }
            return
        }

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(R.id.mainFragmentFragmentContainerView, tag)
            addToBackStack(null)
        }

    }

    override fun photoCaptured(photoUri: Uri) {
        bottomSheetDialog.dismiss()
        goToPhotoCreatingFragment(photoUri)
    }

    private fun goToPhotoCreatingFragment(photoUri: Uri) = requireActivity().supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<CreatePhotoFragment>(
            R.id.mainActivityFragmentContainerView,
            CreatePhotoFragment.FRAGMENT_TAG,
            bundleOf(CreatePhotoFragment.PHOTO_URI to photoUri)
        )
        addToBackStack(null)
    }


    companion object {
        const val FRAGMENT_TAG = "MainFragment"
    }
}