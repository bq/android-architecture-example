package com.bq.arch_example.ui

import com.bq.arch_example.ui.pokemonlist.PokemonListViewModel
import mini.kodein.android.bindViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

object ViewModelsModule {
    fun create() = Kodein.Module("viewModels") {
        bindViewModel { PokemonListViewModel(instance(), instance(), instance()) }
    }
}