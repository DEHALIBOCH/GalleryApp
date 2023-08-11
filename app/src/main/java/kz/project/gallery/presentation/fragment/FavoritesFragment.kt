package kz.project.gallery.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import kz.project.gallery.R


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        const val FRAGMENT_TAG = "FavoritesFragment"
    }
}