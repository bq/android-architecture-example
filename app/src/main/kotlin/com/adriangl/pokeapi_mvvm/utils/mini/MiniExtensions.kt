package com.adriangl.pokeapi_mvvm.utils.mini

import mini.Resource
import mini.allSuccesful
import mini.anyFailure
import mini.anyLoading

fun <T> Iterable<Resource<T>>.anyEmpty(): Boolean = this.any { it.isEmpty }

fun <T> Iterable<Resource<T>>.onAllSuccessful(fn: () -> Unit): Iterable<Resource<T>> {
    if (this.allSuccesful()) fn()
    return this
}

fun <T> Iterable<Resource<T>>.onAnyFailure(fn: () -> Unit): Iterable<Resource<T>> {
    if (this.anyFailure()) fn()
    return this
}

fun <T> Iterable<Resource<T>>.onAnyLoading(fn: () -> Unit): Iterable<Resource<T>> {
    if (this.anyLoading()) fn()
    return this
}

fun <T> Iterable<Resource<T>>.onAnyEmpty(fn: () -> Unit): Iterable<Resource<T>> {
    if (this.anyEmpty()) fn()
    return this
}