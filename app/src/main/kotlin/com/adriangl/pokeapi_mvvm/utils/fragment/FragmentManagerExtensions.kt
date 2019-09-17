package com.adriangl.pokeapi_mvvm.utils.fragment

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Extension methods for [FragmentManager] common tasks.
 */

/**
 * Performs a fragment transaction defined in the lambda function.
 *
 * @param addToBackStack Whether or not to add the transaction to the back stack. Defaults to false
 * @param backStackName Name provided to identify the transaction in the back stack. Defaults to null
 * @param fn The transaction to perform
 */
inline fun FragmentManager.performTransaction(addToBackStack: Boolean = false,
                                              backStackName: String? = null,
                                              crossinline fn: FragmentTransaction.() -> Unit) {
    val transaction = this.beginTransaction()
    fn(transaction)
    if (addToBackStack) transaction.addToBackStack(backStackName)
    transaction.commit()
}

/**
 * Performs a fragment transaction defined in the lambda function, allowing state loss.
 *
 * @param addToBackStack Whether or not to add the transaction to the back stack. Defaults to false
 * @param backStackName Name provided to identify the transaction in the back stack. Defaults to null
 * @param fn The transaction to perform
 */
inline fun FragmentManager.performTransactionAllowingStateLoss(addToBackStack: Boolean = false,
                                                               backStackName: String? = null,
                                                               crossinline fn: FragmentTransaction.() -> Unit) {
    val transaction = this.beginTransaction()
    fn(transaction)
    if (addToBackStack) transaction.addToBackStack(backStackName)
    transaction.commitAllowingStateLoss()
}

/**
 * Apply a cross-fade animation to switch the fragments. Must be called before the add/replace.
 */
fun FragmentTransaction.crossfade(): FragmentTransaction {
    return setCustomAnimations(
        android.R.anim.fade_in,
        android.R.anim.fade_out,
        android.R.anim.fade_in,
        android.R.anim.fade_out)
}