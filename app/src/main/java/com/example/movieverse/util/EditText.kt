package com.example.movieverse.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
private fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trySend(p0).isSuccess
            }

            override fun afterTextChanged(p0: Editable?) {}

        }
        addTextChangedListener(listener)
        awaitClose {
            removeTextChangedListener(listener)
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun EditText.callBackWhileTyping(
    delay: Long = 1000L,
    callback: (CharSequence?) -> Unit
): Flow<CharSequence?> =
    textChanges().debounce(delay).onEach {
        callback(it)
    }