package com.lmoumou.libloadrecyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:27
 */
abstract class LoadViewCreator {

    /**
     * 获取上拉刷新的View
     *
     * @param context Context
     * @param parent recyclerView
     *
     */
    abstract fun getLoadView(context: Context, parent: ViewGroup): View


    /**
     * 正在下拉
     *
     * @param currentDragHeight  当前拖动的高度
     * @param refreshHeight  总的刷新高度
     * @param currentRefreshStatus 当前状态
     *
     */
    abstract fun onPull(currentDragHeight: Int, refreshHeight: Int, currentRefreshStatus: Int)


    /**
     * 正在加载
     */
    abstract fun onLoading()


    /**
     * 停止加载
     */
    abstract fun onStopLoad()
}