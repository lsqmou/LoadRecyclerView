package com.lmoumou.libloadrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:25
 */
class LoadRecyclerView2 : WrapRecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    companion object {
        //正常状态
        private const val LOAD_STATUS_NORMAL = 0x0011
        //加载状态
        private const val LOAD_STATUS_LOADIND = 0x0022
    }

    private var mLoadViewCreator: LoadViewCreator = DefaultLoadViewCreator()
    private lateinit var mLoadView: View
    private var mCurrentLoadStatus: Int = LOAD_STATUS_NORMAL
    //加载监听
    private var mListener: OnLoadListener? = null
    //是否在加载
    private var isLoading: Boolean = false

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)
        if (parent is SwipeRefreshLayout){
            mSwipeRefreshLayout= parent as SwipeRefreshLayout?
        }
        addLoadView()
    }

    fun addLoadViewCreator(mLoadViewCreator: LoadViewCreator) {
        this.mLoadViewCreator = mLoadViewCreator
        addLoadView()
    }

    private fun addLoadView() {
        this.mLoadView = mLoadViewCreator.getLoadView(context, this)
        if (adapter != null) {
            addLoadView(mLoadView)
        }

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mSwipeRefreshLayout == null) {
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                        >= recyclerView.computeVerticalScrollRange()
                        && mCurrentLoadStatus == LOAD_STATUS_NORMAL
                        && !isLoading
                    ) {
                        mListener?.apply {
                            onLoad(this@LoadRecyclerView2)
                        }
                        mCurrentLoadStatus = LOAD_STATUS_LOADIND
                        isLoading = true
                        mLoadViewCreator.onLoading()
                    }
                } else {
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                        >= recyclerView.computeVerticalScrollRange()
                        && mCurrentLoadStatus == LOAD_STATUS_NORMAL
                        && !isLoading
                        && !mSwipeRefreshLayout!!.isRefreshing
                    ) {
                        mListener?.apply {
                            onLoad(this@LoadRecyclerView2)
                        }
                        mCurrentLoadStatus = LOAD_STATUS_LOADIND
                        isLoading = true
                        mLoadViewCreator.onLoading()
                    }
                }


            }
        })
    }


    interface OnLoadListener {
        fun onLoad(view: LoadRecyclerView2)
    }

    /**
     * 添加监听
     *
     * @param listaner
     * */
    fun addOnLoadListener(listaner: OnLoadListener) {
        this.mListener = listaner
    }

    /**
     * 停止加载
     * */
    fun onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL
        mLoadViewCreator.onStopLoad()
        isLoading = false
    }

    /**
     * 当前状态
     *
     * @return isLoading
     * */
    fun isLoading(): Boolean {
        return isLoading
    }

    /**
     * 设置刷新状态
     *
     * @param isRefresh
     * */
    fun bindRefreshView(mSwipeRefreshLayout: SwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout
    }


}