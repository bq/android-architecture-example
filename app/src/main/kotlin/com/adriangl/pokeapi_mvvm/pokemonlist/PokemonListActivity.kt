package com.adriangl.pokeapi_mvvm.pokemonlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.adriangl.pokeapi_mvvm.R
import com.adriangl.pokeapi_mvvm.utils.injection.viewModel
import com.mini.android.toggleViewsVisibility
import kotlinx.android.synthetic.main.pokemonlist_activity.*
import mini.onSuccess
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class PokemonListActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val pokemonListViewModel: PokemonListViewModel by viewModel()
    private val pokemonListLiveData: LiveData<PokemonListViewData>
        get() = pokemonListViewModel.getPokemonListLiveData()

    private val pokemonListAdapter = PokemonListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemonlist_activity)

        with(list_recycler) {
            adapter = pokemonListAdapter
            layoutManager = LinearLayoutManager(this@PokemonListActivity)
        }
        list_recycler.adapter = pokemonListAdapter

        search_pokemon_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pokemonListViewModel.filterPokemonList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pokemonListViewModel.filterPokemonList(newText)
                return true
            }

        })

        pokemonListLiveData.observe(this, Observer { (resource) ->
            toggleViewsVisibility(resource, list_content, list_loading, error, View.GONE)

            resource
                .onSuccess {
                    pokemonListAdapter.list = it
                }
        })
    }
}
