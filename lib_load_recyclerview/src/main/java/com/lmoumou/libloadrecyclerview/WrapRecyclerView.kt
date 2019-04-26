package com.lmoumou.libloadrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:19
 */
open class WrapRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private lateinit var mWrapRecyclerAdapter: WrapRecyclerViewAdapter

    override fun setAdapter(adapter: Adapter<RecyclerView.ViewHolder>?) {
        adapter?.let {
            mWrapRecyclerAdapter = if (it is WrapRecyclerViewAdapter) {
                it
            } else {
                WrapRecyclerViewAdapter(it)
            }
        }

        super.setAdapter(mWrapRecyclerAdapter)
        mWrapRecyclerAdapter.adjustSpanSize(this)
    }

    fun addLoadView(view: View) {
        mWrapRecyclerAdapter.addFooterView(view)
    }

    fun removeLoadView() {
        mWrapRecyclerAdapter.removeFooterView()
    }
}