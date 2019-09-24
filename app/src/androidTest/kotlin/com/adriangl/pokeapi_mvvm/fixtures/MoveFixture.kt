package com.adriangl.pokeapi_mvvm.fixtures

import com.adriangl.pokeapi_mvvm.network.Generation
import com.adriangl.pokeapi_mvvm.network.MoveType
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import com.adriangl.pokeapi_mvvm.network.Target


interface MoveFixtures {

    private val baseMove
        get() = PokemonMove(
            id = 1,
            accuracy = 1,
            contestCombos = null,
            contestEffect = null,
            contestType = null,
            damageClass = null,
            effectChance = 1,
            effectChanges = null,
            effectEntries = emptyList(),
            flavorTextEntries = emptyList(),
            generation = Generation("any", "http:://example.com"),
            machines = null,
            meta = null,
            name = "Base",
            names = emptyList(),
            pastValues = emptyList(),
            power = 10,
            pp = 23,
            priority = 23,
            statChanges = null,
            superContestEffect = null,
            target = Target(
                "any",
                "http:://example.com"
            ),
            type = MoveType("Normal", "http:://example.com")
        )

    fun anyMoveList() =
        listOf(
            baseMove.copy(name = "Razor-wind", type = baseMove.type.copy(name = "Normal")),
            baseMove.copy(
                name = "Swords-dance",
                type = baseMove.type.copy(name = "Normal")
            ),
            baseMove.copy(name = "Cut", type = baseMove.type.copy(name = "Normal")),
            baseMove.copy(
                name = "Bind", type = baseMove.type.copy(name = "Normal")
            )
        )

}