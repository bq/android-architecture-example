package com.bq.arch_example.utils.fragment

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mini.onUi

/**
 * Extension functions for the [Fragment] class.
 */

/** Exception thrown on error when retrieving an argument from arguments in a [Fragment] */
class FragmentArgumentException(message: String) : RuntimeException(message)

/**
 * Retrieves an argument from the arguments map in a [Fragment].
 *
 * If the value exists in the fragment arguments but it cannot be casted to the specified class,
 * an exception will be thrown.
 *
 * @exception [FragmentArgumentException] when it fails to retrieve the desired [T] object.
 */
inline fun <reified T> Fragment.argument(key: String): Lazy<T> = lazy {
    try {
        arguments?.get(key) as T
    } catch (e: ClassCastException) {
        throw FragmentArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    } catch (e: Exception) {
        throw FragmentArgumentException("Argument $key is required")
    }
}

/**
 * Retrieves an argument from the arguments map in a [Fragment].
 * If the value couldn't be obtained, returns the default value specified as parameter.
 *
 * If the value exists in the fragment arguments but it cannot be casted to the specified class,
 * an exception will be thrown.
 *
 * @exception [FragmentArgumentException] when the value cannot be cast to [T].
 */
inline fun <reified T> Fragment.argument(key: String, defaultValue: T): Lazy<T> = lazy {
    try {
        arguments?.get(key) as T
    } catch (e: ClassCastException) {
        throw FragmentArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    } catch (e: Exception) {
        defaultValue
    }
}

/**
 * Retrieves an optional argument from the arguments map in a [Fragment].
 * If it doesn't exist, returns null as the value.
 *
 * If the value exists in the fragment arguments but it cannot be casted to the specified class,
 * an exception will be thrown.
 *
 * @exception [FragmentArgumentException] when the value cannot be cast to [T].
 */
inline fun <reified T> Fragment.optionalArgument(key: String): Lazy<T?> = lazy {
    try {
        arguments?.get(key) as T?
    } catch (e: ClassCastException) {
        throw FragmentArgumentException("Argument $key cannot be cast to class ${T::class.qualifiedName}")
    }
}

/**
 * Displays a text as a toast in the current fragment.
 *
 * @param text Text to display in the toast
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Fragment.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, duration).show()
}

/**
 * Displays a text as a toast in the current fragment.
 *
 * @param stringResId Text to display in the toast as a string resource ID
 * @param duration Duration, one of [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Fragment.showToast(@StringRes stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, stringResId, duration).show()
}

/**
 * Shows a [Snackbar] given a container [View], message an optionally a duration and action
 */
fun Fragment.showSnackbar(
    view: View?,
    message: CharSequence,
    length: Int = Snackbar.LENGTH_LONG
) {
    view?.let {
        showSnackbar(Snackbar.make(view, message, length))
    }
}

/**
 * Shows a [Snackbar] given a container [View], resource message an optionally a duration and action
 */
fun Fragment.showSnackbar(
    view: View?,
    @StringRes message: Int,
    length: Int = Snackbar.LENGTH_LONG
) {
    view?.let {
        showSnackbar(Snackbar.make(view, message, length))
    }
}

/**
 * Shows a [Snackbar] safely.
 */
fun Fragment.showSnackbar(snackbar: Snackbar) {
    onUi {
        snackbar.show()
    }
}