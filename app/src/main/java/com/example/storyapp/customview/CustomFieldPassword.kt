package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class CustomFieldPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnClickListener {

    init {
        setOnClickListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.invalid_password), null)
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
}