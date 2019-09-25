package com.adriangl.pokeapi_mvvm.utils.mini.rx

import io.reactivex.Flowable
import mini.Store
import mini.rx.flowable
import mini.rx.select

/**
 * Builder function for [StateMerger].
 */
inline fun <R> mergeStates(crossinline builder: StateMerger<R>.() -> Unit): Flowable<List<R>> {
    return StateMerger<R>().apply { builder() }.flowable()
}

/**
 * Builder function for [StateMerger].
 */
inline fun <R> mergeStatesWithoutStartState(crossinline builder: StateMerger<R>.() -> Unit): Flowable<List<R>> {
    return StateMerger<R>().apply { builder() }.flowableWithoutStartState()
}

/**
 * Builder function for [StateMerger].
 */
inline fun <T> mergeListStates(crossinline builder: StateListMerger<T>.() -> Unit): Flowable<List<T>> {
    return StateListMerger<T>().apply { builder() }.flowable()
}

/**
 * Builder function for [StateMerger].
 */
inline fun <T> mergeListStatesWithoutStartState(crossinline builder: StateListMerger<T>.() -> Unit): Flowable<List<T>> {
    return StateListMerger<T>().apply { builder() }.flowableWithoutStartState()
}

/**
 * Combine multiple store state flowables into a list of type R. If multiple stores
 * change at the same this will emit twice, one for each store.
 *
 * ```
 * mergeStates<Task> {
 *  merge(linesStore) { loadProductTask[productId] }
 *  merge(upsellStore) { upsellsTasks[productId] }
 * }
 * ```
 */
class StateMerger<R> {
    val storeAndMappers = ArrayList<Pair<Store<*>, () -> R>>()

    /** Add a new store + mapper to the flowable. */
    inline fun <S : Store<U>, U : Any> merge(store: S, crossinline mapper: (U.() -> R)) {
        storeAndMappers.add(store to { store.state.mapper() })
    }

    /** Build the StateMerger into the final flowable. */
    fun flowable(): Flowable<List<R>> {
        return storeAndMappers
            .map { (store, fn) -> store.flowable().select { fn() } }
            .reduce { acc, storeFlowable ->
                acc.mergeWith(storeFlowable)
            }
            .map {
                storeAndMappers.map { (_, fn) -> fn() }.toList()
            }
            .onBackpressureLatest()
    }

    /** Build the StateMerger into the final flowable. */
    fun flowableWithoutStartState(): Flowable<List<R>> {
        return storeAndMappers
            .map { (store, fn) -> store.flowable(hotStart = false).select { fn() } }
            .reduce { acc, storeFlowable ->
                acc.mergeWith(storeFlowable)
            }
            .map {
                storeAndMappers.map { (_, fn) -> fn() }.toList()
            }
            .onBackpressureLatest()
    }
}

/**
 * Same as `StateMerger` but it merges states that holds lists.
 */
class StateListMerger<T> {
    val storeAndMappers = ArrayList<Pair<Store<*>, () -> List<T>>>()

    /** Add a new store + mapper to the flowable.
     *  Default param is returned when the list is empty.
     */
    inline fun <S : Store<U>, U : Any> mergeList(
        store: S,
        default: T,
        crossinline mapper: (U.() -> List<T>)
    ) {
        storeAndMappers.add(store to {
            val list = store.state.mapper()
            if (list.count() == 0) {
                listOf(default)
            } else {
                list
            }
        })
    }

    /** Build the StateMerger into the final flowable. */
    fun flowable(): Flowable<List<T>> {
        return storeAndMappers
            .map { (store, fn) -> store.flowable().select { fn() } }
            .reduce { acc, storeFlowable ->
                acc.mergeWith(storeFlowable)
            }
            .map {
                storeAndMappers.flatMap { (_, fn) -> fn() }.toList()
            }
            .onBackpressureLatest()
    }

    /** Build the StateMerger into the final flowable. */
    fun flowableWithoutStartState(): Flowable<List<T>> {
        return storeAndMappers
            .map { (store, fn) -> store.flowable(false).select { fn() } }
            .reduce { acc, storeFlowable ->
                acc.mergeWith(storeFlowable)
            }
            .map {
                storeAndMappers.flatMap { (_, fn) -> fn() }.toList()
            }
            .onBackpressureLatest()
    }
}