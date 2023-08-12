package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kz.project.gallery.GalleryApp
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSplashBinding
import kz.project.gallery.presentation.viewmodel.access_token.AccessTokenViewModel
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var factory: MultiViewModelFactory
    private val viewModel: AccessTokenViewModel by viewModels { factory }

    private val binding: FragmentSplashBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as GalleryApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAnimations()
        checkAuthorization()

    }

    /**
     * Проверяет, был ли авторизован пользователь
     */
    private fun checkAuthorization() {
        viewModel.refreshTokenWithDelay()

        viewModel.authenticationToken.observe(viewLifecycleOwner, Observer { token ->
            val isAuthorized = token.refreshToken.isNotBlank() && token.accessToken.isNotBlank()

            fragmentSwitchBasedOnAuthorizationStatus(isAuthorized)
        })
    }

    /**
     * Исходя из-из того был ли авторизован, меняет фрагмент
     */
    private fun fragmentSwitchBasedOnAuthorizationStatus(isAuthorized: Boolean) = if (isAuthorized) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace<MainFragment>(R.id.mainActivityFragmentContainerView, MainFragment.FRAGMENT_TAG)
        }
    } else {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addSharedElement(binding.logo, "logo_destination")
            replace<WelcomeFragment>(R.id.mainActivityFragmentContainerView, WelcomeFragment.FRAGMENT_TAG)
        }
    }


    private fun setupAnimations() = binding.apply {
        logo.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animaton_1000)
        )
    }

}