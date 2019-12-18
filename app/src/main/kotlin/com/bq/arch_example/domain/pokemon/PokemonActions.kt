package com.bq.arch_example.domain.pokemon

import com.bq.arch_example.network.common.Pokemon
import mini.BaseAction
import mini.Task


class GetPokemonDetailsListAction : BaseAction()

data class PokemonDetailsListLoadedAction(val pokemonList: List<Pokemon>?, val task: Task) : BaseAction()

data class FilterPokemonListAction(val query: String) : BaseAction()
