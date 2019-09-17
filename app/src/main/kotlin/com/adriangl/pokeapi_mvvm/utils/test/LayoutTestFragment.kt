package com.adriangl.pokeapi_mvvm.utils.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import com.adriangl.pokeapi_mvvm.utils.fragment.argument

/**
 * Fragment used to inflate a layout to test.
 */
@RestrictTo(RestrictTo.Scope.TESTS)
class LayoutTestFragment : Fragment() {

    companion object {
        private const val LAYOUT_ID = "layout_id"
        @Suppress("UndocumentedPublicFunction")
        fun newInstance(@LayoutRes layoutId: Int) = LayoutTestFragment().apply {
            arguments = Bundle().apply { putInt(LAYOUT_ID, layoutId) }
        }
    }

    private val layoutId: Int by argument(LAYOUT_ID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }
}
