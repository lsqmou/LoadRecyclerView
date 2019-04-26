package com.lmoumou.libloadrecyclerview

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:25
 */
class LoadRecyclerView : WrapRecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    companion object {
        //正常状态
        private const val LOAD_STATUS_NORMAL = 0x0011
        //加载状态
        private const val LOAD_STATUS_LOADIND = 0x0022
        //上滑加载状态
        const val LOAD_STATUS_PULL_DOWN_REFRESH = 0x0033
    }

    private var mLoadViewCreator: LoadViewCreator = DefaultLoadViewCreator()
    private lateinit var mLoadView: View
    private var mCurrentLoadStatus: Int = LOAD_STATUS_NORMAL
    //加载监听
    private var mListener: OnLoadListener? = null
    //加载布局的高度
    private var mLoadViewHeight: Int = 0
    //阻力值
    private val mDragIndex: Float = 0.35f

    //按下Y坐标
    private var mFingerDownY: Int = 0
    private var mCurrentDrag: Boolean = false
    //是否在刷新
    private var isLoading: Boolean = false

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)

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
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mFingerDownY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {
                if (mCurrentDrag) {
                    restoreLoadView()
                }
            }

        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                if (this.canScrollVertically(1) ||
                    mCurrentLoadStatus == LOAD_STATUS_LOADIND
                ) {
                    isLoading = false
                    return super.onTouchEvent(e)
                }
                mLoadViewHeight = mLoadView.measuredHeight

                if (mCurrentDrag) scrollToPosition(adapter!!.itemCount - 1)

                val distanceY = ((e.rawY - mFingerDownY) * mDragIndex).toInt()

                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY)
                    updateLoadStatus(-distanceY)
                    mCurrentDrag = true
                    isLoading = true
                }

            }
        }

        return super.onTouchEvent(e)
    }

    /**
     * 更新状态
     *
     * @param distanceY
     * */
    private fun updateLoadStatus(distanceY: Int) {
        mCurrentLoadStatus = when {
            distanceY <= 0 -> LOAD_STATUS_NORMAL
            distanceY < mLoadViewHeight/2 -> LOAD_STATUS_PULL_DOWN_REFRESH
            else -> LOAD_STATUS_LOADIND
        }
    }

    /**
     * 复原加载布局
     * */
    private fun restoreLoadView() {
        val currentBottomMargin = (mLoadView.layoutParams as MarginLayoutParams).bottomMargin//加载布局目前的BottomMargin
        val finalBottomMargin = 0//最终的MarginBottom
        if (mCurrentLoadStatus == LOAD_STATUS_LOADIND) {
            mLoadViewCreator.onLoading()
            mListener?.apply {
                onLoad(this@LoadRecyclerView)
            }
        }

        val distance = currentBottomMargin - finalBottomMargin
        val animator = ObjectAnimator.ofFloat(currentBottomMargin.toFloat(), finalBottomMargin.toFloat())
            .setDuration(distance.toLong())
        animator.addUpdateListener { animation ->
            @Suppress("NAME_SHADOWING")
            val currentBottomMargin = animation.animatedValue as Float
            setLoadViewMarginBottom(currentBottomMargin.toInt())
        }
        animator.start()
        mCurrentDrag = false
    }

    /**
     * 设置刷新topMargin
     */
    private fun setLoadViewMarginBottom(marginBottom: Int) {
        var bottomMargin = marginBottom
        val params = mLoadView.layoutParams as MarginLayoutParams
        if (marginBottom < 0) {
            bottomMargin = 0
        }
        params.bottomMargin = bottomMargin
        mLoadView.layoutParams = params
    }

    interface OnLoadListener {
        fun onLoad(view: LoadRecyclerView)
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
        restoreLoadView()
        mLoadViewCreator.onStopLoad()
    }

    /**
     * 当前状态
     *
     * @return isLoading
     * */
    fun isLoading(): Boolean {
        return isLoading
    }


}