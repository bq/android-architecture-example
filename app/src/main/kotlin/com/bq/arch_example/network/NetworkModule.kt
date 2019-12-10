package com.bq.arch_example.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object NetworkModule {
    fun create() = Kodein.Module("network") {
        val cacheSize = 300L * 1024 * 1024 // 300 MB

        bind<OkHttpClient>() with singleton {
            val context: Context = instance()

            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(Cache(context.cacheDir, cacheSize))
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
        }

        bind<Retrofit>() with singleton {
            val endpoint = "https://pokeapi.co"

            Retrofit.Builder()
                .baseUrl(HttpUrl.parse(endpoint)!!)
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<Moshi>() with singleton {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                // Add adapter to parse RFC3339 dates to Date objects
                .add(
                    Date::class.java,
                    Rfc3339DateJsonAdapter()
                )
                .build()
        }
    }
}