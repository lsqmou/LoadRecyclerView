package com.lmoumou.decoratedemo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmoumou.libloadrecyclerview.DefaultLoadViewCreator2
import com.lmoumou.libloadrecyclerview.LoadRecyclerView2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var start: Int = 0
    private var end: Int = 15

    private val dataList: MutableList<String> by lazy { mutableListOf<String>() }
    private val adapter: MainAdapter by lazy { MainAdapter(this, dataList) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        initData(start, end)

        mReclerView.layoutManager = LinearLayoutManager(this)
        mReclerView.addLoadViewCreator(DefaultLoadViewCreator2())
        mReclerView.adapter = adapter
//        mReclerView.bindRefreshView(mSwipeRefreshLayout)

        mReclerView.addOnLoadListener(object : LoadRecyclerView2.OnLoadListener {
            override fun onLoad(view: LoadRecyclerView2) {
                Log.e("MainActivity", "onLoad")
                start += 15
                end += 15
                Handler().postDelayed({
                    initData(start, end)
                    adapter.notifyDataSetChanged()
                    view.onStopLoad()
                }, 1000)
            }
        })

    }

    private fun initData(start: Int, end: Int) {
        for (i in start until end) {
            dataList.add("Item$i")
        }
    }
}
