package com.bq.arch_example.ui.pokemonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bq.arch_example.domain.moves.LoadMovesAction
import com.bq.arch_example.domain.moves.MovesState
import com.bq.arch_example.domain.moves.MovesStore
import com.bq.arch_example.domain.pokemon.FilterPokemonListAction
import com.bq.arch_example.domain.pokemon.GetPokemonDetailsListAction
import com.bq.arch_example.domain.pokemon.PokeState
import com.bq.arch_example.domain.pokemon.PokeStore
import com.bq.arch_example.network.MoveName
import com.bq.arch_example.network.Pokemon
import com.bq.arch_example.network.PokemonMove
import com.bq.arch_example.test.OpenForTesting
import com.bq.arch_example.utils.EmptyConfigurable
import com.bq.arch_example.utils.extensions.valuesList
import mini.*
import mini.rx.android.viewmodels.RxViewModel
import mini.rx.flowable
import mini.rx.mergeStates
import mini.rx.select

@OpenForTesting
class PokemonListViewModel(private val pokeStore: PokeStore,
                           private val movesStore: MovesStore,
                           private val dispatcher: Dispatcher) : RxViewModel(), EmptyConfigurable {

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