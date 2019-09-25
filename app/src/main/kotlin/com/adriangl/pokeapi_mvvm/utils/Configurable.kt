package com.adriangl.pokeapi_mvvm.utils

/**
 * Exposes a configuration method that requires an instance of [T] to configure the class
 */
interface Configurable<T> {
    fun setup(data: T)
}

interface EmptyConfigurable : Configurable<Unit> {
    @Deprecated(message = "This method delegates to the non-parameters setup function", replaceWith = ReplaceWith("setup"))
    override fun setup(data: Unit) {
    }

    fun setup()
}