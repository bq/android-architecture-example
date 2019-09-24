package com.adriangl.pokeapi_mvvm.di

import com.adriangl.pokeapi_mvvm.moves.MovesController
import com.adriangl.pokeapi_mvvm.moves.MovesControllerImpl
import com.adriangl.pokeapi_mvvm.moves.MovesStore
import com.adriangl.pokeapi_mvvm.pokemon.PokeController
import com.adriangl.pokeapi_mvvm.pokemon.PokeControllerImpl
import com.adriangl.pokeapi_mvvm.pokemon.PokeStore
import com.adriangl.pokeapi_mvvm.utils.injection.bindStore
import mini.Store
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.setBinding
import org.kodein.di.generic.singleton

val storeModule = Kodein.Module("store") {
    bind() from setBinding<Store<*>>()
}

val movesStoreModule = Kodein.Module("movesStore") {
    bindStore { MovesStore(instance()) }
    bind<MovesController>() with singleton {
        MovesControllerImpl(
            instance(),
            instance()
        )
    }
}


val pokeStoreModule = Kodein.Module("pokeStore") {
    bindStore { PokeStore(instance()) }
    bind<PokeController>() with singleton {
        PokeControllerImpl(
            instance(),
            instance()
        )
    }
}