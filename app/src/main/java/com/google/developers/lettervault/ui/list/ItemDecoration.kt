package com.google.developers.lettervault.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Add margin for list item
 *
 * @param dimenPixel pixel size {@see Resources#getDimensionPixelSize}
 */
class ItemDecoration(private val dimenPixel: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(dimenPixel, dimenPixel, dimenPixel, dimenPixel)
    }
}
