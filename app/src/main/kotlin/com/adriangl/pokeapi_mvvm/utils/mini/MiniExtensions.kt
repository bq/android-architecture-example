package com.adriangl.pokeapi_mvvm.utils.mini

import mini.*

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

fun Iterable<Task>.onAnyIdle(fn: () -> Unit): Iterable<Task> = onAnyEmpty(fn).map { it as Task }