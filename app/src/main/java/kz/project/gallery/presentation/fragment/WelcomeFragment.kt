package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.View
import android.view.animation.AnimationUtils
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val binding: FragmentWelcomeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = ChangeBounds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAnimations()
        setupButtons()
    }

    private fun setupButtons() = binding.apply {

        createAccountButton.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                replace<SignUpFragment>(R.id.mainActivityFragmentContainerView)
                addToBackStack(null)
            }
        }

        alreadyHaveAccountButton.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                replace<SignInFragment>(R.id.mainActivityFragmentContainerView)
                addToBackStack(null)
            }
        }
    }

    private fun setupAnimations() = binding.apply {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animaton_1000)
        welcomeTextView.startAnimation(anim)
        createAccountButton.startAnimation(anim)
        alreadyHaveAccountButton.startAnimation(anim)
    }
}