package com.adriangl.pokeapi_mvvm.domain.pokemon

import com.adriangl.pokeapi_mvvm.network.Pokemon
import mini.BaseAction
import mini.Task


class GetPokemonDetailsListAction : BaseAction()

data class PokemonDetailsListLoadedAction(val pokemonList: List<Pokemon>?, val task: Task) : BaseAction()

data class FilterPokemonListAction(val query: String) : BaseAction()
