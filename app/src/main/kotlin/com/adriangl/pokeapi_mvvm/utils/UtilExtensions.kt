package com.adriangl.pokeapi_mvvm.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline cb: (T)-> Unit ) {
    observe(owner, Observer {
        cb(it)
    })
}
