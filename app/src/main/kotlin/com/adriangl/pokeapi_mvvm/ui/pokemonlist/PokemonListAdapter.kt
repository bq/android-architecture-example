package com.adriangl.pokeapi_mvvm.ui.pokemonlist

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.adriangl.pokeapi_mvvm.R
import com.mini.android.inflateNoAttach
import com.mini.android.makeGone
import kotlinx.android.synthetic.main.pokemonlist_item.view.*

class PokemonListAdapter : RecyclerView.Adapter<PokemonListViewHolder>() {

    var list: List<PokemonListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    // We use the layout ID to distinguish each type
    override fun getItemViewType(position: Int): Int {
        return R.layout.pokemonlist_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(parent.inflateNoAttach(viewType))
    }


    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        with(holder) {
            val item = list[position]

            sprite.load(item.sprite) {
                crossfade(true)
            }

            number.text = item.number.toString()
            name.text = item.name.capitalize()

            item.currentMoves.getOrNull(0)?.let {
                move1.text = "${it.name.capitalize()} " + ":" + " ${it.type.name}"
            } ?: move1.makeGone()

            item.currentMoves.getOrNull(1)?.let {
                move2.text = "${it.name.capitalize()} " + ":" + " ${it.type.name}"
            } ?: move2.makeGone()

            item.currentMoves.getOrNull(2)?.let {
                move3.text = "${it.name.capitalize()} " + ":" + " ${it.type.name}"
            } ?: move3.makeGone()

            item.currentMoves.getOrNull(3)?.let {
                move4.text = "${it.name.capitalize()} " + ":" + " ${it.type.name}"
            } ?: move4.makeGone()
        }
    }
}


class PokemonListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val sprite: ImageView = itemView.pokemon_sprite
    val number: TextView = itemView.pokemon_number
    val name: TextView = itemView.pokemon_name
    val move1: TextView = itemView.pokemon_move_1
    val move2: TextView = itemView.pokemon_move_2
    val move3: TextView = itemView.pokemon_move_3
    val move4: TextView = itemView.pokemon_move_4
}