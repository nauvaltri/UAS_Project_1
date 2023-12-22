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



    // ViewHolder untuk menyimpan referensi ke elemen UI
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val desc: TextView = itemView.findViewById(R.id.tvDesc)
        val author: TextView = itemView.findViewById(R.id.tvAuthor)
        val image: ImageView = itemView.findViewById(R.id.ivImage)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

    }


    // Membuat ViewHolder baru saat dibutuhkan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemfilm, parent, false)
        return ItemHolder(view)
    }

    // Mendapatkan jumlah item dalam daftar
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Menghubungkan data dengan elemen dalam ViewHolder
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = itemList[position]


        // Menetapkan nilai dari objek ItemData ke elemen UI dalam ViewHolder
        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.author.text = item.author

        // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
        Glide.with(holder.image)
            .load(item.imageurl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.image)

        // Menambahkan OnClickListener untuk tombol Edit
        holder.btnEdit.setOnClickListener {


            // Menginisialisasi intent untuk membuka AdminListUpdateMainActivity
            val intent = Intent(holder.itemView.context, AdminListUpdateMainActivity::class.java)

            // Mengirim data item yang dipilih ke AdminListUpdateMainActivity
            intent.putExtra("itemId", item.id)
            intent.putExtra("itemTitle", item.title.toString())
            intent.putExtra("itemImageUrl", item.imageurl.toString())
            intent.putExtra("itemDesc", item.desc.toString())
            intent.putExtra("itemAuthor", item.author.toString())

            // Memulai aktivitas dengan intent yang disiapkan
            holder.itemView.context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, position)
        }
    }


    // Menampilkan dialog konfirmasi penghapusan item
    private fun showDeleteConfirmationDialog(context: Context, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Confirmation")
            .setMessage("Apakah Anda ingin Menghapus ?")
            .setPositiveButton("Ya") { _, _ ->

                // Mendapatkan nama file gambar yang akan dihapus dari Firebase Storage
                val itemToDelete =
                    Uri.parse(itemList[position].imageurl.toString()).lastPathSegment?.removePrefix("images/")
                itemList.removeAt(position)
                notifyItemRemoved(position)
                deleteItemFromDatabase(itemToDelete.toString())
            }
            .setNegativeButton("Tidak ") { dialog, _ ->

                // Menutup dialog jika tombol Tidak ditekan
                dialog.dismiss()
            }
            .create()
            .show()
    }


    // Menghapus item dari Firebase Storage dan Database
    private fun deleteItemFromDatabase(imgId: String) {
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)


        // Menghapus file gambar dari Firebase Storage
        storageReference.delete().addOnSuccessListener {
            val database = FirebaseDatabase.getInstance().getReference("Admin")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                    // Handle success jika diperlukan
                }
                .addOnFailureListener {
                    // Handle failure if needed
                }
        }.addOnFailureListener {
            Log.e("RecyclerViewAdapterAdmin", "Error deleting image: ${it.message}")
        }
    }
}
