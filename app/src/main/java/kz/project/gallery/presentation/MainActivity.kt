package kz.project.gallery.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.fragment.app.commit
import kz.project.gallery.R
import kz.project.gallery.databinding.ActivityMainBinding
import kz.project.gallery.presentation.fragment.CreatePhotoFragment
import kz.project.gallery.presentation.fragment.HomeFragment
import kz.project.gallery.presentation.fragment.MainFragment
import kz.project.gallery.presentation.fragment.SignInFragment
import kz.project.gallery.presentation.fragment.SignUpFragment
import kz.project.gallery.presentation.fragment.SplashFragment
import kz.project.gallery.presentation.fragment.WelcomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            replace(R.id.mainActivityFragmentContainerView, SplashFragment())
        }

    }
}