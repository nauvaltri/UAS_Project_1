package com.example.uas_project_1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ItemAdapter(private val itemList: ArrayList<ItemData>) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val desc: TextView = itemView.findViewById(R.id.tvDesc)
        val author: TextView = itemView.findViewById(R.id.tvAuthor)
        val image: ImageView = itemView.findViewById(R.id.ivImage)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemfilm, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = itemList[position]

        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.author.text = item.author


        Glide.with(holder.image)
            .load(item.imageurl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.image)

        holder.btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminListUpdateMainActivity::class.java)
            intent.putExtra("itemId", item.id)
            intent.putExtra("itemTitle", item.title.toString())
            intent.putExtra("itemImageUrl", item.imageurl.toString())
            intent.putExtra("itemDesc", item.desc.toString())
            intent.putExtra("itemAuthor", item.author.toString())
            holder.itemView.context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, position)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Confirmation")
            .setMessage("Apakah Anda ingin Menghapus ?")
            .setPositiveButton("Ya") { _, _ ->
                val itemToDelete =
                    Uri.parse(itemList[position].imageurl.toString()).lastPathSegment?.removePrefix("images/")
                itemList.removeAt(position)
                notifyItemRemoved(position)
                deleteItemFromDatabase(itemToDelete.toString())
            }
            .setNegativeButton("Tidak ") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteItemFromDatabase(imgId: String) {
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)

        storageReference.delete().addOnSuccessListener {
            val database = FirebaseDatabase.getInstance().getReference("Admin")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                    // Handle success if needed
                }
                .addOnFailureListener {
                    // Handle failure if needed
                }
        }.addOnFailureListener {
            Log.e("RecyclerViewAdapterAdmin", "Error deleting image: ${it.message}")
        }
    }
}
