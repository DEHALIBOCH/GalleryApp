package kz.project.gallery.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import kz.project.data.forms_validation_impl.BirthdayValidatorImpl
import kz.project.data.forms_validation_impl.ConfirmPasswordValidatorImpl
import kz.project.data.forms_validation_impl.EmailValidatorImpl
import kz.project.data.forms_validation_impl.PasswordValidatorImpl
import kz.project.data.forms_validation_impl.UsernameValidatorImpl
import kz.project.data.remote.LoginApi
import kz.project.data.remote.PhotoApi
import kz.project.data.remote.dto.error.parser.ErrorParser
import kz.project.data.repository.AccessTokenRepositoryImpl
import kz.project.data.repository.LoginRepositoryImpl
import kz.project.data.repository.PagingPhotoRepositoryImpl
import kz.project.data.repository.PhotoRepositoryImpl
import kz.project.domain.forms_validation.BirthdayValidator
import kz.project.domain.forms_validation.ConfirmPasswordValidator
import kz.project.domain.forms_validation.EmailValidator
import kz.project.domain.forms_validation.PasswordValidator
import kz.project.domain.forms_validation.UsernameValidator
import kz.project.domain.repository.AccessTokenRepository
import kz.project.domain.repository.LoginRepository
import kz.project.domain.repository.PagingPhotoRepository
import kz.project.domain.repository.PhotoRepository
import kz.project.gallery.utils.Constants

@Module
interface DataModule {

    @Binds
    fun provideConfirmPasswordValidator(confirmPasswordValidatorImpl: ConfirmPasswordValidatorImpl): ConfirmPasswordValidator

    @Binds
    fun provideUsernameValidator(usernameValidatorImpl: UsernameValidatorImpl): UsernameValidator

    @Binds
    fun providePasswordValidator(passwordValidatorImpl: PasswordValidatorImpl): PasswordValidator

    @Binds
    fun provideEmailValidator(emailValidatorImpl: EmailValidatorImpl): EmailValidator

    @Binds
    fun provideBirthdayValidator(birthdayValidatorImpl: BirthdayValidatorImpl): BirthdayValidator


    companion object {
        @Provides
        fun provideLoginRepository(loginApi: LoginApi, errorParser: ErrorParser): LoginRepository {
            return LoginRepositoryImpl(loginApi, errorParser)
        }

        @Provides
        fun provideSharedPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constants.SHARED_PREFS_ACCESS_TOKEN_TAG, Context.MODE_PRIVATE)
        }

        @Provides
        fun provideAccessTokenRepository(sharedPreferences: SharedPreferences): AccessTokenRepository {
            return AccessTokenRepositoryImpl(sharedPreferences)
        }

        @Provides
        fun providePagingPhotoRepository(photoApi: PhotoApi): PagingPhotoRepository =
            PagingPhotoRepositoryImpl(photoApi)

        @Provides
        fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository =
            PhotoRepositoryImpl(photoApi)
    }
}