package com.lmoumou.decoratedemo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main.view.*

/**
 * 作者：Lmoumou
 * 时间：2019/4/26 09:54
 */
class MainAdapter(
    mContext: Context,
    private val dataList: MutableList<String>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(mInflater.inflate(R.layout.item_main, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.itemView.mTextView.text = dataList[position]
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}