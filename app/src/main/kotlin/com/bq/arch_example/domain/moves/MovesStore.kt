package com.bq.arch_example.domain.moves

import com.bq.arch_example.network.common.MoveName
import com.bq.arch_example.network.common.PokemonMove
import com.bq.arch_example.network.moves.MovesApi
import com.bq.arch_example.utils.extensions.replace
import com.bq.arch_example.utils.extensions.replaceIfNotNull
import mini.Reducer
import mini.Store
import mini.Task
import mini.kodein.bindStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

data class MovesState(
    val movesMap: Map<MoveName, PokemonMove> = emptyMap(),
    val movesTaskMap: Map<MoveName, Task> = emptyMap()
)

class MovesStore(private val movesController: MovesController) : Store<MovesState>() {

    @Reducer
    fun getMove(action: LoadMovesAction) {
        var newMovesTaskMap = state.movesTaskMap
        action.moveNameList.forEach { moveName ->
            newMovesTaskMap = newMovesTaskMap.replace(moveName, Task.loading())
        }
        setState(
            state.copy(
                movesTaskMap = newMovesTaskMap
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
                movesTaskMap = state.movesTaskMap.replace(action.moveName, action.task)
            )
        )
    }
}

object MovesModule {
    fun create() = Kodein.Module("movesModule") {
        bindStore { MovesStore(instance()) }
        bind<MovesController>() with singleton {
            MovesControllerImpl(
                instance(),
                instance()
            )
        }
        bind<MovesApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create(MovesApi::class.java)
        }
    }
}
