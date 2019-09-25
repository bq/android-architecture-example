package com.adriangl.pokeapi_mvvm.moves

import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import mini.BaseAction
import mini.Task

data class LoadMovesAction(val moveNameList: List<MoveName>) : BaseAction()

data class MoveLoadedAction(val moveName: MoveName, val pokemonMove: PokemonMove?, val task: Task) : BaseAction()
