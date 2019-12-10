package com.bq.arch_example.domain.moves

import com.bq.arch_example.network.MoveName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mini.Dispatcher
import mini.Task


interface MovesController {
    fun getMove(moveName: MoveName)
}

class MovesControllerImpl(
    private val movesApi: MovesApi,
    private val dispatcher: Dispatcher
) : MovesController {

    override fun getMove(moveName: MoveName) {
        GlobalScope.launch {
            try {
                val move = movesApi.getMove(moveName)
                dispatcher.dispatch(MoveLoadedAction(moveName, move, Task.success()))
            } catch (e: Exception) {
                dispatcher.dispatch(MoveLoadedAction(moveName, null, Task.failure()))
            }
        }
    }
}