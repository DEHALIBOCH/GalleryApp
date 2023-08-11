package kz.project.gallery.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentMainBinding
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import kz.project.gallery.presentation.viewmodel.user.CurrentUserViewModel
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: CurrentUserViewModel by activityViewModels { factory }

    private val binding: FragmentMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) replaceFragment<HomeFragment>(HomeFragment.FRAGMENT_TAG)

        binding.bottomNavigationView.setOnItemSelectedListener(navigationListener)
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
                showBottomSheetDialog()
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

        val fragment = parentFragmentManager.findFragmentByTag(tag)

        if (fragment is T) {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.mainFragmentFragmentContainerView, fragment, tag)
                addToBackStack(null)
            }
            return
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(R.id.mainFragmentFragmentContainerView, tag)
            addToBackStack(null)
        }
    }

    /**
     * Создаёт и показывает BottomSheetDialog для добавления нового тэга
     */
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = Dialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_modal_photo)

        // TODO доработать функционал по кликам на кнопки

        bottomSheetDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.bottom_sheet_dialog_background)
            attributes.windowAnimations = R.style.BottomSheetAnimation
            setGravity(Gravity.BOTTOM)
        }

        bottomSheetDialog.show()
    }

    companion object {
        const val FRAGMENT_TAG = "MainFragment"
    }
}