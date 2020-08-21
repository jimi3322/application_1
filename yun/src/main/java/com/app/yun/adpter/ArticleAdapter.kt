package com.app.yun.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.yun.R
import com.app.yun.data.Artical

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
    private var mList: MutableList<Artical>?=null

    constructor(list: MutableList<Artical>?){
        this.mList = list//将DeviceInfoData存入mList中
    }


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val authorName:TextView = view.findViewById(R.id.authorName)
        val titleName:TextView = view.findViewById(R.id.titleName)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item,parent,false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        if (item!=null){
            holder.authorName.text = item.author
            holder.titleName.text = item.title
        }

     //   holder.fruitImage.setImageResource(fruit.imageId)
      //  holder.fruitName.text = fruit.name
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}