package com.bq.arch_example.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations


inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline cb: (T) -> Unit) {
    observe(owner, Observer {
        cb(it)
    })
}

inline fun <T, X> LiveData<T>.map(crossinline cb: (T) -> X): LiveData<X> =
    Transformations.map(this) { cb(it) }

inline fun <T, X> LiveData<T>.switchMap(crossinline cb: (T) -> LiveData<X>): LiveData<X> =
    Transformations.switchMap(this) { cb(it) }