package ymsli.com.ea1h.utils.common

import android.text.Editable
import android.text.TextWatcher

/**
 * Provides default implementation of TextWatcher, so that consumers only need to implement
 * required functions.
 *
 * @author VE00YM023
 */
open class TextWatcherAdapter: TextWatcher {
    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}