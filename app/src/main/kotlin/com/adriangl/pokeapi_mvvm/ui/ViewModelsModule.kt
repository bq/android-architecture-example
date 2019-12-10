package com.adriangl.pokeapi_mvvm.ui

import com.adriangl.pokeapi_mvvm.ui.pokemonlist.PokemonListViewModel
import mini.kodein.android.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

object ViewModelsModule {
    fun create() = Kodein.Module("viewModels") {
        bindViewModel { PokemonListViewModel(instance()) }
    }
}