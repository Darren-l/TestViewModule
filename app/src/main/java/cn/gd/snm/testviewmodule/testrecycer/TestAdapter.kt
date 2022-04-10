package cn.gd.snm.testviewmodule.testrecycer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.gd.snm.testviewmodule.R


/**
 * 测试最简易的adapter
 *
 */
open class TestAdapter(var mcontext: Context, var datas: MutableList<String>) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    companion object{
        private var TAG = "##darren"
    }

    private var onCreateAccount = 0
    /**
     * 绑定布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        Log.d(TAG,"onCreateViewHolder... viewType=${viewType},累积创建${onCreateAccount++}")
        var itemLay = LayoutInflater.from(mcontext).inflate(R.layout.itemview, parent,false)

        var holder = TestViewHolder(itemLay)

        //todo 优化点1，监听放到onCreate中设置，防止在onBind中重复创建监听对象，引起内存抖动。
        holder.itemView.setOnClickListener{
            Toast.makeText(mcontext,"点击了第${datas[holder.adapterPosition]}个",Toast.LENGTH_SHORT)
                .show()
            //测试刷新
            testNotify()
        }

        return holder
    }

    /**
     * 测试数据刷新。
     */
    private fun testNotify() {
        datas[2] = "###更改数据了###"
        //todo 全部刷新
//        notifyDataSetChanged()

        //todo 指定刷新位置2的数据
        notifyItemChanged(2)
    }

    //todo 假设这是外部传进来的数据
//    private var dataBean = ArrayList<ItemBean>()
    /**
     * 绑定数据
     */
    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder... pos=${position}")

        holder.img.setImageDrawable(mcontext.resources.getDrawable(R.drawable.img))
        holder.tx.text = "标题 -- ${datas[holder.adapterPosition]}"

        //todo: 设置高亮
//        if(dataBean[position].isLight){
//            holder.light.visibility = View.VISIBLE
//        }else{
//            holder.light.visibility = View.INVISIBLE
//        }
    }

    /**
     * 获取个数
     */
    override fun getItemCount(): Int {
        return datas.size
    }


    /**
     * viewHolder作用：
     *
     *  1. 包装view。
     *  2. 绑定当前item的布局。
     *
     * 根据缓存的原理，创建时会调用onCreateViewHolder获取，从缓存中拿时，会调用onBindViewHolder。
     *
     * 注意这里构造中的itemView参数，实际就是布局view。
     *
     */
    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.img)
        var tx: TextView = itemView.findViewById(R.id.tv)
        var light:View = itemView.findViewById(R.id.light)

    }
}