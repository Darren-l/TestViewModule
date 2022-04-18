package cn.gd.snm.testviewmodule.testfocus

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * recyclerview的装饰器。
 *
 */
class FocusRecDecoration:RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(0,0,0,20)
    }



}