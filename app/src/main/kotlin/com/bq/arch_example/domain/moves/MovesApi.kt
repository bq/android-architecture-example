package com.bq.arch_example.domain.moves

import com.bq.arch_example.network.MoveName
import com.bq.arch_example.network.PokemonMove
import retrofit2.http.GET
import retrofit2.http.Path

interface MovesApi {
    @GET("/api/v2/move/{name}")
    suspend fun getMove(@Path("name") moveName: MoveName): PokemonMove
}