package com.adriangl.pokeapi_mvvm.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.util.*


val utilsModule = Kodein.Module("utils") {
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