package com.hoffmann.emilio.project3.button

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.hoffmann.emilio.project3.R

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 2000L
    }

    private lateinit var paint: Paint
    private var loadingLevel = 0f
    private var mWidth = 0
    private var mHeight = 0

    private var buttonDisplayedText = ""
    private var mBackgroundColor: Int = 0
    private var backgroundLoadingColor: Int = 0
    private var circleLoadingColor: Int = 0
    private var buttonTextColor: Int = 0
    private var initialButtonText: String = ""
    private var downloadButtonText: String = ""

    private var buttonState: ButtonState = ButtonState.Initial

    private val textRect = Rect()
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.CustomButton, defStyleAttr, 0) {
            buttonTextColor = getInt(R.styleable.CustomButton_android_textColor, 0)
            mBackgroundColor = getInt(R.styleable.CustomButton_android_backgroundTint, 0)
            backgroundLoadingColor = getInt(R.styleable.CustomButton_backgroundLoadingColor, 0)
            circleLoadingColor = getInt(R.styleable.CustomButton_circleLoadingColor, 0)

            initialButtonText = getString(R.styleable.CustomButton_initialText) ?: ""
            downloadButtonText = getString(R.styleable.CustomButton_downloadingText) ?: ""
        }
    }

    fun setLoadingState(isLoading: Boolean, downloadPercentage: Float = 0f) {
        buttonState = if (isLoading) {
            if (downloadPercentage != loadingLevel) {
                setLoadingLevel(downloadPercentage)
            }
            ButtonState.Loading
        } else {
            setLoadingLevel(downloadPercentage)
            ButtonState.Initial
        }
    }

    private fun setLoadingLevel(newPercentage: Float) {
        val animator = ValueAnimator.ofFloat(loadingLevel, newPercentage)
        animator.addUpdateListener {
            loadingLevel = it.animatedValue as Float
            invalidate()
        }
        animator.disableViewDuringAnimation()
        animator.duration = DEFAULT_ANIMATION_DURATION
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas)

        when (buttonState) {
            ButtonState.Initial -> {
                buttonDisplayedText = initialButtonText
                drawText(canvas)
            }
            ButtonState.Loading -> {
                buttonDisplayedText = downloadButtonText
                drawRectLoading(canvas)
                drawLoadingCircle(canvas)
                drawText(canvas)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val measuredWidth: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val measuredHeight: Int =
            resolveSizeAndState(MeasureSpec.getSize(measuredWidth), heightMeasureSpec, 0)
        mWidth = measuredWidth
        mHeight = measuredHeight

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun drawBackground(canvas: Canvas?) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mBackgroundColor
        }
        canvas?.drawColor(paint.color)
    }

    private fun drawRectLoading(canvas: Canvas?) {
        paint.color = backgroundLoadingColor
        canvas?.drawRect(
            0f,
            0f,
            loadingLevel * mWidth.toFloat(),
            mHeight.toFloat(),
            paint
        )
    }

    private fun drawText(canvas: Canvas?) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = context.resources.getDimension(R.dimen.default_text_size)
            color = buttonTextColor
        }
        paint.getTextBounds(buttonDisplayedText, 0, buttonDisplayedText.length, textRect)
        pointPosition.calculateTextXY(textRect)
        canvas?.drawText(buttonDisplayedText, pointPosition.x, pointPosition.y, paint)
    }

    private fun drawLoadingCircle(canvas: Canvas?) {
        paint.color = circleLoadingColor

        val rectF = RectF()
        val circleDiameter = 35.0f
        val circleSize = mHeight - paddingBottom - circleDiameter
        rectF.set(circleDiameter, circleDiameter, circleSize, circleSize)

        canvas?.drawArc(
            rectF,
            0F,
            loadingLevel * 360F,
            true,
            paint
        )
    }

    private fun PointF.calculateTextXY(textRect: Rect) {
        x = mWidth.toFloat() / 2
        y = mHeight.toFloat() / 2 - textRect.centerY()
    }

    private fun ValueAnimator.disableViewDuringAnimation() {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isEnabled = true
            }
        })
    }

    enum class ButtonState {
        Initial,
        Loading
    }
}
