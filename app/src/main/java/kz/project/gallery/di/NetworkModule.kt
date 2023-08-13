package kz.project.gallery.di

import kz.project.data.core.AuthTokenInterceptor
import kz.project.data.core.GalleryApiAuthenticator
import kz.project.data.remote.LoginApi
import kz.project.gallery.utils.Constants
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import kz.project.data.remote.PhotoApi
import kz.project.data.remote.UserApi
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {

    @Binds
    fun bindAuthenticator(galleryApiAuthenticator: GalleryApiAuthenticator): Authenticator

    companion object {

        @Provides
        fun provideHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authenticator: Authenticator,
            authTokenInterceptor: AuthTokenInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .authenticator(authenticator)
                .addInterceptor(authTokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

        }

        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideLoginApi(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }

        @Provides
        fun providePhotoApi(retrofit: Retrofit): PhotoApi {
            return retrofit.create(PhotoApi::class.java)
        }

        @Provides
        fun provideUserApi(retrofit: Retrofit) : UserApi {
            return retrofit.create(UserApi::class.java)
        }

        @Provides
        fun provideGson(): Gson = Gson()
    }

}