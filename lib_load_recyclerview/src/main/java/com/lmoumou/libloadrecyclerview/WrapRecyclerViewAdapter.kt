package com.lmoumou.libloadrecyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:07
 *
 * 装饰着模式
 *
 * 将Recyclerview.Adapter添加加载布局
 */
class WrapRecyclerViewAdapter(private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var footerView: View? = null

    companion object {
        private const val BASE_FOOTER_VIEWTYPE = 1000
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == BASE_FOOTER_VIEWTYPE) {
            return object : RecyclerView.ViewHolder(footerView!!) {}
        } else {
            adapter.onCreateViewHolder(parent, viewType)
        }
    }


    override fun getItemCount(): Int = if (footerView == null) adapter.itemCount else (adapter.itemCount + 1)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == BASE_FOOTER_VIEWTYPE) {
            return
        } else {
            adapter.onBindViewHolder(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (footerView == null) {
            adapter.getItemViewType(position)
        } else {
            if (position == adapter.itemCount) {
                BASE_FOOTER_VIEWTYPE
            } else {
                adapter.getItemViewType(position)
            }
        }
    }

    /**
     * 处理GridLayoutManager header和footer显示整行
     *
     * @param recyclerView
     * */
    fun adjustSpanSize(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == BASE_FOOTER_VIEWTYPE) layoutManager.spanCount else 1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            if (getItemViewType(holder.layoutPosition) == BASE_FOOTER_VIEWTYPE) {
                layoutParams.isFullSpan = true
            }
        }
    }

    /**
     * 添加加载布局
     *
     * @param view
     * */
    fun addFooterView(view: View) {
        this.footerView = view
        notifyItemChanged(adapter.itemCount)
    }

    /**
     * 移除加载布局
     * */
    fun removeFooterView() {
        this.footerView = null
        notifyItemChanged(adapter.itemCount)
    }
}