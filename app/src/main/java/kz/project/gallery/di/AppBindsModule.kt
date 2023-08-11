package kz.project.gallery.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.project.gallery.presentation.viewmodel.AccessTokenViewModel
import kz.project.gallery.presentation.viewmodel.signin_signup.LoginViewModel

@Module
interface AppBindsModule {

    @[Binds IntoMap ViewModelKey(LoginViewModel::class)]
    fun loginRegistrationViewModel(loginViewModel: LoginViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(AccessTokenViewModel::class)]
    fun accessTokenViewModel(accessTokenViewModel: AccessTokenViewModel): ViewModel


}