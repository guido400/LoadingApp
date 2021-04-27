package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = "Download"
    private var paint = Paint()
    private var loadingWidth = 0f
    private var loadingAngle = 0f

    private var buttonPrimaryColor: Int = 0
    private var buttonSecondaryColor: Int = 0
    private var buttonTextColor: Int = 0
    private var animationDuration: Int = 3000

    private var valueAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = "We are loading"
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
                    duration = animationDuration.toLong()
                    repeatMode = ValueAnimator.REVERSE

                    addUpdateListener {
                        loadingWidth = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }

                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = animationDuration.toLong()
                    repeatMode = ValueAnimator.REVERSE

                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                        if (animatedValue == 360f) {
                            buttonState = ButtonState.Completed
                        }
                    }
                    start()
                }
            }
            ButtonState.Completed -> {
                buttonText = "Download"
                loadingWidth = 0f
                loadingAngle = 0f

                this@LoadingButton.invalidate()
            }

            else -> {
                buttonState = ButtonState.Completed
            }
        }
    }

    init {
        isClickable = true

        buttonState = ButtonState.Clicked

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonPrimaryColor = getColor(R.styleable.LoadingButton_buttonPrimaryColor, 0)
            buttonSecondaryColor = getColor(R.styleable.LoadingButton_buttonSecondaryColor, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_buttonTextColor, 0)
            animationDuration = getInt(R.styleable.LoadingButton_animationDuration, 0)
        }


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = buttonPrimaryColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = buttonSecondaryColor
        canvas?.drawRect(0f, 0f, loadingWidth, heightSize.toFloat(), paint)


        paint.color = buttonTextColor
        paint.textSize = resources.getDimension(R.dimen.default_text_size)
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            buttonText, (0.5 * widthSize).toFloat(),
            (0.6 * heightSize).toFloat(), paint
        )

        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas?.drawArc(
            (0.70 * widthSize).toFloat(),
            (0.275 * heightSize).toFloat(),
            (0.70 * widthSize).toFloat() + 70f,
            (0.275 * heightSize).toFloat() + 70f,
            0f,
            loadingAngle,
            true,
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState = ButtonState.Loading
        return true
    }

}