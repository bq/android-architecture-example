package com.adriangl.pokeapi_mvvm

import android.app.Application
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModelProvider
import com.adriangl.pokeapi_mvvm.di.networkModule
import com.adriangl.pokeapi_mvvm.di.storeModule
import com.adriangl.pokeapi_mvvm.moves.movesStoreModule
import com.adriangl.pokeapi_mvvm.pokemon.pokeStoreModule
import com.adriangl.pokeapi_mvvm.pokemonlist.pokemonListViewModelModule
import com.adriangl.pokeapi_mvvm.utils.injection.KodeinViewModelFactory
import mini.Dispatcher
import mini.LoggerInterceptor
import mini.MiniGen
import mini.Store
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.Closeable
import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

class App : Application(), KodeinAware {
    override val kodein = ConfigurableKodein(mutable = true)

    private var testModule: Kodein.Module? = null

    private lateinit var dispatcher: Dispatcher
    private lateinit var stores: List<Store<*>>
    private lateinit var storeSubscriptions: Closeable

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        // Start dependency injection
        initializeInjection()
    }

    fun initializeInjection() {
        Log.e("AAA", "Reload dependencies")

        // Clear everything
        if (this::storeSubscriptions.isInitialized) {
            storeSubscriptions.close()
        }

        if (this::stores.isInitialized) {
            stores.forEach { it.close() }
        }

        kodein.clear()

        with(kodein) {
            // First, add all dependencies
            addImport(appModule, true)
            addImport(storeModule, true)
            addImport(networkModule, true)
            addImport(pokeStoreModule, true)
            addImport(movesStoreModule, true)
            addImport(pokemonListViewModelModule, true)

            if (testModule != null) addImport(testModule!!, true)
        }

        stores = kodein.direct.instance<Set<Store<*>>>().toList()
        dispatcher = kodein.direct.instance()

        // Initialize Mini
        storeSubscriptions = MiniGen.subscribe(dispatcher, stores.toList())
        stores.forEach { store ->
            store.initialize()
        }

        dispatcher.addInterceptor(LoggerInterceptor(stores, { tag, msg ->
            Log.d(tag, msg)
        }))
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setTestModule(init: Kodein.Builder.() -> Unit) {
        testModule = Kodein.Module(name = "test", allowSilentOverride = true, init = init)
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun clearTestModule() {
        testModule = null
    }
}

val appModule = Kodein.Module("app", true) {
    bind<Application>() with singleton { app }
    bind<Dispatcher>() with singleton { MiniGen.newDispatcher() }

    bind<ViewModelProvider.Factory>() with singleton {
        KodeinViewModelFactory(
            kodein.direct
        )
    }
}