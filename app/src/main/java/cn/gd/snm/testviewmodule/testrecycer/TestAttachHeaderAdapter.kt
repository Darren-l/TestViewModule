package cn.gd.snm.testviewmodule.testrecycer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.gd.snm.testviewmodule.R

/**
 * 测试带头部和尾部的adapter。
 *
 */
class TestAttachHeaderAdapter(var mcontext: Context, var datas: List<String>)
    : RecyclerView.Adapter<TestAttachHeaderAdapter.TestAttachHolder>() {

    /**
     * 定义recycler view的item类型
     */
    companion object{
        const val TYPE_HEADER = 101
        const val TYPE_BOTTOM = 102
        const val TYPE_BODY = 103
    }

    /**
     * 根据不同的type类型，创建不同的holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestAttachHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderHolder(mcontext,headerView!!)
            }
            TYPE_BOTTOM -> {
                BottomHolder(mcontext,bottomView!!)
            }
            else -> {
                var itemLay = LayoutInflater.from(mcontext).inflate(R.layout.itemview,
                    parent, false)
                BodyHolder(mcontext,itemLay)
            }
        }
    }

    override fun onBindViewHolder(holder: TestAttachHolder, position: Int) {
        holder.onBinderView(position,datas)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    private var bottomView:View ?= null
    /**
     * 外部设置尾部布局
     */
    fun addBottomModule(id:Int){
        bottomView = LayoutInflater.from(mcontext).inflate(id,null)
    }

    private var headerView:View ?= null
    /**
     * 外部设置头部布局
     */
    fun addHeaderModule(id:Int){
        headerView = LayoutInflater.from(mcontext).inflate(id,null)
    }

    /**
     * 根据pos，决定当前pos的view所对应的type类型。
     *
     *  假设如果recyclerview的子item布局存在三种，那么这里应该自定义对应三个类型。
     *
     */
    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        if(headerView != null && position == 0){
            return TYPE_HEADER
        }else if(bottomView != null && position == datas.size - 1){
            return TYPE_BOTTOM
        }
        return TYPE_BODY
    }

    /**
     * 处理body view要的数据绑定
     */
    class BodyHolder(var mcontext:Context,view:View):TestAttachHolder(mcontext,view){
        override fun onBinderView(position: Int,datas: List<String>) {

            var img: ImageView = itemView.findViewById(R.id.img)
            var tx: TextView = itemView.findViewById(R.id.tv)

            img.setImageDrawable(mcontext.resources.getDrawable(R.drawable.img))
            tx.text = "标题 -- ${datas[adapterPosition]}"
        }
    }

    /**
     * 处理header view要绑定的数据
     */
    class HeaderHolder(var mcontext:Context,view:View):TestAttachHolder(mcontext,view){
        override fun onBinderView(position: Int,datas: List<String>) {
            var tv:TextView = itemView.findViewById(R.id.tv)

            tv.text = "${tv.text} $position"
        }
    }

    /**
     * 处理bottom view 要绑定的数据
     */
    class BottomHolder(var mcontext:Context, view:View):TestAttachHolder(mcontext,view){
        override fun onBinderView(position: Int,datas: List<String>) {
            var tv:TextView = itemView.findViewById(R.id.tv)

            tv.text = "${tv.text} $position"
        }
    }

    /**
     * recyclerview所有item的持有者holder
     *
     *  接受一个布局view。
     *
     */
    abstract class TestAttachHolder(var context:Context, itemView: View)
        :RecyclerView.ViewHolder(itemView){

        abstract fun onBinderView(position: Int,datas: List<String>)
    }

}