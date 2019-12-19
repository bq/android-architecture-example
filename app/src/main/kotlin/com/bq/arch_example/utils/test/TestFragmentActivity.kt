package com.bq.arch_example.utils.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bq.arch_example.R
import com.bq.arch_example.utils.extensions.argument
import com.bq.arch_example.utils.extensions.performTransaction
import kotlin.reflect.KClass

/**
 * [AppCompatActivity] that can hold a [Fragment] to ease testing isolated fragments with Espresso.
 *
 * It assumes that the [Fragment] that we want to load has a default constructor.
 */
@RestrictTo(RestrictTo.Scope.TESTS)
class TestFragmentActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_CLASS_KEY = "fragment_class"
        private const val FRAGMENT_ARGS_KEY = "fragment_args"
        private const val FRAGMENT_VIEW_CONTENT_ID_KEY =
            "fragment_view_content_id" // In case we want to do tests with fragment transitions
        private const val FRAGMENT_DEFAULT_VIEW_CONTENT_ID = R.id.test_fragment_content_view_id

        fun <T : Fragment> createIntent(
            context: Context,
            clazz: KClass<T>,
            args: Bundle? = null,
            @IdRes viewContentId: Int = FRAGMENT_DEFAULT_VIEW_CONTENT_ID
        ): Intent =
            Intent(context, TestFragmentActivity::class.java).apply {
                putExtra(FRAGMENT_CLASS_KEY, clazz.java.canonicalName)
                putExtra(FRAGMENT_VIEW_CONTENT_ID_KEY, viewContentId)
                putExtra(FRAGMENT_ARGS_KEY, args)
            }
    }

    private lateinit var content: FrameLayout
    private val fragmentClassName: String by argument(FRAGMENT_CLASS_KEY)
    private val fragmentArgs: Bundle? by argument(FRAGMENT_ARGS_KEY)
    private val fragmentViewContentId: Int by argument(FRAGMENT_VIEW_CONTENT_ID_KEY)

    val fragmentToTest: Fragment by lazy {
        supportFragmentManager.fragmentFactory.instantiate(classLoader, fragmentClassName)
            .apply { arguments = fragmentArgs }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = FrameLayout(this)
        content.id = fragmentViewContentId
        content.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        setContentView(content)

        try {
            supportFragmentManager.performTransaction {
                replace(fragmentViewContentId, fragmentToTest)
            }
        } catch (e: Exception) {
            throw UnsupportedOperationException("The fragment with class name $fragmentClassName could not be instantiated")
        }
    }
}