package com.bq.arch_example

import android.app.Application
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModelProvider
import com.bq.arch_example.domain.moves.MovesModule
import com.bq.arch_example.domain.pokemon.PokeModule
import com.bq.arch_example.network.NetworkModule
import com.bq.arch_example.ui.ViewModelsModule
import mini.Dispatcher
import mini.LoggerInterceptor
import mini.MiniGen
import mini.Store
import mini.kodein.android.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton
import java.io.Closeable
import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance
private val dispatcher = MiniGen.newDispatcher()

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
            addImports(
                false,
                AppModule.create(),
                NetworkModule.create(),
                PokeModule.create(),
                MovesModule.create(),
                ViewModelsModule.create()
            )

            if (testModule != null) addImport(testModule!!, true)
        }

        stores = kodein.direct.instance<Set<Store<*>>>().toList()
        dispatcher = kodein.direct.instance()

        // Initialize Mini
        storeSubscriptions = MiniGen.subscribe(dispatcher, stores)
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

object AppModule {
    fun create() = Kodein.Module("app") {
        bind<Application>() with singleton { app }
        bind<Dispatcher>() with singleton { dispatcher }
        bind() from setBinding<Store<*>>()

        bind<ViewModelProvider.Factory>() with singleton {
            KodeinViewModelFactory(
                kodein.direct
            )
        }
    }
}


private fun ConfigurableKodein.addImports(allowOverride: Boolean, vararg moduleInfo: Kodein.Module) {
    moduleInfo.forEach { addImport(it, allowOverride) }
}