package com.adriangl.pokeapi_mvvm.moves

import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import com.adriangl.pokeapi_mvvm.utils.extensions.replace
import com.adriangl.pokeapi_mvvm.utils.extensions.replaceIfNotNull
import mini.Reducer
import mini.Store
import mini.Task

data class MovesState(
    val movesMap: Map<MoveName, PokemonMove> = emptyMap(),
    val movesMapTask: Map<MoveName, Task> = emptyMap()
)

class MovesStore(private val movesController: MovesController) : Store<MovesState>() {

    @Reducer
    fun getMove(action: LoadMovesAction) {
        var newMovesMapTask = state.movesMapTask
        action.moveNameList.forEach { moveName ->
            newMovesMapTask = newMovesMapTask.replace(moveName, Task.loading())
        }
        setState(
            state.copy(
                movesMapTask = newMovesMapTask
            )
        )
        action.moveNameList.forEach { moveName ->
            movesController.getMove(moveName)
        }
    }

    @Reducer
    fun onMovesLoadedAction(action: MoveLoadedAction) {
        setState(
            state.copy(
                movesMap = state.movesMap.replaceIfNotNull(action.moveName, action.pokemonMove),
                movesMapTask = state.movesMapTask.replace(action.moveName, action.task)
            )
        )
    }
}