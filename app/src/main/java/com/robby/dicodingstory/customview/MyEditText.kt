package com.robby.dicodingstory.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.robby.dicodingstory.R

class MyEditText : AppCompatEditText {
    private var _hasError = false

    val hasError: Boolean
        get() = _hasError

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) return

                when (inputType - 1) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD -> validatePassword(s)
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> validateEmail(s)
                    EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME -> validatePersonName(s)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(s: CharSequence) {
        if (s.length < MIN_PASSWORD_LENGTH) {
            _hasError = true
            error = context.getString(R.string.password_error_message)
        } else {
            _hasError = false
        }
    }

    private fun validateEmail(s: CharSequence) {
        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            _hasError = true
            error = context.getString(R.string.email_error_message)
        } else {
            _hasError = false
        }
    }

    private fun validatePersonName(s: CharSequence) {
        val namePattern = Regex("^[a-zA-Z-' ]{2,}\$")
        if (!namePattern.containsMatchIn(s)) {
            _hasError = true
            error = context.getString(R.string.name_error_message)
        } else {
            _hasError = false
        }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}