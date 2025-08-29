package com.battlebucks.battlebucksgithubapp.data.network

import com.battlebucks.battlebucksgithubapp.BuildConfig
import com.battlebucks.battlebucksgithubapp.utils.getBaseUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ApiManager {
    private val BASE_URL = getBaseUrl()

    @Provides
    @Singleton
    fun provideHeadersInterceptor(): Interceptor = Interceptor { chain ->
        val req = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github+json")
            .addHeader("X-GitHub-Api-Version", "2022-11-28")
            .build()
        chain.proceed(req)
    }

    @Provides
    @Singleton
    fun provideOkHttp(headers: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(headers)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // <-- important for Kotlin data classes
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Provides @Singleton
    fun provideGithubApi(retrofit: Retrofit): ApiClient =
        retrofit.create(ApiClient::class.java)
}