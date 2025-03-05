package com.example.submissionstoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class EmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val emailRegex = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = this@EmailEditText.parent?.parent
                if (parent is TextInputLayout) {
                    parent.error = if (!isValidEmail(s.toString())) {
                        "Format email tidak valid"
                    } else {
                        null
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}})
    }

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matcher(email).matches()

    }
}