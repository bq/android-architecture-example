package com.adriangl.pokeapi_mvvm.pokemon

import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.pokemonlist.FilterPokemonListAction
import com.adriangl.pokeapi_mvvm.pokemonlist.GetPokemonDetailsListAction
import com.adriangl.pokeapi_mvvm.pokemonlist.PokemonDetailsListLoadedAction
import com.adriangl.pokeapi_mvvm.pokemonlist.PokemonListItem
import mini.Reducer
import mini.Store
import mini.Task

data class PokeState(
    val pokemonList: List<Pokemon>? = null,
    val pokemonListTask: Task = Task.idle(),
    val pokemontask2:Task = Task.idle(),
    val filter: (PokemonListItem) -> Boolean = { true }
)

class PokeStore(private val pokeController: PokeController) : Store<PokeState>() {

    @Reducer
    fun getPokemonDetailsList(action: GetPokemonDetailsListAction) {
        if (state.pokemonListTask.isLoading) return
        setState(state.copy(pokemonListTask = Task.loading()))
        pokeController.getPokemonDetailsList()
    }

    @Reducer
    fun onPokemonDetailsListLoaded(action: PokemonDetailsListLoadedAction) {
        setState(
            state.copy(
                pokemonList = action.pokemonList,
                pokemonListTask = action.task
            )
        )
    }

    @Reducer
    fun filterPokemonList(action: FilterPokemonListAction) {
        setState(
            state.copy(
                filter = { pokemonListItem -> pokemonListItem.name.contains(action.query) }
            )
        )
    }
}