# Android architecture example
Sample illustrating the use of modern Android architectures and components.

## Before you dive in...
In order to understand the example fully, we recommend reading the following documentation first:
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) documentation
* [Flux architecture documentation](https://facebook.github.io/flux/)
* [Mini repo documentation](https://github.com/bq/mini-kotlin)

## What this example does?
The app in this example is a barebones Pokédex app that uses the API provided by https://pokeapi.co/ to display a subset of Pokémon 
and their initial moves.

The app flow is driven via Flux actions to load Pokémon data and displayed to the user via `ViewModel`s and `LiveData`.

## How the app is structured?
The app is divided in several packages: 
* **domain**: contains the classes related to business logic: stores, controllers, application models...
* **network**: contains the classes related to network communication: API contracts, network models...
* **ui**: contains the classes related to UI: `ViewModel`s, `LiveData`s, fragments, activities, views...
 
In each of these packages you can find sub-packages by functionality. These functionalities are:
* **pokemon**: classes related to Pokémon data retrieving, such as Pokédex number, images and so on
* **moves**: classes related to Pokémon moves and their stats and properties

## How is Flux used in the app?
The basic flow of the app is as follows:
* When the `PokemonListActivity` is loaded we setup the related `ViewModel`, that way the `ViewModel` will dispatch an action to
  load Pokémon data.
* As soon as the Pokémon data is loaded the `ViewModel` will trigger an action to load a subset of each Pokémon moves, 
one action per Pokémon.
* The `ViewModel` listens to changes in both `PokemonStore` and `MovesStore` so it can compose the final data passed to 
the view (referred as `ViewData`, which has a method to transform store states into view-consumable info) and emits it 
via a `LiveData` stored in the `ViewModel` so the activity can render both Pokémon and related moves.

## How is everything tested?
We decided to split the tests in two types:
#### UI tests
We can test if the UI works as expected by writing an UI test using `Espresso via the Kakao` library. The architecture allows us
to create a mock of the `ViewModel` using `Mockito` and then replace the `LiveData` that it contains for our own test `LiveData`. 

This makes creating UI tests a breeze: just emit the values you need using the test `LiveData` and do your screen assertions. 

#### Unit tests
We can unit test the mapping functions of each `ViewData` so we can ensure that the proper information is properly generated for
view consumption.

## References
* Kodein ViewModel injection: https://proandroiddev.com/android-viewmodel-dependency-injection-with-kodein-249f80f083c9
* Kodein testing sample: https://github.com/Karumi/kodein-sample-testing
* Basic Flux implementation: https://https://github.com/pabloogc/FakeShop/
* Mock Kotlin classes in tests: https://proandroiddev.com/mocking-androidtest-in-kotlin-51f0a603d500

## License
```
   Copyright 2019 BQ

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```


