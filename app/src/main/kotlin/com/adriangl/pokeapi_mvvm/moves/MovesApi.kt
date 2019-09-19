package com.adriangl.pokeapi_mvvm.moves

import retrofit2.http.GET

interface MovesApi {
    @GET("/api/v2/move/{id}")
    suspend fun getMove(moveId: MoveId): Move
}