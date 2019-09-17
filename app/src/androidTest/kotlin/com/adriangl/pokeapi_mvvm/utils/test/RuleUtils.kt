package com.adriangl.pokeapi_mvvm.utils.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import kotlin.reflect.KClass

/**
 * Creates an [ActivityTestRule] with a given intent for an specific activity.
 */
fun <T : Activity> testActivity(
    clazz: KClass<T>,
    initialTouchMode: Boolean = false,
    launchActivity: Boolean = true,
    createIntent: ((context: Context) -> Intent)? = null
): ActivityTestRule<T> {
    //Overriding the rule throws an exception
    if (createIntent == null) return ActivityTestRule<T>(
        clazz.java,
        initialTouchMode,
        launchActivity
    )
    return object : ActivityTestRule<T>(clazz.java, initialTouchMode, launchActivity) {
        override fun getActivityIntent(): Intent =
            createIntent(ApplicationProvider.getApplicationContext())
    }
}

typealias FragmentTestRule = ActivityTestRule<TestFragmentActivity>

/**
 * Creates a [FragmentTestRule] for an specific fragment.
 */
fun <T : Fragment> testFragment(factory: () -> T): FragmentTestRule {
    return object : FragmentTestRule(TestFragmentActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val fragment = factory()
            return TestFragmentActivity.createIntent(
                ApplicationProvider.getApplicationContext(),
                fragment.javaClass.kotlin,
                fragment.arguments
            )
        }
    }
}