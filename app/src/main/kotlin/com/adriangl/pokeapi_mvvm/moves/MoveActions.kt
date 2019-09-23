package com.adriangl.pokeapi_mvvm.moves

data class LoadMoveAction(val moveId: MoveId)

data class MoveLoadedAction(val moveId: MoveId, val pokemonMove: PokemonMove)

data class MoveFailedAction(val moveId: MoveId)