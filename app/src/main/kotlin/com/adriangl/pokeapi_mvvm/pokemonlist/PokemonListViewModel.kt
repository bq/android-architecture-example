package com.adriangl.pokeapi_mvvm.pokemonlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.adriangl.pokeapi_mvvm.moves.MovesState
import com.adriangl.pokeapi_mvvm.moves.MovesStore
import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.pokemon.PokeState
import com.adriangl.pokeapi_mvvm.pokemon.PokeStore
import com.adriangl.pokeapi_mvvm.utils.injection.bindViewModel
import com.adriangl.pokeapi_mvvm.utils.mini.mergeStates
import com.adriangl.pokeapi_mvvm.utils.mini.viewmodel.RxAndroidViewModel
import mini.*
import mini.rx.select
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PokemonListViewModel(app: Application) : RxAndroidViewModel(app), KodeinAware {
    override val kodein by closestKodein()

    private val pokeStore: PokeStore by instance()
    private val movesStore: MovesStore by instance()
    private val dispatcher: Dispatcher by instance()

    private val pokemonListLiveData = MutableLiveData<PokemonListViewData>()

    init {
        mergeStates<Task> {
            merge(pokeStore) { pokemonListTask }
            merge(movesStore) { movesTask }
        }.select {
            PokemonListViewData.from(pokeStore.state, movesStore.state)
        }.subscribe {
            pokemonListLiveData.postValue(it)
        }.track()


        if (pokeStore.state.pokemonList == null) {
            getPokemonDetails()
        }

    }

    fun getPokemonListLiveData() = pokemonListLiveData

    fun getPokemonDetails() {
        dispatcher.dispatch(GetPokemonDetailsListAction())
    }

    fun filterPokemonList(query: String?) {
        dispatcher.dispatch(FilterPokemonListAction(query))
    }
}

data class PokemonListItem(val name: String, val number: Int, val sprite: String?) {
    companion object {
        fun from(pokemon: Pokemon): PokemonListItem {
            return PokemonListItem(pokemon.name, pokemon.order, pokemon.sprites.frontDefault)
        }
    }
}

data class PokemonListViewData(val pokemonListRes: Resource<List<PokemonListItem>>) {
    companion object {
        fun from(pokeState: PokeState, movesState: MovesState): PokemonListViewData {
            val allTasks = listOf(movesState.movesTask, pokeState.pokemonListTask)

            return when {
                allTasks.allSuccesful() -> {
                    pokeState.filteredPokemonList!!.map {
                        PokemonListItem.from(
                            it
                        )
                    }
                    PokemonListViewData(Resource.success(emptyList()))
                }
                allTasks.anyFailure() -> {
                    PokemonListViewData(Resource.failure())
                }
                else -> {
                    PokemonListViewData(Resource.loading())
                }
            }
        }
    }
}

val pokemonListViewModelModule = Kodein.Module("pokemonListViewModelModule", true) {
    bindViewModel { PokemonListViewModel(instance()) }
}