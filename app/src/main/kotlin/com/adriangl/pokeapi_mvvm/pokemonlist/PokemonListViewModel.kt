package com.adriangl.pokeapi_mvvm.pokemonlist

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adriangl.pokeapi_mvvm.moves.LoadMovesAction
import com.adriangl.pokeapi_mvvm.moves.MovesState
import com.adriangl.pokeapi_mvvm.moves.MovesStore
import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import com.adriangl.pokeapi_mvvm.pokemon.PokeState
import com.adriangl.pokeapi_mvvm.pokemon.PokeStore
import com.adriangl.pokeapi_mvvm.utils.EmptyConfigurable
import com.adriangl.pokeapi_mvvm.utils.extensions.valuesList
import mini.*
import mini.kodein.android.bindViewModel
import mini.rx.android.viewmodels.RxAndroidViewModel
import mini.rx.flowable
import mini.rx.mergeStates
import mini.rx.select
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PokemonListViewModel(app: Application) : RxAndroidViewModel(app), KodeinAware, EmptyConfigurable {
    override val kodein by closestKodein()

    private val pokeStore: PokeStore by instance()
    private val movesStore: MovesStore by instance()
    private val dispatcher: Dispatcher by instance()

    private val pokemonListLiveData = MutableLiveData<PokemonListViewData>()

    override fun setup() {
        mergeStates<Any> {
            merge(pokeStore) { this } // change merge to inline fun <S : Store<U>, U : Any> merge(store: S, crossinline mapper: (U.() -> R) = { this as R }) {
            merge(movesStore) { this }
        }.select {
            PokemonListViewData.from(pokeStore.state, movesStore.state)
        }.subscribe {
            pokemonListLiveData.postValue(it)
        }.track()

        pokeStore.flowable()
            .select { it.pokemonListTask }
            .subscribe {
                if (it.isSuccess) {
                    val movesList = pokeStore.state.pokemonList!!
                        .flatMap { pokemon ->
                            pokemon.moves.subList(0, 4.coerceAtMost(pokemon.moves.size))
                        } // At most 4 movements
                        .map { it.move.name }.distinct()
                    getMoves(movesList)
                }
            }.track()

        if (pokeStore.state.pokemonList == null) {
            getPokemonDetails()
        }
    }

    fun getPokemonListLiveData(): LiveData<PokemonListViewData> = pokemonListLiveData


    @VisibleForTesting
    fun getPokemonMutableLiveData() = pokemonListLiveData

    fun getPokemonDetails() {
        if (pokeStore.state.pokemonList == null) {
            dispatcher.dispatch(GetPokemonDetailsListAction())
        }
    }

    fun getMoves(moves: List<MoveName>) {
        dispatcher.dispatchAsync(LoadMovesAction(moves))
    }

    fun filterList(query: String) {
        dispatcher.dispatch(FilterPokemonListAction(query))
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
        fun from(pokeState: PokeState, movesState: MovesState): PokemonListViewData {
            val pokemonListTasks = listOf(pokeState.pokemonListTask)
            val movesListTasks = movesState.movesTaskMap.valuesList().let { if (it.isEmpty()) listOf(Task.idle()) else it }
            val taskList = pokemonListTasks + movesListTasks

            return when {
                taskList.allSuccesful() -> {
                    val pokemonWithMovements = pokeState.pokemonList!!
                        .map { pokemon ->
                            val moveList =
                                pokemon.moves.subList(0, 4.coerceAtMost(pokemon.moves.size)).map {
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

val pokemonListViewModelModule = Kodein.Module("pokemonListViewModelModule") {
    bindViewModel { PokemonListViewModel(instance()) }
}