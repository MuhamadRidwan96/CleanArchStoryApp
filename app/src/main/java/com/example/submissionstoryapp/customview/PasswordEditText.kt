package com.example.submissionstoryapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {
    init {
        isFocusable = true
        isFocusableInTouchMode = true

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = this@PasswordEditText.parent?.parent // Ambil TextInputLayout
                if (parent is TextInputLayout) {
                    parent.error = if ((s?.length ?: 0) < 8) {
                        "Password harus terdiri dari minimal 8 karakter"
                    } else {
                        null // Hapus error jika valid
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}