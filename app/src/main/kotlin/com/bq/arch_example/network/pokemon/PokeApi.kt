package com.bq.arch_example.network.pokemon

import com.bq.arch_example.network.common.Pokemon
import com.bq.arch_example.network.common.ResourceList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("/api/v2/pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int) : ResourceList

    @GET("/api/v2/pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int) : Pokemon

    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String) : Pokemon
}