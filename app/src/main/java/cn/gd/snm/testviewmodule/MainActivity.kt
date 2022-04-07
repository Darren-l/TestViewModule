package cn.gd.snm.testviewmodule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.gd.snm.testviewmodule.testrecycer.TestRecyclerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //todo 普通使用。
        test1()
    }

    private fun test1() {
        startActivity(Intent(this,TestRecyclerActivity::class.java))

    }
}