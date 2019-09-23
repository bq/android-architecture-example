package com.adriangl.pokeapi_mvvm.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias MoveName = String

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

@JsonClass(generateAdapter = true)
data class PokemonMove(
    @Json(name = "accuracy")
    val accuracy: Int?,
    @Json(name = "contest_combos")
    val contestCombos: ContestCombos?,
    @Json(name = "contest_effect")
    val contestEffect: ContestEffect?,
    @Json(name = "contest_type")
    val contestType: ContestType?,
    @Json(name = "damage_class")
    val damageClass: DamageClass?,
    @Json(name = "effect_chance")
    val effectChance: Int?,
    @Json(name = "effect_changes")
    val effectChanges: List<Any>?,
    @Json(name = "effect_entries")
    val effectEntries: List<EffectEntry>,
    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    @Json(name = "generation")
    val generation: Generation,
    @Json(name = "id")
    val id: Int,
    @Json(name = "machines")
    val machines: List<Machine>?,
    @Json(name = "meta")
    val meta: Meta?,
    @Json(name = "name")
    val name: String,
    @Json(name = "names")
    val names: List<Name>,
    @Json(name = "past_values")
    val pastValues: List<Any>?,
    @Json(name = "power")
    val power: Int?,
    @Json(name = "pp")
    val pp: Int?,
    @Json(name = "priority")
    val priority: Int?,
    @Json(name = "stat_changes")
    val statChanges: List<Any>?,
    @Json(name = "super_contest_effect")
    val superContestEffect: SuperContestEffect?,
    @Json(name = "target")
    val target: Target,
    @Json(name = "type")
    val type: MoveType
)

@JsonClass(generateAdapter = true)
data class ContestEffect(
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class ContestType(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class DamageClass(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Target(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "ailment")
    val ailment: Ailment,
    @Json(name = "ailment_chance")
    val ailmentChance: Int,
    @Json(name = "category")
    val category: Category,
    @Json(name = "crit_rate")
    val critRate: Int,
    @Json(name = "drain")
    val drain: Int?,
    @Json(name = "flinch_chance")
    val flinchChance: Int?,
    @Json(name = "healing")
    val healing: Int,
    @Json(name = "max_hits")
    val maxHits: Any?,
    @Json(name = "max_turns")
    val maxTurns: Any?,
    @Json(name = "min_hits")
    val minHits: Any?,
    @Json(name = "min_turns")
    val minTurns: Any?,
    @Json(name = "stat_chance")
    val statChance: Int?
)

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Ailment(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class SuperContestEffect(
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class EffectEntry(
    @Json(name = "effect")
    val effect: String,
    @Json(name = "language")
    val language: Language,
    @Json(name = "short_effect")
    val shortEffect: String
)

@JsonClass(generateAdapter = true)
data class Language(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Name(
    @Json(name = "language")
    val language: Language,
    @Json(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
data class LanguageX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class FlavorTextEntry(
    @Json(name = "flavor_text")
    val flavorText: String,
    @Json(name = "language")
    val language: LanguageX,
    @Json(name = "version_group")
    val versionGroup: VersionGroup
)

@JsonClass(generateAdapter = true)
data class MoveType(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Machine(
    @Json(name = "machine")
    val machine: MachineX,
    @Json(name = "version_group")
    val versionGroup: VersionGroupX
)

@JsonClass(generateAdapter = true)
data class VersionGroupX(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class MachineX(
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class ContestCombos(
    @Json(name = "normal")
    val normal: Normal,
    @Json(name = "super")
    val superX: Super
)

@JsonClass(generateAdapter = true)
data class Super(
    @Json(name = "use_after")
    val useAfter: Any?,
    @Json(name = "use_before")
    val useBefore: Any?
)

@JsonClass(generateAdapter = true)
data class Normal(
    @Json(name = "use_after")
    val useAfter: List<UseAfter>?,
    @Json(name = "use_before")
    val useBefore: List<UseBefore>?
)

@JsonClass(generateAdapter = true)
data class UseBefore(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class UseAfter(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class Generation(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
)