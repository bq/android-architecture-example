package com.adriangl.pokeapi_mvvm.moves

import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import mini.Task

data class LoadMovesAction(val moveNameList: List<MoveName>)

data class MoveLoadedAction(val moveName: MoveName, val pokemonMove: PokemonMove?, val task: Task)
