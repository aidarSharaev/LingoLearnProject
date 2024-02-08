//package ru.aidar.lingolearn.di
//
//import android.widget.Button
//import com.google.gson.Gson
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import ru.aidar.lingolearn.api.DeepLService
//import ru.aidar.lingolearn.base.BaseApiService
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkDI {
//
//    private const val DEEPL_API_BASE_URL = "https://randomuser.me/"
//    // base url_2
//    // base url_3
//    // base url_4
//    // base url_5
//
//    @Provides
//    @Singleton
//    fun providesHTTPInterceptor(): Interceptor {
//        return HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//    }
//
//    @Provides
//    @Singleton
//    fun providesOkHttpClient(
//        httpLoggingInterceptor: HttpLoggingInterceptor
//    ): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun providesRetrofit(
//        okHttpClient: OkHttpClient
//    ): Retrofit {
//        val json = Gson()
//        return Retrofit.Builder()
//            .baseUrl(DEEPL_API_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(json))
//            .client(okHttpClient)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideDeeplService(
//        retrofit: Retrofit
//    ): BaseApiService {
//        return retrofit.create(DeepLService::class.java)
//    }
//}