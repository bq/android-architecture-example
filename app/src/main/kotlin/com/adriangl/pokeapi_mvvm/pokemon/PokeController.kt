package com.adriangl.pokeapi_mvvm.pokemon

import com.adriangl.pokeapi_mvvm.network.PokeApi
import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.pokemonlist.PokemonDetailsListLoadedAction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.Task

interface PokeController {
    fun getPokemonDetailsList()
    fun filterPokemonList(list: List<Pokemon>?, query: String?): List<Pokemon>?
}

class PokeControllerImpl(
    private val pokeApi: PokeApi,
    private val dispatcher: Dispatcher
) : PokeController {
    /*override fun filterPokemonList(list: List<Pokemon>?, query: String?) {
        GlobalScope.launch {
            try {
                val filteredList = if (query.isNullOrBlank()) list else list?.filter {
                    it.name.contains(
                        query,
                        true
                    )
                }
                dispatcher.dispatch(PokemonListFilteredAction(query, filteredList, Task.success()))
            } catch (e: Exception) {
                dispatcher.dispatch(PokemonListFilteredAction(query, list, Task.failure(e)))
            }
        }
    }*/

    override fun filterPokemonList(list: List<Pokemon>?, query: String?): List<Pokemon>? {
        return if (query.isNullOrBlank()) list else list?.filter { it.name.contains(query, true) }
    }

    override fun getPokemonDetailsList() {
        GlobalScope.launch {
            try {
                val pokemonResourceList = pokeApi.getPokemonList(100, 0)
                val pokemonList = pokemonResourceList.results.map { result ->
                    async {
                        val pokeName = result.name
                        return@async pokeApi.getPokemonByName(pokeName)
                    }
                }
                    .awaitAll().sortedWith(compareBy({ it.order }, { it.id }))

                dispatcher.dispatch(
                    PokemonDetailsListLoadedAction(
                        pokemonList,
                        Task.success()
                    )
                )
            } catch (e: Exception) {
                dispatcher.dispatch(PokemonDetailsListLoadedAction(null, Task.failure(e)))
            }
        }
    }

}