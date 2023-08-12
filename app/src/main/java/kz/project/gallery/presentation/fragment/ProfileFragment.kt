package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    /**
     * Настраивает функционал кнопок
     */
    private fun setupButtons() = binding.apply {
        // TODO
        toolbarSettingsButton.root.setOnClickListener(::goToSettingsFragment)
    }

    /**
     * Открывает фрагмент с настройками информации пользователя
     */
    private fun goToSettingsFragment(view: View?) {

        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<SettingsFragment>(R.id.mainActivityFragmentContainerView, SettingsFragment.FRAGMENT_TAG)
            addToBackStack(null)
        }

    }


    companion object {
        const val SAVE_BACK_STACK_PROFILE = "SAVE_BACK_STACK_PROFILE"
        const val FRAGMENT_TAG = "ProfileFragment"
    }
}