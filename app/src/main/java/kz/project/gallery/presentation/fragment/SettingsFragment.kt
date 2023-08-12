package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

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
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val FRAGMENT_TAG = "SettingsFragment"
    }
}