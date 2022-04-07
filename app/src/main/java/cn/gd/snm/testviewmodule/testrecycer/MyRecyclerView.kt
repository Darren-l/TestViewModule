package cn.gd.snm.testviewmodule.testrecycer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 用于源码查看
 *
 */
class MyRecyclerView(context: Context,attributeSet: AttributeSet):RecyclerView(context,attributeSet) {


    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return super.onTouchEvent(e)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

}