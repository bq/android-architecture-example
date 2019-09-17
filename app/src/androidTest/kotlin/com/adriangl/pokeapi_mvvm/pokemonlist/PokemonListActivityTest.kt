package com.adriangl.pokeapi_mvvm.pokemonlist

import android.content.Intent
import android.view.View
import com.adriangl.pokeapi_mvvm.R
import com.adriangl.pokeapi_mvvm.app
import com.adriangl.pokeapi_mvvm.utils.injection.bindViewModel
import com.adriangl.pokeapi_mvvm.utils.test.testActivity
import com.adriangl.pokeapi_mvvm.utils.test.testDispatcher
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.agoda.kakao.text.KTextView
import mini.Dispatcher
import mini.Resource
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class PokemonListScreen : Screen<PokemonListScreen>() {
    val loadingView = KView { withId(R.id.list_loading) }
    val errorView = KView { withId(R.id.error) }
    val searchBar = KView { withId(R.id.search_pokemon_bar) }
    val pokemonList = KRecyclerView(
        builder = { withId(R.id.list_recycler) },
        itemTypeBuilder = {
            itemType(::PokemonListRecyclerItem)
        }
    )

    class PokemonListRecyclerItem(parent: Matcher<View>) :
        KRecyclerItem<PokemonListRecyclerItem>(parent) {
        val sprite: KImageView = KImageView(parent) { withId(R.id.pokemon_sprite) }
        val name: KTextView = KTextView(parent) { withId(R.id.pokemon_name) }
        val number: KTextView = KTextView(parent) { withId(R.id.pokemon_number) }
    }
}

class PokemonListActivityTest {
    @get:Rule
    val testActivity = testActivity(clazz = PokemonListActivity::class, launchActivity = false)

    @Before
    fun setup() {
        app.clearTestModule()
    }

    @Test
    fun validate_list_size() {
        reinitInjection {
            bindViewModel<PokemonListViewModel>() with singleton {
                PokemonListViewModel(instance()).apply {
                    getLiveData().postValue(
                        Resource.success(
                            listOf(
                                PokemonListItem(
                                    "Perry",
                                    1,
                                    "https://placekitten.com/256/256"
                                )
                            )
                        )
                    )
                }
            }
        }

        testActivity.launchActivity(Intent())

        onScreen<PokemonListScreen> {
            loadingView.isNotDisplayed()
            errorView.isNotDisplayed()
            searchBar.isDisplayed()
            pokemonList {
                isDisplayed()

                childAt<PokemonListScreen.PokemonListRecyclerItem>(0) {
                    isDisplayed()
                    name.hasText("Perry")
                    number.hasText("1")
                }
            }

        }
    }

    @Test
    fun validate_list_size_2() {
        reinitInjection {
            bindViewModel<PokemonListViewModel>() with provider {
                PokemonListViewModel(instance()).apply {
                    // We have to use postValue instead of setValue because we can't set a value when there's
                    // no available observers
                    getLiveData().postValue(
                        Resource.success(
                            listOf(
                                PokemonListItem(
                                    "Lola",
                                    5,
                                    "https://placekitten.com/256/256"
                                ),
                                PokemonListItem(
                                    "Perra",
                                    2,
                                    "https://placekitten.com/256/256"
                                )
                            )
                        )
                    )
                }
            }
        }

        testActivity.launchActivity(Intent())

        onScreen<PokemonListScreen> {
            loadingView.isNotDisplayed()
            errorView.isNotDisplayed()
            searchBar.isDisplayed()
            pokemonList {
                isDisplayed()

                childAt<PokemonListScreen.PokemonListRecyclerItem>(0) {
                    isDisplayed()
                    name.hasText("Lola")
                    number.hasText("5")
                }

                childAt<PokemonListScreen.PokemonListRecyclerItem>(1) {
                    isDisplayed()
                    name.hasText("Perra")
                    number.hasText("2")
                }
            }

        }
    }

    private inline fun reinitInjection(crossinline init: Kodein.Builder.() -> Unit) {
        app.setTestModule {
            bind<Dispatcher>() with singleton { testDispatcher }
            init()
        }

        app.initializeInjection()
    }
}