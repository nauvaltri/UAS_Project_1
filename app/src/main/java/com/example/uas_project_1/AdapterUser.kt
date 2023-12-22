package com.example.uas_project_1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_project_1.Database.Note

class AdapterUser (private var itemList : List<Note>) : RecyclerView.Adapter<AdapterUser.MyViewHolder>() {
    class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
        val title : TextView = itemList.findViewById(R.id.tvTitleUser)
        val author : TextView = itemList.findViewById(R.id.tvAuthorUser)
        val image : ImageView = itemList.findViewById(R.id.ivImageUser)
        val desc : TextView = itemList.findViewById(R.id.tvDescUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.useritemfilm,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.title
        holder.author.text = currentItem.author
        holder.desc.text = currentItem.desc

        // Load and display the image using Glide
        Glide.with(holder.image)
            .load(currentItem.imageurl.toUri())
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("title",currentItem.title)
            intent.putExtra("author",currentItem.author)
            intent.putExtra("description",currentItem.desc)
            intent.putExtra("imgId", currentItem.imageurl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateData(newList: List<Note>) {
        itemList = newList
        notifyDataSetChanged()
    }

}