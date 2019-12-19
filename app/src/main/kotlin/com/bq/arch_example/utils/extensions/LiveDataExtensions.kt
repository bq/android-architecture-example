package com.bq.arch_example.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import mini.Resource


/**
 * Observes a given [LiveData].
 */
inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline cb: (T) -> Unit) = observe(owner, Observer { cb(it) })

/**
 * Observes a given the [LiveData] only once.
 */
inline fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline cb: (T) -> Unit) =
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            cb(t)
            removeObserver(this)
        }
    })

/**
 * Binds a [Resource] to a live data which computes the terminal states.
 *
 * The callbacks provided are:
 * - onChanged: general purpose resource listening. It sets the resource in the function's scope
 * - onLoading: triggered when the [Resource] is in loading status. It sets the value of the resource in the function's scope
 * - onSuccess: triggered when the [Resource] is in success status. It sets the value of the resource in the function's scope
 * - onFailure: triggered when the [Resource] is in failure status. It sets the exception of the resource in the function's scope
 * - onEmpty: triggered when the [Resource] is in empty status.
 */
inline fun <U, T : Resource<U>> LiveData<T>.observeResource(owner: LifecycleOwner,
                                                            crossinline onChanged: (Resource<U>) -> Unit = {},
                                                            crossinline onLoading: (U?) -> Unit = {},
                                                            crossinline onSuccess: (U) -> Unit = {},
                                                            crossinline onFailure: (Throwable?) -> Unit = {},
                                                            crossinline onEmpty: () -> Unit = {}) {
    observe(owner) {
        onChanged(it)
        if (it.isLoading) onLoading(it.getOrNull())
        if (it.isSuccess) onSuccess(it.getOrNull()!!)
        if (it.isFailure) onFailure(it.exceptionOrNull())
        if (it.isEmpty) onEmpty()
    }
}

/**
 * Binds a [Resource] to a live data once, which computes the terminal states.
 *
 * The callbacks provided are:
 * - onChanged: general purpose resource listening. It sets the resource in the function's scope
 * - onLoading: triggered when the [Resource] is in loading status. It sets the value of the resource in the function's scope
 * - onSuccess: triggered when the [Resource] is in success status. It sets the value of the resource in the function's scope
 * - onFailure: triggered when the [Resource] is in failure status. It sets the exception of the resource in the function's scope
 * - onEmpty: triggered when the [Resource] is in empty status.
 */
inline fun <U, T : Resource<U>> LiveData<T>.observeResourceOnce(owner: LifecycleOwner,
                                                                crossinline onChanged: (Resource<U>) -> Unit = {},
                                                                crossinline onLoading: (U?) -> Unit = {},
                                                                crossinline onSuccess: (U) -> Unit = {},
                                                                crossinline onFailure: (Throwable?) -> Unit = {},
                                                                crossinline onEmpty: () -> Unit = {}) {
    observeOnce(owner) {
        onChanged(it)
        if (it.isLoading) onLoading(it.getOrNull())
        if (it.isSuccess) onSuccess(it.getOrNull()!!)
        if (it.isFailure) onFailure(it.exceptionOrNull())
        if (it.isEmpty) onEmpty()
    }
}