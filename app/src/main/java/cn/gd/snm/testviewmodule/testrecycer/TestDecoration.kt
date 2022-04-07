package cn.gd.snm.testviewmodule.testrecycer

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
class TestDecoration:RecyclerView.ItemDecoration() {
    /**
     * 该回调决定当前这个装饰器的宽度及高度.
     *
     * outRect: 决定当前适配器最后的位置。
     *
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)

        //todo 确定当前decoration的装饰区域大小
        outRect.set(0,0,0,20)
    }

    /**
     * 当前绘制在item绘制之后，这也意味着当前绘制是可以盖在recyclerview的子item上面的。
     *
     * 需要注意的是，当前这个绘制并非是针对与recyclerview的item，而是recyclerview。
     * 这也意味着，这个方法只会走一次，如果每个item都需要做绘制，那么需要自行循环绘制。
     *
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView,
                            state: RecyclerView.State) {
//        super.onDrawOver(c, parent, state)
        //这里我们演示绘制一条和itemView一样长的直线。
        var paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f

        //todo 循环给每一个item都绘制一条线
        for(pos in 0 until parent.childCount){
            var item = parent.getChildAt(pos)

            var left = item.left
            var top = item.bottom + 10

            var right = item.right
            var bottom = item.bottom + 10

            c.drawLine(left.toFloat(),top.toFloat(),right.toFloat(),
                bottom.toFloat(),paint)
        }

    }

    /**
     * 在装饰器的范围内，进行具体的绘制。
     *
     *  当前绘制在item绘制之前。
     *
     */
    override fun onDraw(c: Canvas, parent: RecyclerView,
                        state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }



}