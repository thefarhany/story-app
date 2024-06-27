package com.example.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class CustomFieldEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnClickListener {

    init {
        setOnClickListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()

                if (!isValidEmail(email)) {
                    error = context.getString(R.string.invalid_email)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        })
    }

    override fun onClick(view: View?) {
        error = null
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}