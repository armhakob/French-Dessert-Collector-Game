package com.example.frenchdessertcollectorgame

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.random.Random


class GameView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, deffStyleAttr: Int = 0):View(context, attributeSet, deffStyleAttr) {
    private var unitsPassed = 0

    private val animatedRects = mutableListOf<AnimatedRect>()
    private val paint = Paint()
    var h = 0f


    private var blackRect: Rect = Rect(0, 0, 100, 100)
    private var blackRectDragging = false
    private var lastTouchY = 0f
    private var lastTouchX = 0f
    private var gameListener: GameListener? = null

    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)

        val iterator = animatedRects.iterator()
        while (iterator.hasNext()) {
            val animatedRect = iterator.next()
            paint.color = animatedRect.color
            canvas.drawRect(animatedRect.rect, paint)

            if (animatedRect.rect.bottom >= height) {
                iterator.remove()
            } else if (animatedRect.rect.intersect(blackRect)) {
                iterator.remove()
                unitsPassed++
                Log.d("GameView", "Units passed: $unitsPassed")
            }
        }

        paint.color = Color.BLACK
        canvas.drawRect(blackRect, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("B:", bottom.toString())
        Log.d("B:", top.toString())
        h = (bottom - top).toFloat()
        val canvasWidth = right - left
        val canvasHeight = bottom - top

        val centerX = (right - left) / 2
        blackRect.offsetTo(centerX - blackRect.width() / 2, (h - blackRect.height()).toInt())

        initializeRectangles(canvasWidth, canvasHeight)
    }

    private fun initializeRectangles(canvasWidth: Int, canvasHeight: Int) {
        animatedRects.clear()

        val numRects = 10
        for (i in 0 until numRects) {
            val rectSize = 100
            val startX = Random.nextInt(0, canvasWidth - rectSize)
            val rect = Rect(startX, 0, startX + rectSize, rectSize)
            val color = getRandomColor()
            val animatedRect = AnimatedRect(rect, color)
            animatedRects.add(animatedRect)

            val startDelay = (i * 500).toLong()
            startRectangleAnimation(animatedRect, canvasHeight, startDelay)
        }
    }

    private fun startRectangleAnimation(animatedRect: AnimatedRect, canvasHeight: Int, startDelay: Long) {
        val animator = ValueAnimator.ofFloat(0f, canvasHeight.toFloat())
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.startDelay = startDelay
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            animatedRect.updatePosition(animatedValue)
            invalidate()
        }
        animator.start()
    }

//    private fun updateRectanglesPosition(animatedValue: Float) {
//        for (rect in rectangles) {
//            val height = rect.height()
//            rect.offsetTo(rect.left, (animatedValue - height).toInt())
//        }
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (blackRect.contains(event.x.toInt(), event.y.toInt())) {
                    blackRectDragging = true
                    lastTouchY = event.y
                    lastTouchX = event.x
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (blackRectDragging) {
                    val dy = event.y - lastTouchY
                    val dx = event.x - lastTouchX
                    moveBlackRect(dx, dy)
                    lastTouchY = event.y
                    lastTouchX = event.x
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                blackRectDragging = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun moveBlackRect(dx: Float, dy: Float) {
        val newLeft = blackRect.left + dx.toInt()
        val newTop = blackRect.top + dy.toInt()
        val newRight = blackRect.right + dx.toInt()
        val newBottom = blackRect.bottom + dy.toInt()

        if (newLeft >= 0 && newRight <= width && newTop >= 0 && newBottom <= height) {
            blackRect.offset(dx.toInt(), dy.toInt())
        } else {
            if (newLeft < 0) blackRect.offsetTo(0, blackRect.top)
            if (newRight > width) blackRect.offsetTo(width - blackRect.width(), blackRect.top)
            if (newTop < 0) blackRect.offsetTo(blackRect.left, 0)
            if (newBottom > height) blackRect.offsetTo(blackRect.left, height - blackRect.height())
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private class AnimatedRect(var rect: Rect, val color: Int) {
        fun updatePosition(y: Float) {
            val height = rect.height()
            rect.offsetTo(rect.left, y.toInt() - height)
        }
    }

    interface GameListener {
        fun onAllRectanglesDeleted(unitsPassed: Int)
    }
}