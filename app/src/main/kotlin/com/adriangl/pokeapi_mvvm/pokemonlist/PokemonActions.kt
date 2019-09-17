package com.adriangl.pokeapi_mvvm.pokemonlist

import com.adriangl.pokeapi_mvvm.network.Pokemon
import mini.BaseAction
import mini.Task

class GetPokemonDetailsListAction : BaseAction()

data class PokemonDetailsListLoadedAction(val pokemonList: List<Pokemon>?, val task: Task)

data class FilterPokemonListAction(val query: String?) : BaseAction()

data class PokemonListFilteredAction(val query: String?, val list: List<Pokemon>?, val task: Task)

