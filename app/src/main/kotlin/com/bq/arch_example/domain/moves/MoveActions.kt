package com.bq.arch_example.domain.moves

import com.bq.arch_example.network.MoveName
import com.bq.arch_example.network.PokemonMove
import mini.BaseAction
import mini.Task

data class LoadMovesAction(val moveNameList: List<MoveName>) : BaseAction()

data class MoveLoadedAction(val moveName: MoveName, val pokemonMove: PokemonMove?, val task: Task) : BaseAction()
