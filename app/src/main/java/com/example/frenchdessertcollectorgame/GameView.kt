package com.example.frenchdessertcollectorgame

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.os.Handler
import android.os.Looper

class GameView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {
    private var localCalback: (Int) -> Unit = {}
    private var unitsPassed = 0

    private var countM = 0
    private var countB = 0
    private var countP = 0
    private var countBone = 0
    private var countFBone = 0

    private val pointB = 10
    private val pointM = 20
    private val pointP = 30
    private val pointBone = -10
    private val pointFBone = -20

    private val animatedBitmaps = mutableListOf<AnimatedBitmap>()
    private val paint = Paint()
    var h = 0f

    private val desiredWidth = 250
    private val desiredHeight = 250

    fun setTimerAdderListener(callback: (Int) -> Unit) {
        localCalback = callback
    }

    private val movableBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.player).let {
        Bitmap.createScaledBitmap(it, desiredWidth, desiredHeight, false)
    }
    private var movableBitmapX = 0f
    private var movableBitmapY = 0f
    private var movableBitmapDragging = false
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var gameListener: GameListener? = null

    private val images: List<Bitmap> = listOf(
        BitmapFactory.decodeResource(resources, R.drawable.macaron),
        BitmapFactory.decodeResource(resources, R.drawable.puff),
        BitmapFactory.decodeResource(resources, R.drawable.baguette),
        BitmapFactory.decodeResource(resources, R.drawable.bone),
        BitmapFactory.decodeResource(resources, R.drawable.fishbone),
    )

    private val handler = Handler(Looper.getMainLooper())
    private val addBitmapRunnable = object : Runnable {
        override fun run() {
            addNewAnimatedBitmap()
            val randomDelay = Random.nextLong(500, 1000)
            handler.postDelayed(this, randomDelay)
        }
    }

    fun setGameListener(listener: GameListener) {
        gameListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val iterator = animatedBitmaps.iterator()
        while (iterator.hasNext()) {
            val animatedBitmap = iterator.next()
            canvas.drawBitmap(animatedBitmap.bitmap, null, animatedBitmap.rect, paint)

            if (animatedBitmap.rect.bottom >= height) {
                iterator.remove()
            } else if (animatedBitmap.rect.intersect(Rect(movableBitmapX.toInt(), movableBitmapY.toInt(), (movableBitmapX + movableBitmap.width).toInt(), (movableBitmapY + movableBitmap.height).toInt()))) {
                iterator.remove()
                unitsPassed++

                when (animatedBitmap.bitmap) {
                    images[0] -> {
                        countM++
                        unitsPassed += pointM
                    }
                    images[1] -> {
                        countP++
                        unitsPassed += pointP
                    }
                    images[2] ->{
                        countB++
                        unitsPassed += pointB
                        localCalback(5)
                    }
                    images[3] -> {
                        countBone++
                        unitsPassed += pointBone
                    }
                    images[4] -> {
                        countFBone++
                        unitsPassed += pointFBone
                    }
                }
                a.buggeteScore = countB
                a.puffScore =countP
                a.macaroneScore = countM
                a.boneScore = countBone
                a.fishboneScore = countFBone
                a.totalScore = unitsPassed
                Log.d("GameView", "Units passed: $unitsPassed")
            }
        }

        canvas.drawBitmap(movableBitmap, movableBitmapX, movableBitmapY, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = (bottom - top).toFloat()
        val canvasWidth = right - left
        val canvasHeight = bottom - top

        val centerX = (right - left) / 2
        movableBitmapX = (centerX - movableBitmap.width / 2).toFloat()
        movableBitmapY = (h - movableBitmap.height).toFloat()

        initializeBitmaps(canvasWidth, canvasHeight)
    }

    private fun initializeBitmaps(canvasWidth: Int, canvasHeight: Int) {
        animatedBitmaps.clear()
        handler.post(addBitmapRunnable)
    }

    private fun addNewAnimatedBitmap() {
        val canvasWidth = width
        val canvasHeight = height

        val startX = Random.nextInt(0, canvasWidth - 100)
        val rect = Rect(startX, 0, startX + 100, 100)
        val bitmap = images.random()
        val animatedBitmap = AnimatedBitmap(bitmap, rect)
        animatedBitmaps.add(animatedBitmap)

        startBitmapAnimation(animatedBitmap, canvasHeight)
    }

    private fun startBitmapAnimation(animatedBitmap: AnimatedBitmap, canvasHeight: Int) {
        val animator = ValueAnimator.ofFloat(0f, canvasHeight.toFloat())
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            animatedBitmap.updatePosition(animatedValue)
            invalidate()
        }
        animator.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x >= movableBitmapX && event.x <= movableBitmapX + movableBitmap.width && event.y >= movableBitmapY && event.y <= movableBitmapY + movableBitmap.height) {
                    movableBitmapDragging = true
                    lastTouchX = event.x
                    lastTouchY = event.y
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (movableBitmapDragging) {
                    val dx = event.x - lastTouchX
                    moveMovableBitmap(dx)
                    lastTouchX = event.x
                    invalidate()
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                movableBitmapDragging = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun moveMovableBitmap(dx: Float) {
        val newLeft = movableBitmapX + dx
        val newRight = newLeft + movableBitmap.width

        if (newLeft >= 0 && newRight <= width) {
            movableBitmapX = newLeft
        } else {
            if (newLeft < 0) movableBitmapX = 0f
            if (newRight > width) movableBitmapX = (width - movableBitmap.width).toFloat()
        }
    }

    private class AnimatedBitmap(val bitmap: Bitmap, var rect: Rect) {
        fun updatePosition(y: Float) {
            val height = rect.height()
            rect.offsetTo(rect.left, y.toInt() - height)
        }
    }

    interface GameListener {
        fun onAllBitmapsDeleted(unitsPassed: Int)
    }
}

object a {
    var name = ""
    var buggeteScore = 0
    var macaroneScore = 0
    var puffScore = 0
    var fishboneScore = 0
    var boneScore = 0
    var time = ""
    var totalScore = 0
}

