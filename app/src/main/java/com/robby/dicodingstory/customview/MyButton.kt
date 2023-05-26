package com.robby.dicodingstory.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.robby.dicodingstory.R

class MyButton : AppCompatButton {
    private var isSecondary = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = ContextCompat.getDrawable(
            context,
            if (!isSecondary) R.drawable.my_button_background else R.drawable.my_secondary_button_background
        )
        setTextColor(
            ContextCompat.getColor(
                context,
                if (!isSecondary) R.color.white else R.color.navy
            )
        )
        textSize = 15f
        isAllCaps = true
        gravity = Gravity.CENTER
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MyButton)
            isSecondary = typedArray.getBoolean(R.styleable.MyButton_isSecondary, false)
            typedArray.recycle()
        }
    }
}