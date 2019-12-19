package com.bq.arch_example.utils.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import mini.onUi

/**
 * Extension functions for the [Activity] class.
 */

/** Exception thrown on error when retrieving an argument from extras in an [Activity] */
class ActivityArgumentException(message: String) : RuntimeException(message)

/**
 * Retrieves an argument from the [Intent] extras map in an [Activity].
 *
 * @exception [ActivityArgumentException] when it fails to retrieve the desired [T] object
 */
inline fun <reified T> Activity.argument(key: String): Lazy<T> = lazy {
    try {
        intent?.extras?.get(key) as T
    } catch (e: ClassCastException) {
        throw ActivityArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    } catch (e: Exception) {
        throw ActivityArgumentException("Argument $key is required")
    }
}

/**
 * Retrieves an argument from the [Intent] extras map in an [Activity].
 * If the value couldn't be obtained, returns the default value specified as parameter.
 *
 * If the value exists in the fragment arguments but it cannot be casted to the specified class,
 * an exception will be thrown.
 *
 * @exception [FragmentArgumentException] when the value cannot be cast to [T].
 */
inline fun <reified T> Activity.argument(key: String, defaultValue: T): Lazy<T> = lazy {
    try {
        intent?.extras?.get(key) as T
    } catch (e: ClassCastException) {
        throw ActivityArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    } catch (e: Exception) {
        defaultValue
    }
}

/**
 * Retrieves an optional argument from the [Intent] extras map in an [Activity].
 * If it doesn't exist, returns null as the value.
 *
 * If the value exists in the fragment arguments but it cannot be casted to the specified class,
 * an exception will be thrown.
 *
 * @exception [FragmentArgumentException] when the value cannot be cast to [T].
 */
inline fun <reified T> Activity.optionalArgument(key: String): Lazy<T?> = lazy {
    try {
        intent?.extras?.get(key) as T?
    } catch (e: ClassCastException) {
        throw ActivityArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    }
}

/**
 * Displays a text as a toast in the current activity.
 *
 * @param text Text to display in the toast
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Activity.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

/**
 * Displays a text as a toast in the current activity.
 *
 * @param stringResId Text to display in the toast as a string resource ID
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Activity.showToast(@StringRes stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringResId, duration).show()
}

/**
 * Shows a [Snackbar] given a container [View], message an optionally a duration and action
 */
fun Activity.showSnackbar(
    view: View?,
    message: CharSequence,
    length: Int = Snackbar.LENGTH_LONG
) {
    view?.let {
        showSnackbar(Snackbar.make(it, message, length))
    }
}

/**
 * Shows a [Snackbar] given a container [View], resource message an optionally a duration and action
 */
fun Activity.showSnackbar(
    view: View?,
    @StringRes message: Int,
    length: Int = Snackbar.LENGTH_LONG
) {
    view?.let {
        showSnackbar(Snackbar.make(it, message, length))
    }
}

/**
 * Shows a [Snackbar] safely.
 */
fun Activity.showSnackbar(snackbar: Snackbar) {
    onUi {
        snackbar.show()
    }
}