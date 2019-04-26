package com.lmoumou.libloadrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:51
 */
class DefaultLoadViewCreator2 : LoadViewCreator() {

    private lateinit var mIvRefresh: View
    private lateinit var mLoadTv: TextView

    override fun getLoadView(context: Context, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.load_default_view, parent, false)
        mLoadTv = view.findViewById(R.id.load_tv)
        mIvRefresh = view.findViewById(R.id.iv_refresh)
        return view
    }

    override fun onPull(currentDragHeight: Int, refreshHeight: Int, currentRefreshStatus: Int) {

    }

    override fun onLoading() {
        mLoadTv.text = "加载中"
        val animation = RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.repeatCount = -1
        animation.duration = 1288
        mIvRefresh.startAnimation(animation)
    }

    override fun onStopLoad() {
        mIvRefresh.rotation = 0f
        mIvRefresh.clearAnimation()
        mLoadTv.text = "上拉加载更多"
    }

}