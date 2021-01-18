/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.pokedex.ui.adapter

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.skydoves.pokedex.R
import com.skydoves.pokedex.databinding.ItemPokemonBinding
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.ui.details.DetailActivity

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(), Filterable {

  private var items: MutableList<Pokemon> = mutableListOf()
  private var onClickedAt = 0L
  lateinit var pokemonFilterList: List<Pokemon>

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding =
      DataBindingUtil.inflate<ItemPokemonBinding>(inflater, R.layout.item_pokemon, parent, false)
    return PokemonViewHolder(binding).apply {
      binding.root.setOnClickListener {
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
          ?: return@setOnClickListener
        val currentClickedAt = SystemClock.elapsedRealtime()
        if (currentClickedAt - onClickedAt > binding.transformationLayout.duration) {
          DetailActivity.startActivity(binding.transformationLayout, items[position])
          onClickedAt = currentClickedAt
        }
      }
    }
  }

  fun setPokemonList(pokemonList: List<Pokemon>) {
    pokemonFilterList = pokemonList
    resetData(pokemonList)
  }

  fun resetData(pokemonList: List<Pokemon>) {
    val previousItemSize = items.size
    items.clear()
    items.addAll(pokemonList)
    notifyItemRangeChanged(previousItemSize, pokemonList.size)
  }

  override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
    holder.binding.apply {
      pokemon = items[position]
      executePendingBindings()
    }
  }

  override fun getItemCount() = items.size

  class PokemonViewHolder(val binding: ItemPokemonBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun getFilter(): Filter {
    return valueFilter
  }

  private val valueFilter = object : Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults? {
      var filterResults = FilterResults()

      if (!constraint.isNullOrEmpty()) {
        var filterList = mutableListOf<Pokemon>()
        for (pokemon: Pokemon in pokemonFilterList) {
          if (pokemon.name.toUpperCase().contains(constraint.toString().toUpperCase())) {
            filterList.add(pokemon)
          }
        }

        filterResults.count = filterList.size
        filterResults.values = filterList

      } else {
        filterResults.count = pokemonFilterList.size
        filterResults.values = pokemonFilterList
      }

      return filterResults
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
      items.clear()
      items.addAll(results.values as MutableList<Pokemon>)
      notifyDataSetChanged()
    }
  }
}

