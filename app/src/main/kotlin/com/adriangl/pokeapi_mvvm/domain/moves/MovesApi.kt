package com.adriangl.pokeapi_mvvm.domain.moves

import com.adriangl.pokeapi_mvvm.network.MoveName
import com.adriangl.pokeapi_mvvm.network.PokemonMove
import retrofit2.http.GET
import retrofit2.http.Path

interface MovesApi {
    @GET("/api/v2/move/{name}")
    suspend fun getMove(@Path("name") moveName: MoveName): PokemonMove
}