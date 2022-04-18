package cn.gd.snm.testviewmodule.testfocus

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.gd.snm.testviewmodule.R
import kotlinx.android.synthetic.main.activity_fix_focus.*

/**
 * 测试项目中需要的定焦功能
 *
 */
class FixFocusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fix_focus)

        test()
    }

    private fun test() {
        val list = ArrayList<String>()
        for (i in 0..50) {
            list.add(i.toString())
        }

        var adapter = FixFocusAdapter(this, list)

        fix_focus_recy.layoutManager = getLayoutManger(fix_focus_recy)

        fix_focus_recy.adapter = adapter

        fix_focus_recy.addItemDecoration(FocusRecDecoration())

        fix_focus_recy.post {
            fix_focus_recy.getChildAt(0).requestFocus()
        }
    }

    private fun getLayoutManger(recyclerView: RecyclerView): LinearLayoutManager? {
        return object : LinearLayoutManager(this, LinearLayout.VERTICAL, false) {
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean {
                return fix_focus_recy.requestChildRectangleOnScreen(child, rect, immediate)
            }

            //todo 最后实际滑动的距离，需要重写
            override fun scrollVerticallyBy(
                dy: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                return super.scrollVerticallyBy(dy, recycler, state)
            }
        }

    }


}
