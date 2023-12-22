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


    // Kelas yang merepresentasikan setiap item tampilan dalam RecyclerView
    class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
        val title : TextView = itemList.findViewById(R.id.tvTitleUser)
        val author : TextView = itemList.findViewById(R.id.tvAuthorUser)
        val image : ImageView = itemList.findViewById(R.id.ivImageUser)
        val desc : TextView = itemList.findViewById(R.id.tvDescUser)
    }



    // Fungsi yang menginisialisasi ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.useritemfilm,parent,false)
        return MyViewHolder(view)
    }



    // Fungsi yang menghubungkan data ke tampilan setiap item
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Mendapatkan objek data dari posisi tertentu dalam daftar
        val currentItem = itemList[position]

        // Menetapkan teks dari objek data ke tampilan setiap item
        holder.title.text = currentItem.title
        holder.author.text = currentItem.author
        holder.desc.text = currentItem.desc

        // Menggunakan Glide untuk memuat dan menampilkan gambar dari URL ke ImageView
        Glide.with(holder.image)
            .load(currentItem.imageurl.toUri())
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)


        // Menambahkan OnClickListener untuk menavigasi ke DetailActivity saat item diklik
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)

            // Menyertakan data yang diperlukan ke dalam intent
            intent.putExtra("title",currentItem.title)
            intent.putExtra("author",currentItem.author)
            intent.putExtra("description",currentItem.desc)
            intent.putExtra("imgId", currentItem.imageurl)

            // Memulai DetailActivity dengan intent yang disiapkan
            holder.itemView.context.startActivity(intent)
        }
    }


    // Fungsi yang mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int {
        return itemList.size
    }


    // Fungsi untuk memperbarui data daftar dan memberi tahu RecyclerView
    fun updateData(newList: List<Note>) {
        itemList = newList
        notifyDataSetChanged()
    }

}