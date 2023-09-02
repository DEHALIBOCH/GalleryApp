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
- Glide
- Realm

## Скриншоты

<p align="center" >
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/welcome_fragment.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/sign_in_fragment.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/sign_up_fragment.webp">
   <br>
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/home_fragment_no_photos.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/home_fragment_with_photos.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/create_photo_bottom_sheet.webp">
   <br>
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/create_photo_fragment.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/profile_fragment.webp">
   <img width = "220px" height="500px" style="pointer-events: none;" src="screenshots/settings_fragment.webp">
</p> 

