package com.comunidadedevspace.taskbeats.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    fun createNewsService(): NewsService {

        val logging = HttpLoggingInterceptor()
        logging.apply {
            HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit
            .Builder()
            .client(client)
            .baseUrl("https://api.thenewsapi.com/v1/news/headlines?locale=us&language=en&api_token=AJZ9opxg0WAGpanIXiLQRgC1aNtegK9monZaetKI")
            .addConverterFactory(GsonConverterFactory.create(Gson()))

        return retrofit
            .build()
            .create(NewsService::class.java)
    }
}