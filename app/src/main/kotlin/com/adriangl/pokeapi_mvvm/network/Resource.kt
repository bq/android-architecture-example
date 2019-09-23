package com.adriangl.pokeapi_mvvm.network


import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

interface ResourceSummary {
    val id: Int
    val category: String
}

data class ApiResource(
    override val category: String,
    override val id: Int
) : ResourceSummary

data class NamedApiResource(
    val name: String,
    override val category: String,
    override val id: Int
) : ResourceSummary

interface ResourceSummaryList<out T : ResourceSummary> {
    val count: Int
    val next: String?
    val previous: String?
    val results: List<T>
}

data class ApiResourceList(
    override val count: Int,
    override val next: String?,
    override val previous: String?,
    override val results: List<ApiResource>
) : ResourceSummaryList<ApiResource>

data class NamedApiResourceList(
    override val count: Int,
    override val next: String?,
    override val previous: String?,
    override val results: List<NamedApiResource>
) : ResourceSummaryList<NamedApiResource>


private fun urlToId(url: String): Int {
    return "\\/-?[0-9]+\\/$".toRegex().find(url)!!.value.filter { it.isDigit() || it == '-' }
        .toInt()
}

private fun urlToCat(url: String): String {
    return "\\/[a-z\\-]+\\/-?[0-9]+\\/$".toRegex().find(url)!!.value.filter { it.isLetter() || it == '-' }
}

internal class ApiResourceAdapter : JsonDeserializer<ApiResource> {

    data class Json(val url: String)

    override fun deserialize(
        element: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): ApiResource {
        val temp = context.deserialize<Json>(element, TypeToken.get(Json::class.java).type)
        return ApiResource(category = urlToCat(temp.url), id = urlToId(temp.url))
    }
}

internal class NamedApiResourceAdapter : JsonDeserializer<NamedApiResource> {

    data class Json(val name: String, val url: String)

    override fun deserialize(
        element: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): NamedApiResource {
        val temp = context.deserialize<Json>(element, TypeToken.get(Json::class.java).type)
        return NamedApiResource(
            name = temp.name,
            category = urlToCat(temp.url),
            id = urlToId(temp.url)
        )
    }
}