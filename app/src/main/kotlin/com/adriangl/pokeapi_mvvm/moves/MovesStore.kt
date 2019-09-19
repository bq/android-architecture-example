package com.adriangl.pokeapi_mvvm.moves

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
    val movesMap: Map<MoveId, Move> = emptyMap(),
    val movesTask: Task = Task.idle()
)

class MovesStore(private val movesController: MovesController) : Store<MovesState>() {

    @Reducer
    fun getMove(action: LoadMoveAction) {
        if (state.movesTask.isLoading) return
        setState(
            state.copy(
                movesTask = Task.loading()
            )
        )
        movesController.getMove(action.moveId)
    }

    // I'm keen on remove remove nullable data from the state. At least from the collections.
    // Reason1: It's a pain handle nullable stuff inside collections. It already has the meaning of "absence of value"
    // if you the paired tasks fails.
    //EJ 1:
    // Get move fails
    // movesTask[1] -> Failed
    // movesMap[1] -> null


    //EJ 2:
    // Get move success and the second request fails
    // movesTask[1] -> Failed
    // movesMap[1] -> "My move"

    //Therefore is not needed a Map<MoveId, Move?>.
    @Reducer
    fun onMovesLoadedAction(action: MoveLoadedAction) {
        val newMovesMap = state.movesMap.replace(action.moveId, action.move)
        val newMovesMapTask = Task.success()

        setState(
            state.copy(
                movesMap = newMovesMap,
                movesTask = newMovesMapTask
            )
        )
    }

    @Reducer
    fun onMovesFailedAction(action: MoveFailedAction) {
        val newMovesMapTask = Task.failure()
        setState(
            state.copy(
                movesTask = newMovesMapTask
            )
        )
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