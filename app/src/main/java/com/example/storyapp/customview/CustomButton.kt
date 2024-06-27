package com.example.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomButton : AppCompatButton {
    private lateinit var enabledButton: Drawable
    private lateinit var disabledButton: Drawable
    private var textColor : Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledButton else disabledButton
        setTextColor(textColor)
        textSize = 18f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Submit" else "Enter your data"
    }

    private fun init() {
        textColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledButton = ContextCompat.getDrawable(context, R.drawable.login_button) as Drawable
        disabledButton = ContextCompat.getDrawable(context, R.drawable.login_button) as Drawable
    }
}