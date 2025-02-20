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

package com.skydoves.pokedex.ui.main

import android.graphics.Color
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.skydoves.pokedex.R
import com.skydoves.pokedex.base.DataBindingActivity
import com.skydoves.pokedex.databinding.ActivityMainBinding
import com.skydoves.pokedex.ui.adapter.PokemonAdapter
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivity(), SearchView.OnQueryTextListener {

  @VisibleForTesting val viewModel: MainViewModel by viewModels()
  private val binding: ActivityMainBinding by binding(R.layout.activity_main)
  private val adapter: PokemonAdapter by lazy {
    PokemonAdapter()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationStartContainer()
    super.onCreate(savedInstanceState)
    binding.apply {
      lifecycleOwner = this@MainActivity
      adapter = this@MainActivity.adapter
      vm = viewModel
    }

    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    super.onCreateOptionsMenu(menu)
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.toolbar_menu, menu)

    val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
    val searchView: SearchView? = searchItem?.actionView as SearchView?
    searchView?.setOnQueryTextListener(this)
    return true
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    return false
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    this.adapter.filter.filter(newText)
    return false
  }

}
