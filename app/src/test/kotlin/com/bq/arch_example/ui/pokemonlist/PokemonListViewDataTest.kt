package com.bq.arch_example.ui.pokemonlist

import com.bq.arch_example.domain.moves.MovesState
import com.bq.arch_example.domain.pokemon.PokeState
import mini.Task
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonListViewDataTest {
    @Test
    fun view_data_pokestore_loading() {
        val pokeState = PokeState(pokemonListTask = Task.loading())
        val movesState = MovesState()

        val resource = PokemonListViewData.from(pokeState, movesState)

        assertTrue(resource.isLoading)
    }

    @Test
    fun view_data_movestore_loading() {
        val pokeState = PokeState()
        val movesState = MovesState(movesTaskMap = mapOf("razor-wind" to Task.loading()))

        val resource = PokemonListViewData.from(pokeState, movesState)

        assertTrue(resource.isLoading)
    }
}