package cn.gd.snm.testviewmodule.testrecycer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.gd.snm.testviewmodule.R
import kotlinx.android.synthetic.main.activity_test_recycler.*


/**
 * 测试recyclerview
 *
 */
class TestRecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_recycler)

        //todo 初测试基础recycler
        testCommentRecyclerView()

        //todo 测试自定义头部、尾部recyclerview
//        testAttachHeaderRecyclerView()

        //todo 性能优化相关设置
//        testOptimization()

    }

    /**
     * 性能优化接口
     *
     */
    private fun testOptimization() {
        var linearLayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        //todo 当前recyclerview内部若存在其他recyclerview，则设置内部
        // 的recyclerview首次加载个数。
        linearLayoutManager.initialPrefetchItemCount = 10

        //todo 多个recyclerview可共用一个缓存池
        var recyclerPool = RecyclerView.RecycledViewPool()
        recy_view.setRecycledViewPool(recyclerPool)

        //todo 设置cache缓冲区list的大小，源码中默认是2。
        recy_view.setItemViewCacheSize(100)

    }

    /**
     * 自定义头部组件和尾部组件的recyclerview
     *
     */
    private fun testAttachHeaderRecyclerView() {
        val list = ArrayList<String>()
        for (i in 0..50) {
            list.add(i.toString())
        }

        val adapter = TestAttachHeaderAdapter(this, list)
        //todo 添加自定义的头部组件和尾部组件
        adapter.addHeaderModule(R.layout.header_item)
        adapter.addBottomModule(R.layout.bottom_item)

        recy_view.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        recy_view.addItemDecoration(TestDecoration())

        //todo: 若recyclerview大小没有发生变化，则不会调用重绘。
        // 可以优化仅数据变化的场景下，recyclerview的加载性能。
        recy_view.setHasFixedSize(true)

        recy_view.adapter = adapter
    }

    /**
     * 最简单的recyclerview
     *
     */
    private fun testCommentRecyclerView() {
        // todo 随意初始化一个list对象
        val list = ArrayList<String>()
        for (i in 0..50) {
            list.add(i.toString())
        }

        //初始化一个adapter
        var adapter = TestAdapter(this, list)

        //测试线性管理器
        recy_view.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        //测试网格管理器
//        recy_view.layoutManager = GridLayoutManager(this,3)

        recy_view.addItemDecoration(TestDecoration())

        recy_view.adapter = adapter
    }
}