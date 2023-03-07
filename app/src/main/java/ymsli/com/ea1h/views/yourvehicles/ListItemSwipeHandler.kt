package ymsli.com.ea1h.views.yourvehicles

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date   JAN 05, 2021
 * Copyright (c) 2021, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ListItemSwipeHandler : ItemTouchHelper.SimpleCallback implementation that provides the
 *                        swipe to delete feature.
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
abstract class ListItemSwipeHandler(private val backgroundIcon: Drawable):
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private companion object{
        private const val BACKGROUND_ICON_LEFT = 0
        private const val BACKGROUND_ICON_TOP = 1
        private const val BACKGROUND_ICON_RIGHT = 2
        private const val BACKGROUND_ICON_BOTTOM = 3
        private const val BACKGROUND_LEFT = 4
        private const val BACKGROUND_TOP = 5
        private const val BACKGROUND_RIGHT = 6
        private const val BACKGROUND_BOTTOM = 7
        private val backgroundColor = Color.parseColor("#EB4242")
    }

    private val intrinsicWidth = backgroundIcon.intrinsicWidth
    private val intrinsicHeight = backgroundIcon.intrinsicHeight
    private val background = ColorDrawable()
    private val coordinate = IntArray(8)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun isItemViewSwipeEnabled(): Boolean = true

    /**
     * This implementation doesn't provide move feature.
     * @author Balraj VE00YM023
     */
    override fun onMove(p0: RecyclerView,
                        p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean = false

    /**
     * This function is responsible for drawing the background color when any item in
     * recycler view is swiped left or right.
     *
     * @author Balraj VE00YM023
     */
    override fun onChildDraw(c: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        /** If swipe direction is left then draw on the left side */
        coordinate[BACKGROUND_LEFT]   = itemView.right + dX.toInt()
        coordinate[BACKGROUND_TOP]    = itemView.top
        coordinate[BACKGROUND_RIGHT]  = itemView.right
        coordinate[BACKGROUND_BOTTOM] = itemView.bottom

        val backgroundIconMargin = (itemHeight - intrinsicHeight) / 2
        coordinate[BACKGROUND_ICON_TOP] = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        coordinate[BACKGROUND_ICON_LEFT] = itemView.right - backgroundIconMargin - intrinsicWidth!!
        coordinate[BACKGROUND_ICON_RIGHT] = itemView.right - backgroundIconMargin
        coordinate[BACKGROUND_ICON_BOTTOM] = coordinate[BACKGROUND_ICON_TOP] + intrinsicHeight

        background.color = backgroundColor
        background.setBounds(coordinate[BACKGROUND_LEFT], coordinate[BACKGROUND_TOP],
            coordinate[BACKGROUND_RIGHT], coordinate[BACKGROUND_BOTTOM])
        background.draw(c)
        backgroundIcon.setBounds(coordinate[BACKGROUND_ICON_LEFT], coordinate[BACKGROUND_ICON_TOP],
            coordinate[BACKGROUND_ICON_RIGHT], coordinate[BACKGROUND_ICON_BOTTOM])
        backgroundIcon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}