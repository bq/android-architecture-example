package com.adriangl.pokeapi_mvvm.pokemonlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.adriangl.pokeapi_mvvm.moves.*
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
            getPokemonList()
        } else {
            pokeStore.state.pokemonList!!.forEach {
                getMoves(it.moves.map { it.move.id })
            }
        }
    }

    fun getPokemonListLiveData() = pokemonListLiveData

    fun getPokemonList() {
        dispatcher.dispatch(GetPokemonDetailsListAction())
    }

    fun getMoves(moves: List<MoveId>) {
        moves.forEach {
            dispatcher.dispatch(LoadMoveAction(it))
        }
    }

    fun filterPokemonList(query: String?) {
        dispatcher.dispatch(FilterPokemonListAction(query))
    }
}

data class PokemonListItem(
    val name: String,
    val number: Int,
    val sprite: String?,
    val currentMoves: List<Move>
) {
    companion object {
        fun from(pokemon: Pokemon, moves: List<Move>): PokemonListItem {
            return PokemonListItem(pokemon.name, pokemon.order, pokemon.sprites.frontDefault, moves)
        }
    }
}

data class PokemonListViewData(val pokemonListRes: Resource<List<PokemonListItem>>) {
    companion object {
        fun from(pokeState: PokeState, movesState: MovesState): PokemonListViewData {
            val allTasks = listOf(movesState.movesTask, pokeState.pokemonListTask)

            return when {
                allTasks.allSuccesful() -> {
                    val pokemonWithMovements = pokeState.filteredPokemonList!!.map { pokemon ->
                        val moveList =
                            pokemon.moves.map { movesState.movesMap.getValue(it.move.id) }
                        PokemonListItem.from(pokemon, moveList)
                    }
                    PokemonListViewData(Resource.success(pokemonWithMovements))
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