package kz.project.gallery.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import kz.project.gallery.R
import kz.project.gallery.databinding.ActivityMainBinding
import kz.project.gallery.presentation.fragment.SignInFragment
import kz.project.gallery.presentation.fragment.WelcomeFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainActivityFragmentContainerView, SignInFragment())
            .commit()
    }
}