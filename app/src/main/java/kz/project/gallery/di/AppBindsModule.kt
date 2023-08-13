package kz.project.gallery.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.project.gallery.presentation.viewmodel.access_token.AccessTokenViewModel
import kz.project.gallery.presentation.viewmodel.photo.HomeViewModel
import kz.project.gallery.presentation.viewmodel.signin_signup.LoginViewModel
import kz.project.gallery.presentation.viewmodel.user.UserViewModel

@Module
interface AppBindsModule {

    @[Binds IntoMap ViewModelKey(LoginViewModel::class)]
    fun loginRegistrationViewModel(loginViewModel: LoginViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(AccessTokenViewModel::class)]
    fun accessTokenViewModel(accessTokenViewModel: AccessTokenViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(UserViewModel::class)]
    fun userViewModel(userViewModel: UserViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(HomeViewModel::class)]
    fun homeViewModel(homeViewModel: HomeViewModel): ViewModel


}