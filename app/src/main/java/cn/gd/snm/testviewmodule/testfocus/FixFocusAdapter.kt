package cn.gd.snm.testviewmodule.testfocus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.gd.snm.testviewmodule.R

/**
 * 测试定焦
 *
 */
class FixFocusAdapter(private var mcontext: Context, var datas: MutableList<String>) :
    RecyclerView.Adapter<FixFocusAdapter.FixFocusHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixFocusHolder {
        var itemLay = LayoutInflater.from(mcontext).inflate(R.layout.fix_focus_item, parent,false)

        itemLay.isFocusable = true


        itemLay.setOnFocusChangeListener {
                view, isFocus ->
            if (isFocus){
                view.setBackgroundResource(R.drawable.detail_focus_bg)
                AnimatorUtils.setScaleAnimator2(view, true, 1.1f)
            }else{
                view.setBackgroundResource(R.drawable.unfocus_nocolor_corner_bg)
                AnimatorUtils.setScaleAnimator2(view, false, 1.1f)
            }
        }

        var holder = FixFocusHolder(itemLay)

        return holder
    }

    override fun onBindViewHolder(holder: FixFocusHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    class FixFocusHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.img)
        var tx: TextView = itemView.findViewById(R.id.tv)
        var light: View = itemView.findViewById(R.id.light)

    }
}