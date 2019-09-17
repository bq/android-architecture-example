package com.adriangl.pokeapi_mvvm.pokemonlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.adriangl.pokeapi_mvvm.di.bindViewModel
import com.adriangl.pokeapi_mvvm.network.Pokemon
import com.adriangl.pokeapi_mvvm.pokemon.PokeState
import com.adriangl.pokeapi_mvvm.pokemon.PokeStore
import mini.*
import mini.rx.DefaultSubscriptionTracker
import mini.rx.SubscriptionTracker
import mini.rx.flowable
import mini.rx.select
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class PokemonListViewModel(app: Application) : AndroidViewModel(app),
    SubscriptionTracker by DefaultSubscriptionTracker(), KodeinAware {

    override val kodein by closestKodein()

    private val pokeStore: PokeStore by instance()
    private val dispatcher: Dispatcher by instance()

    private val pokemonListLiveData = MutableLiveData<Resource<List<PokemonListItem>>>()

    init {
        pokeStore.flowable()
            .select { it.toViewData() }
            .subscribe {
                pokemonListLiveData.postValue(it)
            }
            .track()

        if (pokeStore.state.pokemonList == null) {
            getPokemonDetails()
        }
    }

    override fun onCleared() {
        clearSubscriptions()
    }

    fun getLiveData() = pokemonListLiveData

    fun getPokemonDetails() {
        Log.e("AAA", "Dispatcher instance in ViewModel: dispatcher")
        dispatcher.dispatch(GetPokemonDetailsListAction())
    }

    fun filterList(query: String?) {
        dispatcher.dispatch(FilterPokemonListAction(query))
    }
}

data class PokemonListItem(val name: String, val number: Int, val sprite: String?) {
    companion object {
        fun from(pokemon: Pokemon): PokemonListItem {
            return PokemonListItem(pokemon.name, pokemon.order, pokemon.sprites.frontDefault)
        }
    }
}

private fun PokeState.toViewData(): Resource<List<PokemonListItem>> {
    var resource: Resource<List<PokemonListItem>> = Resource.empty()
    pokemonListTask
        .onLoading { resource = Resource.loading() }
        .onSuccess {
            resource = Resource.success(this.filteredPokemonList!!.map { PokemonListItem.from(it) })
        }
        .onFailure { resource = Resource.failure() }

    return resource
}

val pokemonListViewModelModule = Kodein.Module("pokemonListViewModelModule", true) {
    bindViewModel<PokemonListViewModel>() with provider { PokemonListViewModel(instance()) }
}