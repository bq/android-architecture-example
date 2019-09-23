package com.adriangl.pokeapi_mvvm.pokemonlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.adriangl.pokeapi_mvvm.moves.LoadMovesAction
import com.adriangl.pokeapi_mvvm.moves.MovesState
import com.adriangl.pokeapi_mvvm.moves.MovesStore
import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import com.adriangl.pokeapi_mvvm.pokemon.PokeState
import com.adriangl.pokeapi_mvvm.pokemon.PokeStore
import com.adriangl.pokeapi_mvvm.utils.injection.bindViewModel
import com.adriangl.pokeapi_mvvm.utils.mini.mergeListStates
import com.adriangl.pokeapi_mvvm.utils.mini.viewmodel.RxAndroidViewModel
import mini.*
import mini.rx.flowable
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
        mergeListStates<Task> {
            mergeList(pokeStore, Task.idle()) { listOf(pokemonTask) }
            mergeList(movesStore, Task.idle()) { movesMapTask.values.toList() }
        }.select { taskList ->
            PokemonListViewData.from(taskList, pokeStore.state, movesStore.state)
        }.subscribe {
            pokemonListLiveData.postValue(it)
        }.track()

        pokeStore.flowable()
            .select { it.pokemonTask }
            .subscribe {
                if (it.isSuccess) {
                    val movesList = pokeStore.state.pokemonList!!
                        .flatMap { it.moves.subList(0, 4) } // Only 4 movements
                        .distinctBy { it.move.name }.map { it.move.name }
                    getMoves(movesList)
                }
            }.track()

        if (pokeStore.state.pokemonList == null) {
            getPokemonList()
        }
    }

    fun getPokemonListLiveData() = pokemonListLiveData

    fun getPokemonList() {
        dispatcher.dispatchAsync(GetPokemonDetailsListAction())
    }

    fun getMoves(moves: List<MoveName>) {
        dispatcher.dispatchAsync(LoadMovesAction(moves))
    }

    fun filterPokemonList(query: String?) {
        dispatcher.dispatchAsync(FilterPokemonListAction(query))
    }
}

data class PokemonListItem(
    val name: String,
    val number: Int,
    val sprite: String?,
    val currentMoves: List<PokemonMove>
) {
    companion object {
        fun from(pokemon: Pokemon, pokemonMove: List<PokemonMove>): PokemonListItem {
            return PokemonListItem(
                pokemon.name,
                pokemon.order,
                pokemon.sprites.frontDefault,
                pokemonMove
            )
        }
    }
}

data class PokemonListViewData(val pokemonListRes: Resource<List<PokemonListItem>>) {
    companion object {
        fun from(
            taskList: List<Task>,
            pokeState: PokeState,
            movesState: MovesState
        ): PokemonListViewData {

            //It's safer to handle the task of the subscription instead of:
            return when {
                taskList.allSuccesful() -> {
                    val pokemonWithMovements = pokeState.pokemonList!!.map { pokemon ->
                        val moveList = pokemon.moves.subList(0, 4).map {
                            movesState.movesMap.getValue(it.move.name)
                        }

                        PokemonListItem.from(pokemon, moveList)
                    }
                    PokemonListViewData(Resource.success(pokemonWithMovements))
                }
                taskList.anyFailure() -> {
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