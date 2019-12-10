package com.bq.arch_example.domain.pokemon

import com.bq.arch_example.network.PokeApi
import com.bq.arch_example.network.Pokemon
import com.bq.arch_example.ui.pokemonlist.PokemonListItem
import mini.Reducer
import mini.Store
import mini.Task
import mini.kodein.bindStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

data class PokeState(
    val pokemonList: List<Pokemon>? = null,
    val pokemonListTask: Task = Task.idle(),
    val filter: (PokemonListItem) -> Boolean = { true }
)

class PokeStore(private val pokeController: PokeController) : Store<PokeState>() {

    @Reducer
    fun getPokemonDetailsList(action: GetPokemonDetailsListAction) {
        if (state.pokemonListTask.isLoading) return
        setState(state.copy(pokemonListTask = Task.loading()))
        pokeController.getPokemonDetailsList()
    }

    @Reducer
    fun onPokemonDetailsListLoaded(action: PokemonDetailsListLoadedAction) {
        setState(
            state.copy(
                pokemonList = action.pokemonList,
                pokemonListTask = action.task
            )
        )
    }

    @Reducer
    fun filterPokemonList(action: FilterPokemonListAction) {
        setState(
            state.copy(
                filter = { pokemonListItem -> pokemonListItem.name.contains(action.query) }
            )
        )
    }
}

object PokeModule {
    fun create() = Kodein.Module("pokeStore") {
        bindStore { PokeStore(instance()) }
        bind<PokeController>() with singleton {
            PokeControllerImpl(
                instance(),
                instance()
            )
        }
        bind<PokeApi>() with singleton {
            val retrofit: Retrofit = instance()
            retrofit.create(PokeApi::class.java)
        }
    }
}