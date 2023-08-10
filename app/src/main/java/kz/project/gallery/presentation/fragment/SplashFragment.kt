package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAnimations()

        // TODO доработка функционала, убрать addToBackStack
        binding.logo.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                addSharedElement(binding.logo, "logo_destination")
                replace<WelcomeFragment>(R.id.mainActivityFragmentContainerView)
                addToBackStack(null)
            }
        }
    }

    private fun setupAnimations() = binding.apply {
        logo.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animaton_1000)
        )
    }

}