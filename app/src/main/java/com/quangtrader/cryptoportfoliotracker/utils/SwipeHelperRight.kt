package com.quangtrader.cryptoportfoliotracker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList
import androidx.core.graphics.createBitmap

@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelperRight(context: Context, recyclerView: RecyclerView) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    companion object {
        const val BUTTON_WIDTH = 140
    }

    private var recyclerView: RecyclerView = recyclerView
    private val buttons = mutableListOf<UnderlayButton>()
    private lateinit var gestureDetector: GestureDetector
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private val buttonsBuffer = mutableMapOf<Int, MutableList<UnderlayButton>>()
    private val recoverQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            return if (contains(element)) false else super.add(element)
        }
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons) {
                if (button.onClick(e.x, e.y)) break
            }
            return true
        }
    }

    // ✅ KHỞI TẠO SAU KHI TẤT CẢ PROPERTIES ĐÃ SẴN SÀNG
    init {
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener { view, e ->
            onTouchEvent(view, e)
        }
        attachSwipe()
    }

    // ✅ CHUYỂN LOGIC VÀO HÀM RIÊNG
    private fun onTouchEvent(view: View, e: MotionEvent): Boolean {
        if (swipedPos < 0) return false

        val point = Point(e.rawX.toInt(), e.rawY.toInt())
        val swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos)
        val swipedItem = swipedViewHolder?.itemView
        val rect = Rect()
        swipedItem?.getGlobalVisibleRect(rect)

        when (e.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE -> {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(e)
                } else {
                    recoverQueue.add(swipedPos)
                    swipedPos = -1
                    recoverSwipedItem()
                }
            }
        }
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition

        if (swipedPos != pos) {
            recoverQueue.add(swipedPos)
        }

        swipedPos = pos

        buttons.clear()
        if (buttonsBuffer.containsKey(swipedPos)) {
            buttons.addAll(buttonsBuffer[swipedPos]!!)
        }

        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons.size * BUTTON_WIDTH
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = swipeThreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = 0.1f * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float = 5.0f * defaultValue

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView

        if (pos < 0) {
            swipedPos = pos
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {
                val buffer = mutableListOf<UnderlayButton>()

                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer[pos] = buffer
                } else {
                    buffer.addAll(buttonsBuffer[pos]!!)
                }

                translationX = dX * buffer.size * BUTTON_WIDTH / itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButtons(
        c: Canvas,
        itemView: View,
        buffer: List<UnderlayButton>,
        pos: Int,
        dX: Float
    ) {
        var left = itemView.left.toFloat()
        val dButtonWidth = (-1) * dX / buffer.size

        for (button in buffer) {
            val right = left - dButtonWidth
            button.onDraw(
                c,
                RectF(
                    left,
                    itemView.top.toFloat(),
                    right,
                    itemView.bottom.toFloat()
                ),
                pos
            )
            left = right
        }
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (recoverQueue.isNotEmpty()) {
            val pos = recoverQueue.poll()
            if (pos!! > -1) {
                recyclerView.adapter?.notifyItemChanged(pos)
            }
        }
    }

    fun attachSwipe() {
        ItemTouchHelper(this).attachToRecyclerView(recyclerView)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder,
        underlayButtons: MutableList<UnderlayButton>
    )

    class UnderlayButton(
        private val context: Context,
        text: String,
        imageResId: Int,
        color: Int,
        private val clickListener: UnderlayButtonClickListener
    ) {
        var text: String = text
            private set
        var imageResId: Int = imageResId
            private set
        var color: Int = color
            private set
        var pos: Int = 0
            private set
        private var clickRegion: RectF? = null

        fun onClick(x: Float, y: Float): Boolean {
            return if (clickRegion != null && clickRegion!!.contains(x, y)) {
                clickListener.onClick(pos)
                true
            } else {
                false
            }
        }

        fun onDraw(c: Canvas, rect: RectF, pos: Int) {
            val p = Paint()

            // 1. DRAW BACKGROUND
            p.color = color
            c.drawRect(rect, p)
            if (imageResId != 0) {
                val drawable = ContextCompat.getDrawable(context, imageResId)
                val bmp = drawableToBitmap(drawable)

                if (bmp != null) {
                    val iconSize = (rect.height() * 0.4f).toInt()
                    val scaledBmp = Bitmap.createScaledBitmap(bmp, iconSize, iconSize, true)

                    // CĂN CHÍNH GIỮA NỀN
                    val left = rect.centerX() - iconSize / 2f
                    val top = rect.centerY() - iconSize / 2f
                    c.drawBitmap(scaledBmp, left, top, null)
                }
            }

            clickRegion = rect
            this.pos = pos
        }

        companion object {
            fun drawableToBitmap(drawable: Drawable?): Bitmap? {
                if (drawable is BitmapDrawable) {
                    val bitmapDrawable = drawable
                    return bitmapDrawable.bitmap
                }

                val bitmap: Bitmap
                if (drawable!!.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                    bitmap = createBitmap(1, 1)
                } else {
                    bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
                }

                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                return bitmap
            }
        }
    }

    interface UnderlayButtonClickListener {
        fun onClick(pos: Int)
    }
}