package com.adriangl.pokeapi_mvvm.pokemonlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.adriangl.pokeapi_mvvm.R
import com.adriangl.pokeapi_mvvm.utils.injection.viewModel
import com.adriangl.pokeapi_mvvm.utils.observe
import com.mini.android.toggleViewsVisibility
import kotlinx.android.synthetic.main.pokemonlist_activity.*
import mini.onFailure
import mini.onSuccess
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class PokemonListActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by kodein()

    val pokemonListViewModel: PokemonListViewModel by viewModel()

    private val pokemonListAdapter = PokemonListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemonlist_activity)

        with(list_recycler) {
            adapter = pokemonListAdapter
            layoutManager = LinearLayoutManager(this@PokemonListActivity)
        }

        search_pokemon_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                pokemonListViewModel.filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                pokemonListViewModel.filterList(newText)
                return true
            }
        })

        pokemonListViewModel.setup()

        pokemonListViewModel.getPokemonListLiveData().observe(this) { viewData ->
            toggleViewsVisibility(viewData.pokemonListRes, list_content, list_loading, error, View.GONE)

            viewData.pokemonListRes.onSuccess { list -> pokemonListAdapter.list = list }
                .onFailure {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
        }
    }
}