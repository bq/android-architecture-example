package com.adriangl.pokeapi_mvvm.moves

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mini.Dispatcher


interface MovesController {
    fun getMove(moveId: MoveId)
}

class MovesControllerImpl(
    private val movesApi: MovesApi,
    private val dispatcher: Dispatcher
) : MovesController {

    override fun getMove(moveId: MoveId) {
        GlobalScope.launch {
            try {
                val move = movesApi.getMove(moveId)
                dispatcher.dispatch(MoveLoadedAction(moveId, move))

            } catch (e: Exception) {
                dispatcher.dispatch(MoveFailedAction(moveId))
            }
        }
    }
}