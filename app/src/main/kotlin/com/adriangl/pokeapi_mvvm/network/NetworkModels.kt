package com.adriangl.pokeapi_mvvm.network

import com.squareup.moshi.Json

data class ResourceList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>
)

data class Result(
    val name: String,
    val url: String
)

data class Ability(
    @Json(name = "ability")
    val ability: AbilityX,
    @Json(name = "is_hidden")
    val isHidden: Boolean,
    @Json(name = "slot")
    val slot: Int
)

data class AbilityX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class Form(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class GameIndex(
    @Json(name = "game_index")
    val gameIndex: Int,
    @Json(name = "version")
    val version: Version
)

data class Move(
    @Json(name = "move")
    val move: MoveX,
    @Json(name = "version_group_details")
    val versionGroupDetails: List<VersionGroupDetail>
)

data class MoveLearnMethod(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class MoveX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class Pokemon(
    @Json(name = "abilities")
    val abilities: List<Ability>,
    @Json(name = "base_experience")
    val baseExperience: Int,
    @Json(name = "forms")
    val forms: List<Form>,
    @Json(name = "game_indices")
    val gameIndices: List<GameIndex>,
    @Json(name = "height")
    val height: Int,
    @Json(name = "held_items")
    val heldItems: List<Any>,
    @Json(name = "id")
    val id: Int,
    @Json(name = "is_default")
    val isDefault: Boolean,
    @Json(name = "location_area_encounters")
    val locationAreaEncounters: String,
    @Json(name = "moves")
    val moves: List<Move>,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "species")
    val species: Species,
    @Json(name = "sprites")
    val sprites: Sprites,
    @Json(name = "stats")
    val stats: List<Stat>,
    @Json(name = "types")
    val types: List<Type>,
    @Json(name = "weight")
    val weight: Int
) {
    override fun toString(): String {
        return "Pokemon(id=$id, name='$name')"
    }
}

data class Species(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class Sprites(
    @Json(name = "back_default")
    val backDefault: String?,
    @Json(name = "back_female")
    val backFemale: String?,
    @Json(name = "back_shiny")
    val backShiny: String?,
    @Json(name = "back_shiny_female")
    val backShinyFemale: String?,
    @Json(name = "front_default")
    val frontDefault: String?,
    @Json(name = "front_female")
    val frontFemale: String?,
    @Json(name = "front_shiny")
    val frontShiny: String?,
    @Json(name = "front_shiny_female")
    val frontShinyFemale: String?
)

data class Stat(
    @Json(name = "base_stat")
    val baseStat: Int,
    @Json(name = "effort")
    val effort: Int,
    @Json(name = "stat")
    val stat: StatX
)

data class StatX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class Type(
    @Json(name = "slot")
    val slot: Int,
    @Json(name = "type")
    val type: TypeX
)

data class TypeX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class Version(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class VersionGroup(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

data class VersionGroupDetail(
    @Json(name = "level_learned_at")
    val levelLearnedAt: Int,
    @Json(name = "move_learn_method")
    val moveLearnMethod: MoveLearnMethod,
    @Json(name = "version_group")
    val versionGroup: VersionGroup
)