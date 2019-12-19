package com.bq.arch_example.ui.pokemonlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bq.arch_example.R
import com.bq.arch_example.utils.extensions.observeResource
import com.mini.android.toggleViewsVisibility
import kotlinx.android.synthetic.main.pokemonlist_activity.*
import mini.kodein.android.viewModel
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

        pokemonListViewModel.getPokemonListLiveData().observeResource(this,
            onChanged = {
                toggleViewsVisibility(it, list_content, list_loading, error, View.GONE)
            },
            onSuccess = { viewData ->
                pokemonListAdapter.list = viewData.pokemonList
            },
            onFailure = {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        )
    }
}