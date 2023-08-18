# GalleryApp

Приложение галлерея с функционалом регистрации, авторизации и отправки фото. При написании проекта были использованы принципы чистой архитектуры и MVVM.

В api использованном для написания проекта используется протокол авторизации OAuth 2, для рефреша токена был использован интерфес Authenticator из OkHttp3.

Для верстки экранов был использован императивный(view) подход. Приложение написано по принципу Single Activity. Для навигации по фрагментам используется Fragment Api, 
для реализации асинхронности - библеотека RxJava3, для запросов в сеть - Retrofit. DI - Dagger2.

## Стек использованных технологий

- RxJava3
- RxBinding
- Retrofit
- OkHttp
- SwipeRefreshLayout
- Dagger2

## Скриншоты

![WelcomeFragment](screenshots/welcome_fragment.webp)
![SignInFragment](screenshots/sign_in_fragment.webp)
![SignUpFragment](screenshots/sign_up_fragment.webp)
![HomeFragment(with error while loading)](screenshots/home_fragment_no_photos.webp)
![HomeFragment](screenshots/home_fragment_with_photos.webp)
![CreatePhotoBottomSheet](screenshots/create_photo_bottom_sheet.webp)
![CreatePhotoFragment](screenshots/create_photo_fragment.webp)
![ProfileFragment](screenshots/profile_fragment.webp)
![SettingsFragment](screenshots/settings_fragment.webp)
