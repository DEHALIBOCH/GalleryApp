package kz.project.gallery.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.realm.kotlin.Realm
import kz.project.data.forms_validation_impl.BirthdayValidatorImpl
import kz.project.data.forms_validation_impl.ConfirmPasswordValidatorImpl
import kz.project.data.forms_validation_impl.EmailValidatorImpl
import kz.project.data.forms_validation_impl.PasswordValidatorImpl
import kz.project.data.forms_validation_impl.UsernameValidatorImpl
import kz.project.data.remote.LoginApi
import kz.project.data.remote.PhotoApi
import kz.project.data.remote.UserApi
import kz.project.data.remote.dto.error.parser.ErrorParser
import kz.project.data.repository.AccessTokenRepositoryImpl
import kz.project.data.repository.CompressImageRepositoryImpl
import kz.project.data.repository.LocalPhotosRepositoryImpl
import kz.project.data.repository.LoginRepositoryImpl
import kz.project.data.repository.PhotoRepositoryImpl
import kz.project.data.repository.UserRepositoryImpl
import kz.project.domain.forms_validation.BirthdayValidator
import kz.project.domain.forms_validation.ConfirmPasswordValidator
import kz.project.domain.forms_validation.EmailValidator
import kz.project.domain.forms_validation.PasswordValidator
import kz.project.domain.forms_validation.UsernameValidator
import kz.project.domain.repository.AccessTokenRepository
import kz.project.domain.repository.CompressImageRepository
import kz.project.domain.repository.LocalPhotosRepository
import kz.project.domain.repository.LoginRepository
import kz.project.domain.repository.PhotoRepository
import kz.project.domain.repository.UserRepository
import kz.project.gallery.utils.Constants

@Module
interface DataModule {

    @Binds
    fun bindConfirmPasswordValidator(confirmPasswordValidatorImpl: ConfirmPasswordValidatorImpl): ConfirmPasswordValidator

    @Binds
    fun bindUsernameValidator(usernameValidatorImpl: UsernameValidatorImpl): UsernameValidator

    @Binds
    fun bindPasswordValidator(passwordValidatorImpl: PasswordValidatorImpl): PasswordValidator

    @Binds
    fun bindEmailValidator(emailValidatorImpl: EmailValidatorImpl): EmailValidator

    @Binds
    fun bindBirthdayValidator(birthdayValidatorImpl: BirthdayValidatorImpl): BirthdayValidator

    @Binds
    fun bindCompressImageRepository(compressImageRepositoryImpl: CompressImageRepositoryImpl): CompressImageRepository


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
        fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository =
            PhotoRepositoryImpl(photoApi)

        @Provides
        fun provideUserRepository(userApi: UserApi, errorParser: ErrorParser): UserRepository =
            UserRepositoryImpl(userApi, errorParser)

        @Provides
        fun provideLocalPhotosRepository(realm: Realm, backgroundScheduler: Scheduler): LocalPhotosRepository =
            LocalPhotosRepositoryImpl(realm, backgroundScheduler)

    }
}