package com.adriangl.pokeapi_mvvm.moves

import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import com.adriangl.pokeapi_mvvm.utils.extensions.replace
import com.adriangl.pokeapi_mvvm.utils.injection.bindStore
import mini.Reducer
import mini.Store
import mini.Task
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

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
        if (action.task.isSuccess) {
            val newMovesMap = state.movesMap.replace(action.moveName, action.pokemonMove!!)
            val mewMovesTask = state.movesMapTask.replace(action.moveName, Task.success())

            setState(
                state.copy(
                    movesMap = newMovesMap,
                    movesMapTask = mewMovesTask
                )
            )
        } else {
            if (action.task.isFailure) {
                val mewMovesTask = state.movesMapTask.replace(action.moveName, Task.failure())

                setState(
                    state.copy(
                        movesMapTask = mewMovesTask
                    )
                )
            }
        }

    }

}

val movesStoreModule = Kodein.Module("movesStore") {
    bindStore { MovesStore(instance()) }
    bind<MovesController>() with singleton {
        MovesControllerImpl(
            instance(),
            instance()
        )
    }
}