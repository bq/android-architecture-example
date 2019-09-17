package com.adriangl.pokeapi_mvvm.utils.test

import android.util.Log
import mini.BaseAction
import mini.Chain
import mini.Dispatcher
import mini.Interceptor
import java.util.*

/**
 * [Interceptor] class for testing purposes which mute all the received actions.
 */
class TestDispatcherInterceptor : Interceptor {
    override fun invoke(action: Any, chain: Chain): Any {
        Log.d("TestDispatcherInterceptor", "Muted: $action")
        mutedActions.add(action)
        return TestOnlyAction
    }

    private val mutedActions = LinkedList<Any>()

    val actions: List<Any> get() = mutedActions
}

/**
 * Action for testing purposes.
 */
object TestOnlyAction : BaseAction()

typealias TestDispatcher = Dispatcher

private val testDispatcherInterceptor = TestDispatcherInterceptor()
fun TestDispatcher.actions() = testDispatcherInterceptor.actions

val testDispatcher: TestDispatcher =
    Dispatcher().apply { addInterceptor(testDispatcherInterceptor) }